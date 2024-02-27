package com.tkpd.macrobenchmark.base

import android.content.Intent
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.tkpd.macrobenchmark.util.MacroArgs
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroMetrics
import com.tkpd.macrobenchmark.util.measureTokopediaApps
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
abstract class BaseStartupBenchmark(
    private val startupMode: StartupMode,
    private val applicationPackage: String = MacroIntent.TKPD_PACKAGE_NAME
) {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Before
    fun setupBefore() {
        if (MacroArgs.useMock(InstrumentationRegistry.getArguments())) {
            setupMock()
        }
    }

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun macrobenchmarkLaunchTime() {
        benchmarkRule.measureTokopediaApps(
            startupMode = startupMode,
            packageName = packageName(),
            setupBlock = {
                setupEnvironment()
            },
            metrics = listOf(
                StartupTimingMetric()
            ).plus(MacroMetrics.getPltMetrics(traceName()))
        ) {
            it.startActivityAndWait(getIntent())
            waitUntil()
        }
    }

    abstract fun setupMock()

    abstract fun setupEnvironment()

    abstract fun getIntent(): Intent

    abstract fun waitUntil()

    abstract fun traceName(): String

    open fun packageName(): String = applicationPackage

    companion object {
        @Parameterized.Parameters(name = "mode={0}")
        @JvmStatic
        fun parameters(): List<Array<Any>> {
            return listOf(StartupMode.COLD)
                .map { arrayOf(it) }
        }
    }
}
