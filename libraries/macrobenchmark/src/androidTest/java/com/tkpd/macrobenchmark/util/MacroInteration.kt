package com.tkpd.macrobenchmark.util

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

object MacroInteration {
    private val DEFAULT_TIMEOUT = 60000L
    private val IDLE_DURATION = 2000L

    fun basicFlingInteraction(
        packageName: String,
        scrollableViewId: String,
        flingDirection: Direction = Direction.DOWN,
        flingSpeed: Int = 50
    ) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        device.wait(Until.hasObject(By.res(packageName, scrollableViewId)), DEFAULT_TIMEOUT)
        val recycler = device.findObject(By.res(packageName, scrollableViewId))

        // Set gesture margin to avoid triggering gesture navigation
        // with input events from automation.
        recycler.setGestureMargin(device.displayWidth / 5)
        for (i in 1..(MacroArgs.getRecyclerViewScrollIterations(InstrumentationRegistry.getArguments()))) {
            recycler.fling(flingDirection, flingSpeed)
            device.waitForIdle()
        }
    }

    fun basicRecyclerviewInteraction(
        packageName: String,
        rvResourceId: String,
        scrollDirection: Direction = Direction.DOWN,
        scrollPercent: Float = 2f
    ) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        device.wait(Until.hasObject(By.res(packageName, rvResourceId)), DEFAULT_TIMEOUT)
        val recycler = device.findObject(By.res(packageName, rvResourceId))

        // Set gesture margin to avoid triggering gesture navigation
        // with input events from automation.
        recycler.setGestureMargin(device.displayWidth / 5)
        for (i in 1..(MacroArgs.getRecyclerViewScrollIterations(InstrumentationRegistry.getArguments()))) {
            recycler.scroll(scrollDirection, scrollPercent)
            device.waitForIdle()
        }
    }

    fun waitUntilRecyclerViewExist(packageName: String, rvResourceId: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)
        device.wait(Until.hasObject(By.res(packageName, rvResourceId)), DEFAULT_TIMEOUT)
        device.waitForIdle(IDLE_DURATION)
    }

    fun waitForRecyclerViewContent(packageName: String, rvResourceId: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        device.wait(Until.hasObject(By.res(packageName, rvResourceId)), DEFAULT_TIMEOUT)

        val recycler = device
            .findObject(By.res(packageName, rvResourceId))

        recycler.wait(Until.scrollable(true), DEFAULT_TIMEOUT)
        device.waitForIdle(IDLE_DURATION)
    }

    fun waitForComposableWidgetVisible(widgetContentDescription: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)
        device.wait(Until.hasObject(By.descContains(widgetContentDescription)), DEFAULT_TIMEOUT)
        device.waitForIdle(IDLE_DURATION)
    }

    fun basicComposableInteraction(
        contentDescription: String,
        scrollDirection: Direction = Direction.DOWN,
        scrollPercent: Float = 2f
    ) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        device.wait(Until.hasObject(By.desc(contentDescription)), DEFAULT_TIMEOUT)
        val recycler = device.findObject(By.desc(contentDescription))

        // Set gesture margin to avoid triggering gesture navigation
        // with input events from automation.
        recycler.setGestureMargin(device.displayWidth / 5)
        for (i in 1..(MacroArgs.getRecyclerViewScrollIterations(InstrumentationRegistry.getArguments()))) {
            recycler.scroll(scrollDirection, scrollPercent)
            device.waitForIdle()
        }
    }
}
