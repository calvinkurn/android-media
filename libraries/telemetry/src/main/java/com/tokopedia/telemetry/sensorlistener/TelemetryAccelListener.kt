package com.tokopedia.telemetry.sensorlistener

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.tokopedia.telemetry.model.Telemetry
import java.lang.ref.WeakReference

object TelemetryAccelListener : SensorEventListener {
    private var activityRef: WeakReference<Activity>? = null

    fun setActivity(activity: Activity?) {
        activityRef = if (activity == null) {
            null
        } else {
            WeakReference(activity)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val isValid = Telemetry.addAccel(event.values[0], event.values[1], event.values[2])
            if (!isValid) {
                val activity = activityRef?.get()
                if (activity != null) {
                    val sensorManager =
                        activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
                    sensorManager?.unregisterListener(this)
                    activityRef = null
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}