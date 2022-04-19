package com.tkpd.macrobenchmark.util

import android.content.Intent
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice

object MacroDevOps {
    fun setupEnvironment(intent: Intent) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.targetContext.startActivity(intent)
        Thread.sleep(10000)
        val device = UiDevice.getInstance(instrumentation)
        killProcess(device, MacroIntent.TKPD_PACKAGE_NAME)
    }

    private fun killProcess(device: UiDevice, packageName: String) {
        Log.d("TkpdMacroBenchmark", "Killing process $packageName")
        device.executeShellCommand("am force-stop $packageName")
    }
}