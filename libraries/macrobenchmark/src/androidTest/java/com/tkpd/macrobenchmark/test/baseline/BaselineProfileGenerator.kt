package com.tkpd.macrobenchmark.test.baseline

import android.content.Intent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.tkpd.macrobenchmark.util.MacroArgs
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun appStartupAndUserJourney() {
        rule.collectBaselineProfile(MacroArgs.TKPD_PACKAGE_NAME) {
            startApplicationJourney()
            scrollRecyclerViewJourney()
            goToDetailJourney()
        }
    }

    fun MacrobenchmarkScope.startApplicationJourney() {
        pressHome()
        startActivityAndWait()
        MacroDevOps.skipOnboardingPage()
        Thread.sleep(4_000)

        device.wait(Until.hasObject(By.res(MacroArgs.TKPD_PACKAGE_NAME, MacroIntent.Home.RV_RESOURCE_ID)), MacroInteration.DEFAULT_TIMEOUT)
    }

    fun MacrobenchmarkScope.scrollRecyclerViewJourney() {
        val recyclerViewHome = device.findObject(By.res(MacroArgs.TKPD_PACKAGE_NAME, MacroIntent.Home.RV_RESOURCE_ID))
        recyclerViewHome.setGestureMargin(device.displayWidth / 5)
        recyclerViewHome.fling(Direction.DOWN)
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.goToDetailJourney() {
        val recyclerViewHome = device.findObject(By.res(MacroIntent.TKPD_PACKAGE_NAME, MacroIntent.Home.RV_RESOURCE_ID))
        repeat(1) { index ->
            if (recyclerViewHome != null) {
                val author = recyclerViewHome.children[index % (recyclerViewHome.childCount - 1)]
                author.click()
                device.wait(Until.gone(By.res(MacroIntent.Home.RV_RESOURCE_ID, MacroIntent.Home.RV_RESOURCE_ID)), 5_000)
                device.waitForIdle()
            }
        }
    }
}
