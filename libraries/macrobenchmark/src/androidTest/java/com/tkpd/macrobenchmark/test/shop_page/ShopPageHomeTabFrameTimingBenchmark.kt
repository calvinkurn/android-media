package com.tkpd.macrobenchmark.test.shop_page

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith

/**
 * Created by Rafli Syam
 * This test will measure ShopPageHomeFragment janky frames with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class ShopPageHomeTabFrameTimingBenchmark: BaseFrameTimingBenchmark() {
    override fun setupEnvironment() {
    }

    override fun setupMock() {
    }


    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.basicRecyclerviewInteraction(
            MacroIntent.ShopPage.PACKAGE_NAME,
            MacroIntent.ShopPage.RV_HOME_TAB_RESOURCE_ID
        )
    }

    override fun getIntent() = MacroIntent.ShopPage.getShopPageHomeTabIntent()
}
