package com.tokopedia.broadcaster.utils

import android.os.Build

interface BuildVersionProvider {
    fun isLollipopOrAbove(): Boolean
    fun supportedAbis(): String
    fun cpuAbi(): String
}

class BuildVersionProviderImpl : BuildVersionProvider {

    override fun isLollipopOrAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    override fun supportedAbis(): String = Build.SUPPORTED_ABIS[0]

    override fun cpuAbi() = Build.CPU_ABI

}

object DeviceInfo {
    private const val ARMEABI_V7A = "armeabi-v7a"
    private const val ARM64_V8A = "arm64-v8a"

    private val supportedAbi = mutableListOf(ARMEABI_V7A, ARM64_V8A)

    private fun checkDeviceAbi(provider: BuildVersionProvider): Boolean {
        return if (provider.isLollipopOrAbove()) {
            supportedAbi.contains(provider.supportedAbis())
        } else {
            supportedAbi.contains(provider.cpuAbi())
        }
    }

    fun isDeviceSupported(
        provider: BuildVersionProvider = BuildVersionProviderImpl()
    ): Boolean {
        return checkDeviceAbi(provider)
    }
}