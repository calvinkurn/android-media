package com.tkpd.macrobenchmark.util

import android.content.Intent
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

object MacroDevOps {
    const val LEWATI_BTN = "Lewati"
    fun setupEnvironment(intent: Intent) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.targetContext.startActivity(intent)
        Thread.sleep(5000)
        val device = UiDevice.getInstance(instrumentation)
        killProcess(device, MacroIntent.TKPD_PACKAGE_NAME)
    }

    fun skipOnboarding() {
        try {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            instrumentation.targetContext.startActivity(
                MacroIntent.Home.getHomeIntent().apply {
                    this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
            val device = UiDevice.getInstance(instrumentation)

            skipOnboardingPage()

            Thread.sleep(3000L)
            device.pressBack()

            Thread.sleep(300L)
            killProcess(device, MacroIntent.TKPD_PACKAGE_NAME)
        } catch (e: Exception) {
            //no-op
        }
    }

    fun skipOnboardingPage() {
        try {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            val device = UiDevice.getInstance(instrumentation)

            device.wait(
                Until.hasObject(By.text(LEWATI_BTN)),
                5000L
            )
            val btn = device.findObject(By.text(LEWATI_BTN));
            btn.click()

            Thread.sleep(3000L)

            //in case there is dialog/bottomsheet
            device.pressBack()
        } catch (e: Exception) {
            //no-op
        }
    }

    fun killProcess(device: UiDevice, packageName: String) {
        Log.d("TkpdMacroBenchmark", "Killing process $packageName")
        device.executeShellCommand("am force-stop $packageName")
    }
}
