package com.tkpd.macrobenchmark.test.shop_page

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Created by Rafli Syam
 * This test will measure ShopPageProductListFragment launch time with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class ShopPageProductTabStartupBenchmark(startupMode: StartupMode): BaseStartupBenchmark(startupMode) {
    override fun getIntent() = MacroIntent.ShopPage.getShopPageProductTabIntent()

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            MacroIntent.ShopPage.PACKAGE_NAME,
            MacroIntent.ShopPage.RV_PRODUCT_TAB_RESOURCE_ID
        )
    }

    override fun traceName() = "mp_shop_page_product"
}