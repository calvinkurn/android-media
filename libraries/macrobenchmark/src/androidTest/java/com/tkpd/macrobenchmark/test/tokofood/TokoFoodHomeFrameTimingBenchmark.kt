package com.tkpd.macrobenchmark.test.tokofood

/**
 * Created by RizqiAryansa
 * This test will measure TokoFoodHomeFragment janky frames with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class TokoFoodHomeFrameTimingBenchmark: BaseFrameTimingBenchmark() {
    override fun setupEnvironment() {
    }

    override fun setupMock() {
    }

    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.basicRecyclerviewInteraction(
            MacroIntent.TokoFood.PACKAGE_NAME,
            MacroIntent.TokoFood.RV_RESOURCE_ID
        )
    }

    override fun getIntent() = MacroIntent.TokoFood.getHomeIntent()
}
