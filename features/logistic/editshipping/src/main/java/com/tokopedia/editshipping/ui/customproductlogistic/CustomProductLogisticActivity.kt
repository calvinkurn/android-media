package com.tokopedia.editshipping.ui.customproductlogistic

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.utils.accelerometer.orientation.AccelerometerOrientationListener

class CustomProductLogisticActivity : BaseSimpleActivity() {

    private val accelerometerOrientationListener: AccelerometerOrientationListener by lazy {
        AccelerometerOrientationListener(contentResolver) {
            onAccelerometerOrientationSettingChange(it)
        }
    }

    override fun getNewFragment(): Fragment? {
        var fragment: CustomProductLogisticFragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = CustomProductLogisticFragment.newInstance(bundle ?: Bundle())
        }
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setActivityOrientation()
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (DeviceScreenInfo.isTablet(this)) {
            accelerometerOrientationListener.register()
        }
    }

    override fun onPause() {
        super.onPause()
        if (DeviceScreenInfo.isTablet(this)) {
            accelerometerOrientationListener.unregister()
        }
    }

    private fun onAccelerometerOrientationSettingChange(isEnabled: Boolean) {
        if (DeviceScreenInfo.isTablet(this)) {
            requestedOrientation =
                    if (isEnabled) ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun setActivityOrientation() {
        if (DeviceScreenInfo.isTablet(this)) {
            val isAccelerometerRotationEnabled = Settings.System.getInt(
                    contentResolver,
                    Settings.System.ACCELEROMETER_ROTATION,
                    ROTATION_DEFAULT_VALUE
            ) == USE_ROTATION_VALUE
            requestedOrientation =
                    if (isAccelerometerRotationEnabled) ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    companion object {
        private const val ROTATION_DEFAULT_VALUE = 0
        private const val USE_ROTATION_VALUE = 1
    }
}