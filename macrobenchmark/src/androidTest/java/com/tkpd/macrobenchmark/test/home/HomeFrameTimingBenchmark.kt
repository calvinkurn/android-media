package com.tkpd.macrobenchmark.test.home
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import com.tkpd.macrobenchmark.util.MacroIntent.Home.RV_RESOURCE_ID
import com.tkpd.macrobenchmark.util.MacroArgs
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by DevAra
 * This test will measure MainParentActivity janky frames with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class HomeFrameTimingBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Before
    fun setupEnvironment() {
        MacroDevOps.setupHomeEnvironment()
    }

    @Test
    fun macroBenchmarkHomeFps() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

//        /**
//         * Read the docs
//         * https://developer.android.com/studio/profile/macrobenchmark
//         */
        benchmarkRule.measureRepeated(
            packageName = MacroIntent.TKPD_PACKAGE_NAME,
            metrics = listOf(FrameTimingMetric()),
            // Try switching to different compilation modes to see the effect
            // it has on frame timing metrics.
            compilationMode = MacroArgs.getCompilationMode(InstrumentationRegistry.getArguments()),
            iterations = MacroArgs.getIterations(InstrumentationRegistry.getArguments()),
            setupBlock = {
                val intent = MacroIntent.Home.getHomeIntent()
                startActivityAndWait(intent)
            }
        ) {
            val recycler = device.findObject(By.res(MacroIntent.TKPD_PACKAGE_NAME, RV_RESOURCE_ID))
            // Set gesture margin to avoid triggering gesture navigation
            // with input events from automation.
            recycler.setGestureMargin(device.displayWidth / 5)
            for (i in 1..(MacroArgs.getRecyclerViewScrollIterations(InstrumentationRegistry.getArguments()))) {
                recycler.scroll(Direction.DOWN, 2f)
                device.waitForIdle()
            }
        }
    }
}
