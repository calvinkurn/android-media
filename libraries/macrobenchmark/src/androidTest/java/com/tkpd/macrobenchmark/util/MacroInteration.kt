package com.tkpd.macrobenchmark.util

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice

object MacroInteration {
    fun basicRecyclerviewInteraction(packageName: String, rvResourceId: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        val recycler = device.findObject(By.res(packageName, rvResourceId))
        // Set gesture margin to avoid triggering gesture navigation
        // with input events from automation.
        recycler.setGestureMargin(device.displayWidth / 5)
        for (i in 1..(MacroArgs.getRecyclerViewScrollIterations(InstrumentationRegistry.getArguments()))) {
            recycler.scroll(Direction.DOWN, 2f)
            device.waitForIdle()
        }
    }
}