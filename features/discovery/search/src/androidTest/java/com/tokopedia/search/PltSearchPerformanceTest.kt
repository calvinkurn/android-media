package com.tokopedia.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.test.application.TestRepeatRule
import org.junit.Rule
import org.junit.Test

class PltSearchPerformanceTest {
    companion object {
        const val TEST_CASE_PAGE_LOAD_TIME_SEARCH_PERFORMANCE = "search_test_case_page_load_time"
    }

    @get:Rule
    var activityRule: ActivityTestRule<SearchActivity> = ActivityTestRule(SearchActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Test
    fun testPageLoadTimePerformance() {
        setUpActivity()
        Thread.sleep(10000)
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_SEARCH_PERFORMANCE)
        activityRule.activity.finishAndRemoveTask()
        Thread.sleep(1000)
    }

    private fun setUpActivity() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = createIntent(context, "samsung")
        activityRule.launchActivity(intent)
    }

    private fun createIntent(context: Context?, query: String): Intent? {
        val intent = Intent(context, SearchActivity::class.java)
        intent.data = Uri.parse(ApplinkConstInternalDiscovery.SEARCH_RESULT + "?q=" + query)
        return intent
    }

    private fun savePLTPerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.getPltPerformanceResultData()
        performanceData?.let {
            var dataSource = if (!it.isSuccess) {
                "failed"
            } else "network"

            PerformanceDataFileUtils.writePLTPerformanceFile(
                    activityRule.activity,
                    tag,
                    it,
                    dataSource
            )
        }
    }
}