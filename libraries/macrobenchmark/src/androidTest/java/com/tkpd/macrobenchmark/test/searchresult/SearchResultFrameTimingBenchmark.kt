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
class SearchResultFrameTimingBenchmark: BaseFrameTimingBenchmark() {
    override fun setupEnvironment() {
    }

    override fun setupMock() {
        MacroDevOps.setupEnvironment(MacroIntent.Mock.getSearchMockIntent())
    }
    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.basicRecyclerviewInteraction(
                MacroIntent.SearchResult.PACKAGE_NAME,
                MacroIntent.SearchResult.RV_RESOURCE_ID
        )
    }

    override fun getIntent() = MacroIntent.SearchResult.getSearchResultIntent()
}
