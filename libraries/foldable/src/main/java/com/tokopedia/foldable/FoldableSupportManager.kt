package com.tokopedia.foldable

import android.app.Activity
import android.content.pm.ActivityInfo
import android.provider.Settings
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.window.WindowInfoRepo
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.utils.accelerometer.orientation.AccelerometerOrientationListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FoldableSupportManager(val windowInfoRepo: WindowInfoRepo, val callback: Callback,val activity: Activity) : LifecycleObserver {

    private var layoutUpdatesJob: Job? = null

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
        layoutUpdatesJob = CoroutineScope(Dispatchers.Main).launch {
            windowInfoRepo.windowLayoutInfo()
                .collect { newLayoutInfo ->
                    callback.onChangeLayout(FoldableInfo(newLayoutInfo))
                }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        layoutUpdatesJob?.cancel()
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

    interface Callback {
        fun onChangeLayout(foldableInfo: FoldableInfo)
    }
}