package com.tkpd.macrobenchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
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
class HomeStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureStartup(
        profileCompiled = false,
        startupMode = StartupMode.COLD,
        iterations = 3
    ) {
        action = ACTION
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

const val TARGET_PACKAGE = "com.tokopedia.tkpd"

fun MacrobenchmarkRule.measureStartup(
    profileCompiled: Boolean,
    startupMode: StartupMode,
    iterations: Int = 3,
    setupIntent: Intent.() -> Unit = {}
) = measureRepeated(
    packageName = TARGET_PACKAGE,
    metrics = listOf(StartupTimingMetric()),
    compilationMode = if (profileCompiled) {
        CompilationMode.SpeedProfile(warmupIterations = 3)
    } else {
        CompilationMode.None
    },
    iterations = iterations,
    startupMode = startupMode
) {
    pressHome()
    val intent = Intent()
    intent.setPackage(TARGET_PACKAGE)
    setupIntent(intent)
    startActivityAndWait(intent)
}
