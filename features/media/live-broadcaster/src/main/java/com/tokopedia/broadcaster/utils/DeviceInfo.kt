package com.tokopedia.broadcaster.utils

import android.os.Build

object DeviceInfo {
    private const val ARMEABI_V7A = "armeabi-v7a"
    private const val ARM64_V8A = "arm64-v8a"

    private val supportedAbi = mutableListOf(ARMEABI_V7A, ARM64_V8A)

    private fun checkDeviceAbi(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportedAbi.contains(Build.SUPPORTED_ABIS[0])
        } else {
            supportedAbi.contains(Build.CPU_ABI)
        }
    }

    fun isDeviceSupported(): Boolean {
        return checkDeviceAbi()
    }
}