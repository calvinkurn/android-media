package com.tkpd.macrobenchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
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
        startupMode = MacroArgs.getStartupMode(InstrumentationRegistry.getArguments()),
        iterations = MacroArgs.getIterations(InstrumentationRegistry.getArguments())) {
            MacroIntent.getHomeIntent()
    }

    companion object {
        /**
         * Target test intent action
         * Target MainParentActivity activity in testapp
         * Detail in this manifest: testapp/src/main/AndroidManifest.xml
         */
        private const val ACTION = "com.tokopedia.navigation.presentation.activity.MainParentActivity"
    }
}

const val TARGET_PACKAGE = "com.tokopedia.tkpd"

fun MacrobenchmarkRule.measureStartup(
    profileCompiled: Boolean,
    startupMode: StartupMode,
    iterations: Int = 3,
    intent: () -> Intent
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
    startActivityAndWait(intent.invoke())
}
