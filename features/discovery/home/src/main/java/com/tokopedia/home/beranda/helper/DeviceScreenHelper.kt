package com.tokopedia.home.beranda.helper

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.device.info.DeviceScreenInfo
import javax.inject.Inject

class DeviceScreenHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun isFoldableOrTablet(): Boolean {
        val isTablet = DeviceScreenInfo.isTablet(context)
        val isFoldable = DeviceScreenInfo.isFoldable(context)
        val isEmulator = DeviceInfo.isEmulated()
        return isTablet || (!isEmulator && isFoldable)
    }
}
