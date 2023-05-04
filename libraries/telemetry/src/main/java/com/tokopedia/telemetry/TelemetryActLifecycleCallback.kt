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
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.telemetry.model.TELEMETRY_REMOTE_CONFIG_KEY
import com.tokopedia.telemetry.model.Telemetry
import com.tokopedia.telemetry.model.TelemetryConfig
import com.tokopedia.telemetry.model.TelemetryConfig.Companion.isInSamplingArea
import com.tokopedia.telemetry.network.TelemetryWorker
import com.tokopedia.telemetry.sensorlistener.TelemetryAccelListener
import com.tokopedia.telemetry.sensorlistener.TelemetryGyroListener
import com.tokopedia.telemetry.sensorlistener.TelemetryTextWatcher
import com.tokopedia.telemetry.sensorlistener.TelemetryTouchListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.lang.ref.WeakReference

enum class TelemetryType {
    TYPING, TOUCH, GYRO, ACCEL
}

data class CapturedTelemetry(
    var capturedTime: Int,
    var count: Int
)

class TelemetryActLifecycleCallback(
    val isEnabled: (() -> (Boolean))
) : Application.ActivityLifecycleCallbacks {

    companion object {
        var prevActivityRef: WeakReference<AppCompatActivity>? = null
        const val SAMPLING_RATE_MICRO = 200_000 // 200ms or 0.2s
        const val SAMPLING_RATE_MS = 200 // 200ms or 0.2s
        var remoteConfig: RemoteConfig? = null
        var telemetryConfig: TelemetryConfig? = null
        var hasFetch = false
        val mapSectionToCount = mutableMapOf<Pair<String, TelemetryType>, CapturedTelemetry>()
    }

    private fun getRemoteConfig(context: Context): RemoteConfig {
        val rc = remoteConfig
        return if (rc == null) {
            val tempRc = FirebaseRemoteConfigImpl(context.applicationContext)
            remoteConfig = tempRc
            tempRc
        } else {
            rc
        }
    }

    private fun registerTelemetryListener(activity: AppCompatActivity) {
        activity.lifecycleScope.launch {
            try {
                yield()
                if (activity.isDestroyed || activity.isFinishing) {
                    return@launch
                }
                val telConfig = fetchConfig(activity)
                if (checkNeedCollect(activity, telConfig, TelemetryType.TYPING)) {
                    activity.findViewById<View>(android.R.id.content)?.viewTreeObserver?.addOnGlobalFocusChangeListener { _, newFocus ->
                        if (newFocus is EditText) {
                            val et: EditText = newFocus
                            et.removeTextChangedListener(TelemetryTextWatcher)
                            et.addTextChangedListener(TelemetryTextWatcher)
                        }
                    }
                }
                var sensorManager: SensorManager? = null
                if (checkNeedCollect(activity, telConfig, TelemetryType.ACCEL)) {
                    sensorManager =
                        activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
                    val sensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

                    val samplingRate = getSamplingRate()
                    sensorManager?.registerListener(TelemetryAccelListener, sensor, samplingRate)
                    TelemetryAccelListener.setActivity(activity)
                }

                if (checkNeedCollect(activity, telConfig, TelemetryType.GYRO)) {
                    if (sensorManager == null) {
                        sensorManager =
                            activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
                    }
                    val sensorGyro: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                    val samplingRate = getSamplingRate()
                    sensorManager?.registerListener(TelemetryGyroListener, sensorGyro, samplingRate)
                    TelemetryGyroListener.setActivity(activity)
                }

                if (checkNeedCollect(activity, telConfig, TelemetryType.TOUCH)) {
                    if (activity is BaseActivity) {
                        activity.addListener(TelemetryTouchListener)
                    }
                }
                // store this activity so it can be stopped later
                prevActivityRef = WeakReference(activity)
            } catch (ignored: Throwable) {
            }
        }
    }

    private fun checkNeedCollect(
        activity: Activity,
        telemetryConfig: TelemetryConfig,
        teleType: TelemetryType
    ): Boolean {
        if (activity !is ITelemetryActivity) return true
        val sectionName = activity.getTelemetrySectionName()
        val configCount = when (teleType) {
            TelemetryType.TYPING -> telemetryConfig.typeCount
            TelemetryType.ACCEL -> telemetryConfig.accelCount
            TelemetryType.GYRO -> telemetryConfig.gyroCount
            TelemetryType.TOUCH -> telemetryConfig.touchCount
        }
        val configInterval = when (teleType) {
            TelemetryType.TYPING -> telemetryConfig.typeInterval
            TelemetryType.ACCEL -> telemetryConfig.accelInterval
            TelemetryType.GYRO -> telemetryConfig.gyroInterval
            TelemetryType.TOUCH -> telemetryConfig.touchInterval
        }
        if (configCount >= 0 && configInterval >= 20) {
            // do checking count and interval
            var telemetryInPage = mapSectionToCount.getOrDefault(
                sectionName to teleType,
                CapturedTelemetry(0, 0)
            )
            val now: Int = (System.currentTimeMillis() / 1000L).toInt()
            if (now - telemetryInPage.capturedTime > configInterval) {
                //reset
                telemetryInPage = CapturedTelemetry(0, 0)
            }
            if (telemetryInPage.count < configCount) {
                // update the map
                val capturedTime = if (telemetryInPage.capturedTime == 0) {
                    now
                } else {
                    telemetryInPage.capturedTime
                }
                val newCount = telemetryInPage.count + 1
                mapSectionToCount[sectionName to teleType] =
                    CapturedTelemetry(capturedTime, newCount)
                return true
            }
        }
        val configRate = when (teleType) {
            TelemetryType.TYPING -> telemetryConfig.typeSamplingInt
            TelemetryType.ACCEL -> telemetryConfig.accelSamplingInt
            TelemetryType.GYRO -> telemetryConfig.gyroSamplingInt
            TelemetryType.TOUCH -> telemetryConfig.touchSamplingInt
        }
        return configRate.isInSamplingArea()
    }

    private fun fetchConfig(context: Context): TelemetryConfig {
        val obj: TelemetryConfig
        if (telemetryConfig == null || !hasFetch) {
            val remoteConfig = getRemoteConfig(context)
            val telemetryConfigString = remoteConfig.getString(TELEMETRY_REMOTE_CONFIG_KEY, "")
            if (telemetryConfigString.isNullOrEmpty()) {
                obj = TelemetryConfig()
                telemetryConfig = obj
                return obj
            } else {
                obj = TelemetryConfig.parseFromRemoteConfig(telemetryConfigString)
                telemetryConfig = obj
                hasFetch = true
                return obj
            }
        } else {
            return telemetryConfig ?: TelemetryConfig()
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
