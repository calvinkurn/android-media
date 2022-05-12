package com.tokopedia.telemetry

import android.app.Activity
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.telemetry.model.Telemetry
import com.tokopedia.telemetry.network.TelemetryWorker
import com.tokopedia.telemetry.sensorlistener.TelemetryAccelListener
import com.tokopedia.telemetry.sensorlistener.TelemetryGyroListener
import com.tokopedia.telemetry.sensorlistener.TelemetryTextWatcher
import com.tokopedia.telemetry.sensorlistener.TelemetryTouchListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class TelemetryActLifecycleCallback : Application.ActivityLifecycleCallbacks {

    companion object {
        var prevActivityRef: WeakReference<AppCompatActivity>? = null
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // stop telemetry collection for previous activity to prevent listener leaking
        val prevActRef = prevActivityRef
        if (prevActRef != null) {
            val prevAct = prevActRef.get()
            if (prevAct != null) {
                stopTelemetryListener(prevAct)
            }
            prevActivityRef = null
        }

        // start new telemetry section
        if (activity !is AppCompatActivity) {
            return
        }
        if (activity is ITelemetryActivity) {
            val sectionName = activity.getTelemetrySectionName()
            //stop time for prev telemetry
            Telemetry.addStopTime(sectionName)

            TelemetryWorker.scheduleWorker(activity.applicationContext)

            // add new section
            Telemetry.addSection(sectionName)

            registerTelemetryListener(activity)

            // timer to stop after telemetry duration
            activity.lifecycleScope.launch {
                delay(SECTION_TELEMETRY_DURATION)
                stopTelemetryListener(activity)
                Log.w("HENDRYTAG", "Telemetry stop in tele activity")
                Telemetry.addStopTime()
            }

        } else { // this activity is not telemetry activity
            if (Telemetry.hasOpenTime()) {
                // check if it is already past section duration or not
                val elapsedDiff = Telemetry.getElapsedDiff()
                if (elapsedDiff < (SECTION_TELEMETRY_DURATION - STOP_THRES)) {
                    registerTelemetryListener(activity)
                    // timer to stop after telemetry duration
                    activity.lifecycleScope.launch {
                        val remainingDurr = SECTION_TELEMETRY_DURATION - elapsedDiff
                        delay(remainingDurr)
                        stopTelemetryListener(activity)
                        Log.w("HENDRYTAG", "Telemetry stop in non-tele activity")
                        Telemetry.addStopTime()
                    }
                } else { // duration is due
                    Log.w("HENDRYTAG", "Telemetry stop in non-tele activity; due")
                    val estimatedDuration = Telemetry.telemetrySectionList[0].startTime + SECTION_TELEMETRY_DURATION
                    Telemetry.addStopTime("", estimatedDuration)
                }
            }
        }
    }

    private fun registerTelemetryListener(activity: AppCompatActivity) {
        activity.lifecycleScope.launch {
            try {
                activity.findViewById<View>(R.id.content).viewTreeObserver
                    .addOnGlobalFocusChangeListener { _, newFocus ->
                        if (newFocus is EditText) {
                            val et: EditText = newFocus
                            et.removeTextChangedListener(TelemetryTextWatcher)
                            et.addTextChangedListener(TelemetryTextWatcher)
                        }
                    }
                val sensorManager: SensorManager? =
                    activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
                val sensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

                sensorManager?.registerListener(TelemetryAccelListener, sensor, SAMPLING)

                val sensorGyro: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                sensorManager?.registerListener(TelemetryGyroListener, sensorGyro, SAMPLING)

                if (activity is BaseActivity) {
                    activity.addListener(TelemetryTouchListener)
                }
                // store this activity so it can be stopped later
                prevActivityRef = WeakReference(activity)
                Log.w("HENDRYTAG", "Telemetry registerTelemetryListener")
            } catch (e: Throwable) {
                Log.w("HENDRYTAG", "Error Telemetry registerTelemetryListener $e")
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        stopTelemetryListener(activity)
    }

    private fun stopTelemetryListener(activity: Activity) {
        try {
            Log.w("HENDRYTAG", "stopTelemetryListener " + activity::class.java.simpleName)
            val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
            sensorManager?.unregisterListener(TelemetryAccelListener)
            sensorManager?.unregisterListener(TelemetryGyroListener)
            if (activity is BaseActivity) {
                activity.removeDispatchTouchListener(TelemetryTouchListener)
            }
        } catch (e: Throwable) {
            Log.w("HENDRYTAG", "Error Telemetry stopTelemetryListener $e")
        }
    }
}