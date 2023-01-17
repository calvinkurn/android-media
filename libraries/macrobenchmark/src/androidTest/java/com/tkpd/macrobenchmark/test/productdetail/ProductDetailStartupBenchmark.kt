package com.tkpd.macrobenchmark.test.productdetail

import androidx.benchmark.macro.StartupMode
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseStartupBenchmark
import com.tkpd.macrobenchmark.util.MacroDevOps
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
        MacroDevOps.setupEnvironment(MacroIntent.Mock.getProductDetailMockIntent())
    }

    override fun setupEnvironment() {
    }

    override fun getIntent() = MacroIntent.ProductDetail.getIntent()

    override fun traceName() = MacroIntent.ProductDetail.TRACE

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            MacroIntent.ProductDetail.PACKAGE_NAME,
            MacroIntent.ProductDetail.RECYCLER_VIEW_ID
        )
    }
}
