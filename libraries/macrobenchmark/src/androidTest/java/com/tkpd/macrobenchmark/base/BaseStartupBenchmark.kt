package com.tkpd.macrobenchmark.base

import android.content.Intent
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
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
import org.junit.runners.Parameterized

/**
 * Created by DevAra
 * Base class for startup macrobenchmark in Tokopedia
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
abstract class BaseStartupBenchmark(private val startupMode: StartupMode) {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun macrobenchmarkLaunchTime() {
        benchmarkRule.measureStartup(
                startupMode = startupMode,
                iterations = MacroArgs.getIterations(InstrumentationRegistry.getArguments()),
                intent = { getIntent() },
                waitUntil = { waitUntil() })
    }

    abstract fun getIntent(): Intent

    abstract fun waitUntil()

    companion object {
        @Parameterized.Parameters(name = "mode={0}")
        @JvmStatic
        fun parameters(): List<Array<Any>> {
            return listOf(StartupMode.COLD, StartupMode.WARM, StartupMode.HOT)
                .map { arrayOf(it) }
        }
    }
}
