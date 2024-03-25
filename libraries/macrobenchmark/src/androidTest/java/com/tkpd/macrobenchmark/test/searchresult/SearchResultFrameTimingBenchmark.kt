package com.tkpd.macrobenchmark.test.searchresult
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
class SearchResultFrameTimingBenchmark : BaseFrameTimingBenchmark() {
    override fun setupEnvironment() {
//        MacroDevOps.setupLoginFlow(
//            "pbs-hidayatullah+prod7@tokopedia.com",
//            "Prod1234"
//        )
        /**
         * Open search then kill to fetch rollence
         */
        MacroDevOps.openTargetThenKill(
            MacroIntent.SearchResult.getSearchResultIntent()
        )
    }

    override fun setupMock() {
        MacroDevOps.setupEnvironment(MacroIntent.Mock.getSearchMockIntent())
    }
    override fun pageInteractionTest(currentIteration: Int) {
        Thread.sleep(12000)
        MacroInteration.basicFlingInteraction(
            MacroIntent.SearchResult.PACKAGE_NAME,
            MacroIntent.SearchResult.RV_RESOURCE_ID,
            flingSpeed = 3000
        )
    }

    override fun getIntent() = MacroIntent.SearchResult.getSearchResultIntent()
}
