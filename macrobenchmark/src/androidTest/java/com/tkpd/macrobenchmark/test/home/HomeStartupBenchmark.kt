package com.tkpd.macrobenchmark.test.home

import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.tkpd.macrobenchmark.util.MacroArgs
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.measureStartup
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
class HomeStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Before
    fun setupEnvironment() {
        MacroDevOps.setupHomeEnvironment()
    }

    @Test
    fun macroBenchmarkHomeLaunchTime() {
        benchmarkRule.measureStartup(
            profileCompiled = false,
            startupMode = MacroArgs.getStartupMode(InstrumentationRegistry.getArguments()),
            iterations = MacroArgs.getIterations(InstrumentationRegistry.getArguments())) {
            MacroIntent.Home.getHomeIntent()
        }
    }
}
