package com.tkpd.macrobenchmark.base
import android.content.Intent
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.tkpd.macrobenchmark.util.MacroArgs
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
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

    @Test
    fun macroBenchmarkFps() {
        /**
         * Read the docs
         * https://developer.android.com/studio/profile/macrobenchmark
         */
        var currentIteration = 0
        benchmarkRule.measureRepeated(
            packageName = MacroIntent.TKPD_PACKAGE_NAME,
            metrics = listOf(FrameTimingMetric()),
            // Try switching to different compilation modes to see the effect
            // it has on frame timing metrics.
            compilationMode = MacroArgs.getCompilationMode(InstrumentationRegistry.getArguments()),
            iterations = MacroArgs.getIterations(InstrumentationRegistry.getArguments()),
            setupBlock = {
                val intent = getIntent()
                startActivityAndWait(intent)
            }
        ) {
            pageInteractionTest(currentIteration)
            currentIteration++
        }
    }
    abstract fun pageInteractionTest(currentIteration: Int)
    abstract fun getIntent(): Intent
}
