package com.tkpd.macrobenchmark.util

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*

object MacroInteration {
    private val DEFAULT_TIMEOUT = 60000L
    private val IDLE_DURATION = 2000L

    fun basicRecyclerviewInteraction(packageName: String, rvResourceId: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        waitUntilRecyclerViewExist(packageName, rvResourceId)
        waitForRecyclerViewContent(packageName, rvResourceId)
        val recycler = device.findObject(By.res(packageName, rvResourceId))

        // Set gesture margin to avoid triggering gesture navigation
        // with input events from automation.
        recycler.setGestureMargin(device.displayWidth / 5)
        for (i in 1..(MacroArgs.getRecyclerViewScrollIterations(InstrumentationRegistry.getArguments()))) {
            recycler.scroll(Direction.DOWN, 2f)
            device.waitForIdle()
        }
    }

    fun waitUntilRecyclerViewExist(packageName: String, rvResourceId: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)
        device.wait(Until.findObject(By.res(packageName, rvResourceId)), DEFAULT_TIMEOUT)
        device.waitForIdle(IDLE_DURATION)
    }

    fun waitForRecyclerViewContent(packageName: String, rvResourceId: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        val recycler = device.findObject(By.res(packageName, rvResourceId))

        recycler.wait(Until.scrollable(true), DEFAULT_TIMEOUT)
        device.waitForIdle(IDLE_DURATION)
    }
}