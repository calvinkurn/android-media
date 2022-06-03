package com.tokopedia.telemetry.sensorlistener

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.tokopedia.telemetry.model.Telemetry

object TelemetryAccelListener: SensorEventListener {
    override fun onSensorChanged(event: SensorEvent?) {
        if (event!= null) {
            Telemetry.addAccel(event.values[0], event.values[1], event.values[2])
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}