package com.tkpd.macrobenchmark.test.product_detail_page

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Created by yovi.putra on 01/11/22"
 * Project name: android-tokopedia-core
 **/

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class ProductDetailStartupBenchmark(startupMode: StartupMode) : BaseStartupBenchmark(startupMode) {
    override fun setupMock() {
    }

    override fun setupEnvironment() {
    }

    override fun getIntent() = MacroIntent.ProductDetail.getStartupIntent()

    override fun traceName() = "product_detail_trace"

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            MacroIntent.TKPD_PACKAGE_NAME,
            MacroIntent.ProductDetail.RV_RESOURCE_ID
        )
    }
}
