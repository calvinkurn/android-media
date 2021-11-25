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
class TokoNowHomeFrameTimingBenchmark: BaseFrameTimingBenchmark() {
    override fun pageInteractionTest() {
        MacroInteration.basicRecyclerviewInteraction(
            MacroIntent.TokopediaNow.PACKAGE_NAME,
            MacroIntent.TokopediaNow.RV_RESOURCE_ID
        )
    }

    override fun getIntent() = MacroIntent.TokopediaNow.getHomeIntent()
}
