package com.tokopedia.product.addedit.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.utils.view.TabletModeUtil.getAccelerometerRotationStatus
import com.tokopedia.utils.view.TabletModeUtil.setOrientationToFullSensor
import com.tokopedia.utils.view.TabletModeUtil.setOrientationToPortrait

open class TabletAdaptiveActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setupOrientation()
        super.onCreate(savedInstanceState)
    }

    private fun setupOrientation() {
        val isTablet = DeviceScreenInfo.isTablet(this)
        val rotationEnabled = getAccelerometerRotationStatus(contentResolver)
        if (isTablet && rotationEnabled) {
            setOrientationToFullSensor()
        } else {
            setOrientationToPortrait()
        }
    }
}