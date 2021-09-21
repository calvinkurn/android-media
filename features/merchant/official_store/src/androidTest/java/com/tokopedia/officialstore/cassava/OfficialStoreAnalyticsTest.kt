package com.tokopedia.officialstore.cassava

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.officialstore.OfficialStoreActivity
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.extension.selectTabAtPosition
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialBannerViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialBenefitViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialFeaturedShopViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialProductRecommendationViewHolder
import com.tokopedia.officialstore.official.presentation.dynamic_channel.*
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 1/11/21.
 */
class OfficialStoreAnalyticsTest {

    companion object{
        private const val TAG = "OfficialStoreAnalyticsTest"
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/official_store/official_store_page.json"
    }

    @get:Rule
    var activityRule = ActivityTestRule(OfficialStoreActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse(OfficialStoreMockResponseConfig())
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, OfficialStoreActivity::class.java))
    }

    @After
    fun dispose(){
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testOfficialStore() {
        initTest()
        doActivityTest()
        assertCassava()
        addDebugEnd()
    }

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        waitForData()
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun addDebugEnd() {
        Thread.sleep(5000)
    }

    private fun doActivityTest() {
        // 1. click category OS
        onView(withId(R.id.tablayout)).perform(selectTabAtPosition(0))
        // 2. scroll and click item at OS
        // Scroll to bottom first and then back to top for load all data (recom case)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        onView(firstView(withId(R.id.recycler_view))).perform(ViewActions.swipeUp())
        Thread.sleep(2500)
        recyclerView.layoutManager?.smoothScrollToPosition(recyclerView, null, 0)
        Thread.sleep(2500)
        val itemCount = recyclerView.adapter?.itemCount ?: 0
        val productRecommendationOffset = 5
        for (i in 0 until (itemCount + productRecommendationOffset)) {
            scrollRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }
        activityRule.activity.moveTaskToBack(true)
        logTestMessage("Done UI Test")
    }

    private fun assertCassava() {
        waitForData()
        //worked
        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME),
                hasAllSuccess())
    }

    private fun scrollRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }

    private fun checkProductOnDynamicChannel(officialStoreRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = officialStoreRecyclerView.findViewHolderForAdapterPosition(i)) {
            is MixLeftComponentViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, MixLeftComponentViewHolder.RECYCLER_VIEW_ID, 0)
            }
            is MixTopComponentViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
            }
            is OfficialBenefitViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recyclerview_official_benefit, 0)
            }
            is OfficialFeaturedShopViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recycler_view_featured_shop,0)
            }
            is OfficialBannerViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.viewpager_banner_category,0)
            }
            is DynamicLegoBannerViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_lego_rv,0)
            }
            is DynamicChannelSprintSaleViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_sprintsale_rv,0)
            }
            is DynamicChannelLegoViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_lego_rv,0)
            }
            is DynamicChannelThematicViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.carouselProductCardRecyclerView,0)
            }
            is OfficialProductRecommendationViewHolder -> {
                activityRule.runOnUiThread {
                    viewHolder.itemView.performClick()
                }
            }
        }
    }
}