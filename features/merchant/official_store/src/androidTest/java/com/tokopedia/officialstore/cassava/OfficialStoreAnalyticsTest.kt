package com.tokopedia.officialstore.cassava

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.FeaturedBrandViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.officialstore.OfficialStoreActivity
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.environment.InstrumentationOfficialStoreTestFullActivity
import com.tokopedia.officialstore.extension.selectTabAtPosition
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.*
import com.tokopedia.officialstore.official.presentation.listener.OSFeaturedBrandCallback
import com.tokopedia.officialstore.util.OSRecyclerViewIdlingResource
import com.tokopedia.officialstore.util.preloadRecomOnOSPage
import com.tokopedia.officialstore.util.removeProgressBarOnOsPage
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 1/11/21.
 */
class OfficialStoreAnalyticsTest {

    companion object {
        private const val TAG = "OfficialStoreAnalyticsTest"
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME =
            "tracker/official_store/official_store_page.json"
    }

    private var osRecyclerViewIdlingResource: OSRecyclerViewIdlingResource? = null

    @get:Rule
    var activityRule = object: IntentsTestRule<InstrumentationOfficialStoreTestFullActivity>(
            InstrumentationOfficialStoreTestFullActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
        }
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Before
    fun setup() {
        setupGraphqlMockResponse(OfficialStoreMockResponseConfig())
        osRecyclerViewIdlingResource = OSRecyclerViewIdlingResource(
                activity = activityRule.activity,
                limitCountToIdle = 3
        )
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        IdlingRegistry.getInstance().register(osRecyclerViewIdlingResource)
//        activityRule.launchActivity(
//            Intent(
//                InstrumentationRegistry.getInstrumentation().targetContext,
//                OfficialStoreActivity::class.java
//            )
//        )
    }

    @After
    fun deleteDatabase() {
        IdlingRegistry.getInstance().unregister(osRecyclerViewIdlingResource)
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
        // 1. click category OS
        onView(withId(R.id.tablayout)).perform(selectTabAtPosition(0))
        // 2. scroll and click item at OS
        // Scroll to bottom first and then back to top for load all data (recom case)
        val recyclerView =
            activityRule.activity.findViewById<RecyclerView>(R.id.os_child_recycler_view)
        val itemCount = recyclerView.adapter?.itemCount ?: 0
        val productRecommendationOffset = 10

        /**
         * This function needed to remove any loading view, because any infinite loop rendered view such as loading view,
         * shimmering, progress bar, etc can block instrumentation test
         */
        removeProgressBarOnOsPage(recyclerView, activityRule.activity)

        /**
         * This function needed to trigger product recommendation usecase in official store,
         * official store page only hit recommendation usecase on scroll in the end of current list
         */
        preloadRecomOnOSPage(recyclerView)
        for (i in 0 until (itemCount + productRecommendationOffset)) {
            scrollRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }
        activityRule.activity.moveTaskToBack(true)
        logTestMessage("Done UI Test")
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
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recycleList, 0)
            }
            is DynamicChannelSprintSaleViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_sprintsale_rv,0)
            }
            is DynamicChannelLegoViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_lego_rv,0)
            }
            is DynamicChannelThematicViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(
                    viewHolder.itemView,
                    R.id.carouselProductCardRecyclerView,
                    0
                )
            }
            is OfficialProductRecommendationViewHolder -> {
                activityRule.runOnUiThread {
                    viewHolder.itemView.performClick()
                }
            }
            is FeaturedBrandViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recycleList,0)
            }
        }
    }
    @Test
    fun checkOSAnalyticsWithCassava2() {
        onView(CommonMatcher.firstView(withId(R.id.os_child_recycler_view))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        OSCassavaTest {
            initTest()
            doActivityTest()
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
        }
    }
}