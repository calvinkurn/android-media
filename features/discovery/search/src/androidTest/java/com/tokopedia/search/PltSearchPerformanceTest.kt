package com.tokopedia.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.junit.After
import org.junit.Before
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

    private val recyclerViewId = R.id.recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null

    @Before
    fun setUp() {
        setupGraphqlMockResponseWithCheck(SearchMockModelConfig())
        setupActivity()
        setupIdlingResource()
    }

    private fun setupActivity() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = createIntent(context, "samsung")
        activityRule.launchActivity(intent)
    }

    private fun createIntent(context: Context?, query: String): Intent? {
        val intent = Intent(context, SearchActivity::class.java)
        intent.data = Uri.parse(ApplinkConstInternalDiscovery.SEARCH_RESULT + "?q=" + query)
        return intent
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)
        recyclerViewIdlingResource = RecyclerViewHasItemIdlingResource(recyclerView)

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    @Test
    fun testPageLoadTimePerformance() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))

        savePLTPerformanceResultData()

        activityRule.activity.finishAndRemoveTask()
    }

    private fun savePLTPerformanceResultData() {
        val performanceData = activityRule.activity.pltPerformanceResultData
        performanceData?.let {
            val dataSource = getDataSource(it)

            PerformanceDataFileUtils.writePLTPerformanceFile(
                    activityRule.activity,
                    TEST_CASE_PAGE_LOAD_TIME_SEARCH_PERFORMANCE,
                    it,
                    dataSource
            )
        }
    }

    private fun getDataSource(it: PltPerformanceData): String {
        return if (!it.isSuccess) "failed" else "network"
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }
}