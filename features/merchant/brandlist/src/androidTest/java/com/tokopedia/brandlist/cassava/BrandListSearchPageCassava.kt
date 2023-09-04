package com.tokopedia.brandlist.cassava

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.activity.BrandlistSearchActivity
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchHeaderViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchRecommendationNotFoundViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchRecommendationViewHolder
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 1/28/21.
 */
class BrandListSearchPageCassava {

    companion object{
        private const val TAG = "BrandListSearchPageAnalyticsTest"
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/official_store/brandlist_search.json"
    }

    @get:Rule
    var activityRule = ActivityTestRule(BrandlistSearchActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun setup() {
        setupGraphqlMockResponse(BrandListPageMockResponse())
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, BrandlistSearchActivity::class.java))
    }

    @Test
    fun testBrandList() {
        initTest()

        doActivityTest()

        doHomeCassavaTest()

        addDebugEnd()
    }

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        waitForData()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun addDebugEnd() {
        Thread.sleep(5000)
    }

    private fun doActivityTest() {
        // 1. type something at search
        Espresso.onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(typeText("Coba"))
        Thread.sleep(1000)

        // 2. scroll and click item at OS
        // Scroll to bottom first and then back to top for load all data (recom case)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_brandlist_search)
        var itemCount = recyclerView.adapter?.itemCount ?: 0
        recyclerView.layoutManager?.smoothScrollToPosition(recyclerView, null, itemCount - 1)
        Thread.sleep(1000)
        recyclerView.layoutManager?.smoothScrollToPosition(recyclerView, null, 0)
        Thread.sleep(1000)
        itemCount = recyclerView.adapter?.itemCount ?: 0
        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }

        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun doHomeCassavaTest() {
        waitForData()
        //worked
        assertThat(cassavaRule.validate(ANALYTIC_VALIDATOR_QUERY_FILE_NAME), hasAllSuccess())
    }

    private fun scrollRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }

    private fun checkProductOnDynamicChannel(officialStoreRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = officialStoreRecyclerView.findViewHolderForAdapterPosition(i)) {
            is BrandlistSearchHeaderViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_groups_chip, 0)
            }
            is BrandlistSearchRecommendationViewHolder, is BrandlistSearchRecommendationNotFoundViewHolder -> {
                Espresso.onView(CoreMatchers.allOf(ViewMatchers.isDisplayed(), withId(R.id.rv_brandlist_search)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, click()))
            }
        }
    }
}