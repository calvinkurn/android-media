package com.tkpd.macrobenchmark.test.catalog

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
 * Created by DevAra
 * This test will measure MainParentActivity launch time with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(Parameterized::class)
class CatalogProductListStartupBenchmark(startupMode: StartupMode) : BaseStartupBenchmark(startupMode) {
    override fun setupEnvironment() {
    }

    override fun setupMock() {
        MacroDevOps.setupEnvironment(MacroIntent.Mock.getCatalogProductListMockIntent())
    }

    override fun getIntent() = MacroIntent.Catalog.getCatalogProductListIntent()

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            MacroIntent.Catalog.PACKAGE_NAME,
            MacroIntent.Catalog.RV_RESOURCE_ID_CATALOG_PRODUCT_LIST
        )
//        Thread.sleep(10000)
    }

    override fun traceName() = "catalog_product_list"
}
