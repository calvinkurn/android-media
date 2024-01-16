package com.tkpd.macrobenchmark.test.catalog
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroDevOps
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith

/**
 * Created by DevAra
 * This test will measure MainParentActivity janky frames with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class CatalogProductListFrameTimingBenchmark : BaseFrameTimingBenchmark() {
    override fun setupEnvironment() {
    }

    override fun setupMock() {
        MacroDevOps.setupEnvironment(MacroIntent.Mock.getCatalogProductListMockIntent())
    }

    override fun pageInteractionTest(currentIteration: Int) {
        Thread.sleep(15000)
        MacroInteration.basicFlingInteraction(
            MacroIntent.Catalog.PACKAGE_NAME,
            MacroIntent.Catalog.RV_RESOURCE_ID_CATALOG_PRODUCT_LIST,
            flingSpeed = 3000
        )
    }

    override fun getIntent() = MacroIntent.Catalog.getCatalogProductListIntent()
}
