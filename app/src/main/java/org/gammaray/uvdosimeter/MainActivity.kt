package org.gammaray.uvdosimeter

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.gammaray.uvdosimeter.R.layout.main_activity
import org.gammaray.uvdosimeter.ui.theme.UVDosimeterTheme


class MainActivity : ComponentActivity() {
    private val delta=0.000001
    private val fourPi= 4*3.14159265359
    private val powerPerPoint=2500*delta/17
    private val powerPerPointPer4Pi: Double = powerPerPoint/fourPi
    private lateinit var etTubeLength :EditText
    private lateinit var etDistance :EditText
    private lateinit var etOffset :EditText
    private lateinit var etTime :EditText
    private lateinit var etDose :TextView
    private val defaultDose="Dose: 0.0 mJcm⁻²"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(main_activity)
        etTubeLength =findViewById(R.id.tubeLength)
        etDistance =findViewById(R.id.distance)
        etOffset =findViewById(R.id.offset)
        etTime =findViewById(R.id.time)
        etDose =findViewById(R.id.dose)

        etTubeLength.setHintTextColor(Color.GRAY)
        etDistance.setHintTextColor(Color.GRAY)
        etOffset.setHintTextColor(Color.GRAY)
        etTime.setHintTextColor(Color.GRAY)

    }
    fun onClickListener(view: View) {
        val tubeLength: Double= etTubeLength.text.toString().toDouble()
        val distance: Double= etDistance.text.toString().toDouble()
        val offset0: Double= etOffset.text.toString().toDouble()
        val time: Double= etTime.text.toString().toDouble()
        if(tubeLength>17){
            etTubeLength.text.clear()
            etTubeLength.setHintTextColor(Color.RED)
            etDose.setTextColor(Color.BLACK)
            etDose.text= defaultDose
        }
        else if (offset0>tubeLength/2){
            etOffset.text.clear()
            etOffset.setHintTextColor(Color.RED)
            etDose.setTextColor(Color.BLACK)
            etDose.text= defaultDose
        }
        else{
            val dose: Double
            val distanceSqr = distance*distance
            var flux=0.0
            var i =0.0
            val offset=tubeLength/2-offset0;

            while (i<=tubeLength-offset){
                flux+=powerPerPointPer4Pi/(i*i+distanceSqr)
                i+=delta;
            }
            i=delta
            while (i<=offset){
                flux+=powerPerPointPer4Pi/(i*i+distanceSqr)
                i+=delta
            }
            dose= flux*time
            //05b26f
            if(dose<10.0){
                etDose.setTextColor(Color.parseColor("#05b26f"))
            }
            //0aa20a
            else if(dose<20.0){
                etDose.setTextColor(Color.parseColor("#0aa20a"))
            }
            //87a20a
            else if(dose<40.0){
                etDose.setTextColor(Color.parseColor("#87a20a"))
            }
            //acb205
            else if(dose<60.0){
                etDose.setTextColor(Color.parseColor("#acb205"))
            }
            //b27f05
            else if(dose<100.0){
                etDose.setTextColor(Color.parseColor("#b27f05"))
            }
            //b23e05
            else if(dose<200.0){
                etDose.setTextColor(Color.parseColor("#b23e05"))
            }
            //b20505
            else if(dose<400.0){
                etDose.setTextColor(Color.parseColor("#b20505"))
            }
            else
                etDose.setTextColor(Color.parseColor("#000000"))

            etDose.text= buildString {
                append("Dose: ")
                append(String.format("%.3f", dose))
                append(" mJcm⁻²")
            }
            etTubeLength.setHintTextColor(Color.GRAY)
            etOffset.setHintTextColor(Color.GRAY)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UVDosimeterTheme {
        Greeting("Android")
    }
}