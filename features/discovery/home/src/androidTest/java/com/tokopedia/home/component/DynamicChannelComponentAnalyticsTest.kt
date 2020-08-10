package com.tokopedia.home.component

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.BannerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.TickerViewHolder
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_BANNER = "tracker/home/hpb.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_SCREEN = "tracker/home/homescreen.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TICKER = "tracker/home/ticker.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LIST_CAROUSEL = "tracker/home/list_carousel.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_LEFT = "tracker/home/mix_left.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_TOP = "tracker/home/mix_top.json"
private const val TAG = "DynamicChannelComponentAnalyticsTest"

/**
 * @author by yoasfs on 07/07/20
 */
class DynamicChannelComponentAnalyticsTest {

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeTestActivity>(InstrumentationHomeTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HomeMockResponseConfig())
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun resetAll() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testDCHome() {
        initTest()

        doActivityTest()

        doAnalyticDebuggerTest()

        onFinishTest()

        addDebugEnd()
    }

    private fun initTest() {
        waitForData()
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun addDebugEnd() {
        Thread.sleep(5000)
    }

    private fun doActivityTest() {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            checkProductOnDynamicChannel(homeRecyclerView, i)
        }
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun doAnalyticDebuggerTest() {
        waitForData()

        //non login state, without userId
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_SCREEN),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_BANNER),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TICKER),
//                hasAllSuccess())
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_LEFT),
                hasAllSuccess())
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_TOP),
                hasAllSuccess())
    }

    private fun onFinishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 350) }
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        val viewholder = homeRecyclerView.findViewHolderForAdapterPosition(i)
        when (viewholder) {
            is TickerViewHolder -> {
                logTestMessage("VH TickerViewHolder")
                clickTickerItem(viewholder.itemView)
            }
            is BannerViewHolder -> {
                logTestMessage("VH BannerViewHolder")
                clickHomeBannerItemAndViewAll(viewholder.itemView)
            }
            is MixLeftComponentViewHolder -> {
                logTestMessage("VH MixLeftComponentViewHolder")
                clickLihatSemuaButtonIfAvailable(viewholder.itemView, "MixLeftComponentViewHolder")
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.rv_product, "MixLeftComponentViewHolder")
            }
            is MixTopComponentViewHolder -> {
                logTestMessage("VH MixTopComponentViewHolder")
                clickLihatSemuaButtonIfAvailable(viewholder.itemView, "MixTopComponentViewHolder")
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.dc_banner_rv, "MixTopComponentViewHolder")
                clickOnMixTopCTA(viewholder.itemView)
            }
        }
    }

    private fun clickTickerItem(view: View) {
        val childView = view
        val textApplink = childView.findViewById<View>(R.id.ticker_description)
        val closeButton = childView.findViewById<View>(R.id.ticker_close_icon)
        if (textApplink.visibility == View.VISIBLE) {
            try {
                Espresso.onView(firstView(ViewMatchers.withId(R.id.ticker_description)))
                        .perform(ViewActions.click())
                logTestMessage("Click SUCCESS ticker text")
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click FAILED ticker text")
            }
        }
        if (closeButton.visibility == View.VISIBLE) {
            try {
                Espresso.onView(firstView(ViewMatchers.withId(R.id.ticker_close_icon)))
                        .perform(ViewActions.click())
                logTestMessage("Click SUCCESS ticker close")
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click FAILED ticker close")
            }
        }
    }

    private fun clickHomeBannerItemAndViewAll(view: View) {
        val childView = view
        val seeAllButton = childView.findViewById<View>(R.id.see_all_promo)

        //banner item click
        val bannerViewPager = childView.findViewById<CircularViewPager>(R.id.circular_view_pager)
        val itemCount = bannerViewPager.getViewPager().adapter?.itemCount ?: 0
        for (i in 0 until itemCount) {
            try {
                Espresso.onView(firstView(ViewMatchers.withId(R.id.circular_view_pager)))
                        .perform(ViewActions.click())
                logTestMessage("Click SUCCESS banner item "  + 1)
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click FAILED banner item "  + i) }

        }
        //see all promo button click
        if (seeAllButton.visibility == View.VISIBLE) {
            try {
                Espresso.onView(firstView(ViewMatchers.withId(R.id.see_all_button)))
                        .perform(ViewActions.click())
                logTestMessage("Click SUCCESS See All Button BannerViewHolder")
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click FAILED See All Button BannerViewHolder")
            }
        }
    }

    private fun clickOnMixTopCTA(view: View) {
        val childView = view
        val bannerButton = childView.findViewById<View>(R.id.banner_button)
        if (bannerButton.visibility == View.VISIBLE) {
            try {
                Espresso.onView(firstView(ViewMatchers.withId(R.id.banner_button)))
                        .perform(ViewActions.click())
                logTestMessage("Click SUCCESS mixtop banner Button")
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click FAILED mixtop banner Button")
            }
        }
    }

    private fun clickLihatSemuaButtonIfAvailable(view: View, viewComponent: String) {
        val childView = view
        val seeAllButton = childView.findViewById<View>(R.id.see_all_button)
        if (seeAllButton.visibility == View.VISIBLE) {
            try {
                Espresso.onView(firstView(ViewMatchers.withId(R.id.see_all_button)))
                        .perform(ViewActions.click())
                logTestMessage("Click SUCCESS See All Button $viewComponent")
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click FAILED See All Button $viewComponent")
            }
        }

    }

    private fun clickOnEachItemRecyclerView(view: View, recyclerViewId: Int, viewComponent: String) {
        val childView = view
        val childRecyclerView = childView.findViewById<RecyclerView>(recyclerViewId)
        val childItemCount = childRecyclerView.adapter?.itemCount ?: 0
        logTestMessage("ChildCount $viewComponent: " + childItemCount + " item")

        for (j in 0 until childItemCount) {
            try {
                Espresso.onView(firstView(ViewMatchers.withId(recyclerViewId)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(j, ViewActions.click()))
                logTestMessage("Click SUCCESS $viewComponent child pos: " + j)
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click FAILED $viewComponent child pos: " + j)
            }
        }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }

    private fun <T> firstView(matcher: Matcher<T>): Matcher<T>? {
        return object : BaseMatcher<T>() {
            var isFirst = true
            override fun matches(item: Any?): Boolean {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false
                    return true
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("should return first matching item")
            }
        }
    }

}