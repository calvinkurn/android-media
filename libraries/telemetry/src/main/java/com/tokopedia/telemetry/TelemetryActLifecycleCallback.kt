package com.tokopedia.telemetry

import android.app.Activity
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
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
import java.lang.Exception
import java.lang.ref.WeakReference

class TelemetryActLifecycleCallback(
    val isEnabled: (() -> (Boolean))
) : Application.ActivityLifecycleCallbacks {

    companion object {
        var prevActivityRef: WeakReference<AppCompatActivity>? = null
        const val SAMPLING_RATE_MICRO = 200_000 // 200ms or 0.2s
        const val SAMPLING_RATE_MS = 200 // 200ms or 0.2s
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
                TelemetryAccelListener.setActivity(activity)

                val sensorGyro: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                sensorManager?.registerListener(TelemetryGyroListener, sensorGyro, samplingRate)
                TelemetryGyroListener.setActivity(activity)

                if (activity is BaseActivity) {
                    activity.addListener(TelemetryTouchListener)
                }
                // store this activity so it can be stopped later
                prevActivityRef = WeakReference(activity)
            } catch (ignored: Throwable) {
            }
        }
    }

    private fun getSamplingRate(): Int {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            SAMPLING_RATE_MICRO
        } else {
            SENSOR_DELAY_NORMAL
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

        if (!isEnabled.invoke()) {
            return
        }

        try {
            if (activity is ITelemetryActivity) {
                collectTelemetryInTeleActivity(activity)
            } else { // this activity is not telemetry activity
                collectTelemetryInNonTeleActivity(activity)
            }
        } catch (ignored: Throwable) {
        }
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {
        stopTelemetryListener(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    private fun collectTelemetryInTeleActivity(activity: ITelemetryActivity) {
        if (activity !is AppCompatActivity) {
            return
        }
        val sectionName = activity.getTelemetrySectionName()

        // Only send telemetry if section Name is different
        if (sectionName != Telemetry.getCurrentSectionName()) {
            collectTelemetry(activity, sectionName)
        }
    }

    private fun collectTelemetryInNonTeleActivity(activity: Activity) {
        if (activity !is AppCompatActivity) {
            return
        }
        if (Telemetry.hasOpenTime()) {
            // check if it is already past section duration or not
            val elapsedDiff = Telemetry.getElapsedDiff()
            if (elapsedDiff < (SECTION_TELEMETRY_DURATION - STOP_THRES)) {
                registerTelemetryListener(activity)
                // timer to stop after telemetry duration
                activity.lifecycleScope.launch {
                    try {
                        val remainingDurr = SECTION_TELEMETRY_DURATION - elapsedDiff
                        delay(remainingDurr)
                        stopTelemetryListener(activity)
                        Telemetry.addStopTime()
                    } catch (ignored: Throwable) {
                    }
                }
            } else { // duration is due
                val estimatedDuration =
                    Telemetry.telemetrySectionList[0].startTime + SECTION_TELEMETRY_DURATION
                Telemetry.addStopTime("", estimatedDuration)
            }
        }
    }

    private fun collectTelemetry(activity: AppCompatActivity, sectionName: String) {
        // stop time for prev telemetry
        Telemetry.addStopTime(sectionName)
        TelemetryWorker.scheduleWorker(activity.applicationContext)

        Telemetry.addSection(sectionName)
        registerTelemetryListener(activity)

        // timer to stop after telemetry duration
        activity.lifecycleScope.launch {
            try {
                delay(SECTION_TELEMETRY_DURATION)
                stopTelemetryListener(activity)
                Telemetry.addStopTime()
            } catch (ignored: Throwable) {
            }
        }
    }

    private fun stopTelemetryListener(activity: Activity) {
        unregisterSensor(activity)
        unregisterWatcher(activity)
        unregisterTouch(activity)
    }

    private fun unregisterSensor(activity: Activity) {
        try {
            val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
            sensorManager?.unregisterListener(TelemetryAccelListener)
            sensorManager?.unregisterListener(TelemetryGyroListener)
        } catch (ignored: Exception) {
        }
    }

    private fun unregisterWatcher(activity: Activity) {
        try {
            activity.findViewById<View>(android.R.id.content)?.viewTreeObserver?.addOnGlobalFocusChangeListener { _, newFocus ->
                if (newFocus is EditText) {
                    newFocus.removeTextChangedListener(TelemetryTextWatcher)
                }
            }
        } catch (ignored: Exception) {
        }
    }

    private fun unregisterTouch(activity: Activity) {
        try {
            if (activity is BaseActivity) {
                activity.removeDispatchTouchListener(TelemetryTouchListener)
            }
        } catch (ignored: Exception) {
        }
    }
}
