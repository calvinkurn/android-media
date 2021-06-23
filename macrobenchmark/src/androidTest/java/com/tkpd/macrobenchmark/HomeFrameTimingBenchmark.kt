package com.tkpd.macrobenchmark

import android.content.Intent
import android.net.Uri
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import com.tokopedia.applink.ApplinkConst
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

    @Test
    fun start() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        /**
         * Read the docs
         * https://developer.android.com/studio/profile/macrobenchmark
         */
        benchmarkRule.measureRepeated(
            packageName = PACKAGE_NAME,
            metrics = listOf(FrameTimingMetric()),
            // Try switching to different compilation modes to see the effect
            // it has on frame timing metrics.
            compilationMode = CompilationMode.None,
            iterations = 2,
            setupBlock = {
                // Before starting to measure, navigate to the UI to be measured
                val intent = Intent()
                intent.setData(Uri.parse(ApplinkConst.HOME))
                intent.putExtra("testenv", "testenv")
                startActivityAndWait(intent)
            }
        ) {
            val recycler = device.findObject(By.res(PACKAGE_NAME, RESOURCE_ID))
            // Set gesture margin to avoid triggering gesture navigation
            // with input events from automation.
            recycler.setGestureMargin(device.displayWidth / 5)
            for (i in 1..2) {
                recycler.scroll(Direction.DOWN, 2f)
                device.waitForIdle()
            }
        }
    }

    companion object {
        /**
         * Target test package
         * In this test class, the target is :testapp with package com.tokopedia.tkpd
         */
        private const val PACKAGE_NAME = "com.tokopedia.tkpd"

        /**
         * Target test intent action
         * Target MainParentActivity activity in testapp
         * Detail in this manifest: testapp/src/main/AndroidManifest.xml
         */
        private const val ACTION = "com.tokopedia.navigation.presentation.activity.MainParentActivity"

        /**
         * Target recyclerview
         * Capture view by resource id
         */
        private const val RESOURCE_ID = "home_fragment_recycler_view"
    }
}
