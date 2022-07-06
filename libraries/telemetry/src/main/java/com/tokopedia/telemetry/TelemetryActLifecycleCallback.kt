package com.tokopedia.telemetry

import android.app.Activity
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.hardware.SensorManager.SENSOR_DELAY_UI
import android.os.Build
import android.os.Bundle
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
import kotlinx.coroutines.yield
import java.lang.ref.WeakReference

class TelemetryActLifecycleCallback : Application.ActivityLifecycleCallbacks {

    companion object {
        var prevActivityRef: WeakReference<AppCompatActivity>? = null
        const val SAMPLING_RATE_MICRO = 85_000 // 85ms or 0.085s
        const val SAMPLING_RATE_MS = 85 // 85ms or 0.085s
    }

    private fun registerTelemetryListener(activity: AppCompatActivity) {
        activity.lifecycleScope.launch {
            try {
                yield()
                activity.findViewById<View>(android.R.id.content)?.viewTreeObserver?.addOnGlobalFocusChangeListener { _, newFocus ->
                    if (newFocus is EditText) {
                        val et: EditText = newFocus
                        et.removeTextChangedListener(TelemetryTextWatcher)
                        et.addTextChangedListener(TelemetryTextWatcher)
                    }
                }
                val sensorManager: SensorManager? =
                    activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
                val sensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

                val samplingRate = getSamplingRate()
                sensorManager?.registerListener(TelemetryAccelListener, sensor, samplingRate)

                val sensorGyro: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                sensorManager?.registerListener(TelemetryGyroListener, sensorGyro, samplingRate)

                if (activity is BaseActivity) {
                    activity.addListener(TelemetryTouchListener)
                }
                // store this activity so it can be stopped later
                prevActivityRef = WeakReference(activity)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun getSamplingRate(): Int {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            SAMPLING_RATE_MICRO
        } else {
            SENSOR_DELAY_UI
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        // stop telemetry collection for previous activity to prevent listener leaking
        val prevActRef = prevActivityRef
        if (prevActRef != null) {
            val prevAct = prevActRef.get()
            if (prevAct != null) {
                stopTelemetryListener(prevAct)
                prevActivityRef = null
            }
        }

        // start new telemetry section
        if (activity !is AppCompatActivity) {
            return
        }
        if (activity is ITelemetryActivity) {
            val sectionName = activity.getTelemetrySectionName()

            // Only send telemetry if section Name is different
            if (sectionName == Telemetry.getCurrentSectionName()) {
                return
            }
            //stop time for prev telemetry
            Telemetry.addStopTime(sectionName)
            TelemetryWorker.scheduleWorker(activity.applicationContext)

            Telemetry.addSection(sectionName)
            registerTelemetryListener(activity)

            // timer to stop after telemetry duration
            activity.lifecycleScope.launch {
                delay(SECTION_TELEMETRY_DURATION)
                stopTelemetryListener(activity)
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
                        Telemetry.addStopTime()
                    }
                } else { // duration is due
                    val estimatedDuration =
                        Telemetry.telemetrySectionList[0].startTime + SECTION_TELEMETRY_DURATION
                    Telemetry.addStopTime("", estimatedDuration)
                }
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {
        stopTelemetryListener(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    private fun stopTelemetryListener(activity: Activity) {
        try {
            val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
            sensorManager?.unregisterListener(TelemetryAccelListener)
            sensorManager?.unregisterListener(TelemetryGyroListener)

            activity.findViewById<View>(android.R.id.content)?.viewTreeObserver?.addOnGlobalFocusChangeListener { _, newFocus ->
                if (newFocus is EditText) {
                    newFocus.removeTextChangedListener(TelemetryTextWatcher)
                }
            }

            if (activity is BaseActivity) {
                activity.removeDispatchTouchListener(TelemetryTouchListener)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}