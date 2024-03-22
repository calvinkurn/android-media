package com.tkpd.macrobenchmark.base
import android.content.Intent
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.tkpd.macrobenchmark.util.MacroArgs
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.measureTokopediaApps
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by DevAra
 * Base class for frame timing macrobenchmark in Tokopedia
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
abstract class BaseFrameTimingBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Before
    fun setupBefore() {
        if (MacroArgs.useMock(InstrumentationRegistry.getArguments())) {
            setupMock()
        }
    }

    abstract fun setupMock()

    abstract fun setupEnvironment()

    open fun packageName(): String = MacroIntent.TKPD_PACKAGE_NAME

    open fun customTrace(): List<String> = emptyList()

    @Test
    fun macroBenchmarkFps() {
        /**
         * Read the docs
         * https://developer.android.com/studio/profile/macrobenchmark
         */
        var currentIteration = 0
        val customTraceMetrics = customTrace().map { TraceSectionMetric(it) }
        val metrics = listOf(FrameTimingMetric()).plus(customTraceMetrics)
        benchmarkRule.measureTokopediaApps(
            metrics = metrics,
            packageName = packageName(),
            startupMode = StartupMode.COLD,
            setupBlock = {
                setupEnvironment()
            }
        ) {
            val intent = getIntent()
            it.startActivityAndWait(intent)

            pageInteractionTest(currentIteration)
            currentIteration++
        }
    }
    abstract fun pageInteractionTest(currentIteration: Int)
    abstract fun getIntent(): Intent
}
