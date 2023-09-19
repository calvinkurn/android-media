package com.tkpd.macrobenchmark.test.tokopedianow

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith

/**
 * Created by Reza
 * This test will measure TokoNowHomeActivity janky frames with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class TokoNowHomeFrameTimingBenchmark : BaseFrameTimingBenchmark() {
    override fun setupEnvironment() {
    }

    override fun setupMock() {
    }

    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.basicFlingInteraction(
            MacroIntent.SearchResult.PACKAGE_NAME,
            MacroIntent.SearchResult.RV_RESOURCE_ID,
            flingSpeed = 3000
        )
    }

    override fun getIntent() = MacroIntent.TokopediaNow.getHomeIntent()
}
