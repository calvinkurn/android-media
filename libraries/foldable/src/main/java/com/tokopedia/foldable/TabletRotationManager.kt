package com.tokopedia.foldable

import android.content.pm.ActivityInfo
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.utils.accelerometer.orientation.AccelerometerOrientationListener

class TabletRotationManager(private val activity: AppCompatActivity) : LifecycleObserver {

    init {
        activity.lifecycle.addObserver(this)
    }

    private val accelerometerOrientationListener: AccelerometerOrientationListener by lazy {
        AccelerometerOrientationListener(activity.contentResolver) {
            onAccelerometerOrientationSettingChange(it)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        if (DeviceScreenInfo.isTablet(activity)) {
            val isAccelerometerRotationEnabled = Settings.System.getInt(
                activity.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            ) == 1
            activity.requestedOrientation =
                if (isAccelerometerRotationEnabled)
                    ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
        if (DeviceScreenInfo.isTablet(activity)) {
            accelerometerOrientationListener.register()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
        if (DeviceScreenInfo.isTablet(activity)) {
            accelerometerOrientationListener.unregister()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {

    }

    private fun onAccelerometerOrientationSettingChange(isEnabled: Boolean) {
        if (DeviceScreenInfo.isTablet(activity)) {
            activity.requestedOrientation =
                if (isEnabled) ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}