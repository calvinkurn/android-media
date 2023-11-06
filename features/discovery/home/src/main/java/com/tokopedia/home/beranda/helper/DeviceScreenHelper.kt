package com.tokopedia.home.beranda.helper

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.device.info.DeviceScreenInfo
import javax.inject.Inject

class DeviceScreenHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun isFoldableOrTablet(): Boolean {
        val isTablet = DeviceScreenInfo.isTablet(context)
        val isFoldable = isFoldable()
        return isFoldable || isTablet
    }

    fun isFoldable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_HINGE_ANGLE)
        } else {
            false
        }
    }
}
