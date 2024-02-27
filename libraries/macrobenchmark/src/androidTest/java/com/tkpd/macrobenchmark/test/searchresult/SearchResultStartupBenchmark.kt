package com.tkpd.macrobenchmark.test.searchresult

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
class SearchResultStartupBenchmark(startupMode: StartupMode) : BaseStartupBenchmark(startupMode) {
    override fun setupMock() {
        MacroDevOps.setupEnvironment(MacroIntent.Mock.getSearchMockIntent())
    }

    override fun setupEnvironment() {
        MacroDevOps.setupLoginFlow(
            email = "pbs-hidayatullah+prod7@tokopedia.com",
            password = "Prod1234"
        )
    }

    override fun getIntent() = MacroIntent.SearchResult.getSearchResultIntent()

    override fun waitUntil() {
        MacroInteration.waitForRecyclerViewContent(
            MacroIntent.SearchResult.PACKAGE_NAME,
            MacroIntent.SearchResult.RV_RESOURCE_ID
        )
    }

    override fun traceName() = "search_result_trace"
}
