package com.tkpd.macrobenchmark.util

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice

object MacroDevOps {
    fun setupHomeEnvironment() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.targetContext.startActivity(MacroIntent.Home.getHomeMacroSetupIntent())
        Thread.sleep(5000)
        val device = UiDevice.getInstance(instrumentation)
        killProcess(device, MacroIntent.TKPD_PACKAGE_NAME)
    }

    private fun killProcess(device: UiDevice, packageName: String) {
        Log.d("Devara", "Killing process $packageName")
        device.executeShellCommand("am force-stop $packageName")
    }
}