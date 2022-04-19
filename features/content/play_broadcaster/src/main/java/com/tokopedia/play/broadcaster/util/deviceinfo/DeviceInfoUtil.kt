package com.tokopedia.play.broadcaster.util.deviceinfo

import android.os.Build


/**
 * Created by mzennis on 26/05/20.
 */
object DeviceInfoUtil {

    private const val ARMEABI_V7A = "armeabi-v7a"
    private const val ARM64_V8A = "arm64-v8a"

    private val supportedAbi = arrayOf(ARMEABI_V7A, ARM64_V8A)

    private fun checkDeviceAbi(): Boolean {
        return supportedAbi.contains(Build.SUPPORTED_ABIS[0])
    }

    fun isDeviceSupported(): Boolean {
        return checkDeviceAbi()
    }
}