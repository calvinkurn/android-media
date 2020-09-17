package com.tokopedia.home.component

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.collapsing.tab.layout.CollapsingTabLayout
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.BannerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.CategoryWidgetViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PopularKeywordViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.TickerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.NewBusinessViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_BANNER = "tracker/home/hpb.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_SCREEN = "tracker/home/homescreen.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TICKER = "tracker/home/ticker.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LIST_CAROUSEL = "tracker/home/list_carousel.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LEGO_BANNER = "tracker/home/lego_banner.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_POPULAR_KEYWORD = "tracker/home/popular_keyword.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_PRODUCT_HIGHLIGHT = "tracker/home/product_highlight.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_CATEGORY_WIDGET = "tracker/home/category_widget.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_BU_WIDGET = "tracker/home/bu_widget.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_LEFT = "tracker/home/mix_left.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_TOP = "tracker/home/mix_top.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_TAB = "tracker/home/recommendation_tab.json"
private const val TAG = "DynamicChannelComponentAnalyticsTest"

/**
 * @author by yoasfs on 07/07/20
 */
class DynamicChannelComponentAnalyticsTest {

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeTestActivity>(InstrumentationHomeTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            gtmLogDBSource.deleteAll().subscribe()
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
        //need improvement
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_SCREEN),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_BANNER),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LIST_CAROUSEL),
//                hasAllSuccess())
        //cant mock occ response

        //ontesting
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_TAB),
                hasAllSuccess())

        //worked
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_PRODUCT_HIGHLIGHT),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TICKER),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_POPULAR_KEYWORD),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_LEFT),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_TOP),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LEGO_BANNER),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_CATEGORY_WIDGET),
//                hasAllSuccess())
//        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_BU_WIDGET),
//                hasAllSuccess())
    }

    private fun onFinishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        val viewholder = homeRecyclerView.findViewHolderForAdapterPosition(i)
        when (viewholder) {
            is TickerViewHolder -> {
                val holderName = "TickerViewHolder"
                logTestMessage("VH $holderName")
                clickTickerItem(viewholder.itemView, i)
            }
            is BannerViewHolder -> {
                val holderName = "BannerViewHolder"
                logTestMessage("VH $holderName")
                clickHomeBannerItemAndViewAll(viewholder.itemView, i)
            }
            is MixLeftComponentViewHolder -> {
                val holderName = "MixLeftComponentViewHolder"
                logTestMessage("VH $holderName")
                clickLihatSemuaButtonIfAvailable(viewholder.itemView, holderName, i)
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.rv_product, holderName)
            }
            is MixTopComponentViewHolder -> {
                val holderName = "MixTopComponentViewHolder"
                logTestMessage("VH $holderName")
                clickLihatSemuaButtonIfAvailable(viewholder.itemView, holderName, i)
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.dc_banner_rv, holderName)
                clickOnMixTopCTA(viewholder.itemView)
            }
            is PopularKeywordViewHolder -> {
                val holderName = "PopularKeywordViewHolder"
                logTestMessage("VH $holderName")
                clickLihatSemuaPopularKeyword(viewholder.itemView, holderName, i)
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.rv_popular_keyword, holderName)
            }
            is RecommendationListCarouselViewHolder -> {
                val holderName = "RecommendationListCarouselViewHolder"
                logTestMessage("VH $holderName")
                clickItemAddToCartListCarousel(viewholder.itemView, R.id.recycleList, holderName)
                clickOnEachItemRecyclerView(viewholder.itemView, R.id.recycleList, holderName)
                clickCloseOnListCarousel(viewholder.itemView, holderName, i)
            }
            is ProductHighlightComponentViewHolder -> {
                val holderName = "ProductHighlightComponentViewHolder"
                logTestMessage("VH $holderName")
                clickOnProductHighlightItem(viewholder.itemView, holderName, i)
            }
            is DynamicLegoBannerViewHolder -> {
                val holderName = "DynamicLegoBannerViewHolder"
                logTestMessage("VH $holderName")
                clickLihatSemuaButtonIfAvailable(viewholder.itemView, holderName, i)
                clickSingleItemOnRecyclerView(viewholder.itemView, R.id.recycleList, holderName)
            }
            is Lego4AutoBannerViewHolder -> {
                val holderName = "Lego4AutoBannerViewHolder"
                logTestMessage("VH $holderName")
                clickLihatSemuaButtonIfAvailable(viewholder.itemView, holderName, i)
                clickSingleItemOnRecyclerView(viewholder.itemView, R.id.recycleList, holderName)
            }
            is CategoryWidgetViewHolder -> {
                val holderName = "CategoryWidgetViewHolder"
                logTestMessage("VH $holderName")
                clickLihatSemuaButtonIfAvailable(viewholder.itemView, holderName, i)
                clickSingleItemOnRecyclerView(viewholder.itemView, R.id.recycleList, holderName)
            }
            is NewBusinessViewHolder -> {
                val holderName = "NewBusinessViewHolder"
                logTestMessage("VH $holderName")
                clickBUWidgetTab(viewholder.itemView)
                clickSingleItemOnRecyclerView(viewholder.itemView, R.id.recycler_view, holderName)
            }
            is HomeRecommendationFeedViewHolder -> {
                val holderName = "HomeRecommendationFeedViewHolder"
                logTestMessage("VH $holderName")
                clickRecommendationFeedTab(viewholder.itemView)
            }
        }
    }

    private fun clickTickerItem(view: View, itemPos: Int) {
        val childView = view
        val textApplink = childView.findViewById<View>(R.id.ticker_description)
        val closeButton = childView.findViewById<View>(R.id.ticker_close_icon)
        if (textApplink.visibility == View.VISIBLE) {
            try {
                Espresso.onView(allOf(ViewMatchers.withId(R.id.ticker_description), isDisplayed())).perform(ViewActions.click())
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

    private fun clickHomeBannerItemAndViewAll(view: View, itemPos: Int) {
        val childView = view
        val seeAllButton = childView.findViewById<View>(R.id.see_more_label)

        //banner item click
        val bannerViewPager = childView.findViewById<CircularViewPager>(R.id.circular_view_pager)
        val itemCount = bannerViewPager.getViewPager().adapter?.itemCount ?: 0
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.circular_view_pager)))
                    .perform(ViewActions.click())
            logTestMessage("Click SUCCESS banner item "  + 0)
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED banner item "  + 0)
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

    private fun clickLihatSemuaButtonIfAvailable(view: View, viewComponent: String, itemPos: Int) {
        val childView = view
        val seeAllButton = childView.findViewById<View>(R.id.see_all_button)
        if (seeAllButton.visibility == View.VISIBLE) {
            try {
                Espresso.onView(ViewMatchers.withId(R.id.home_fragment_recycler_view))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemPos, clickOnViewChild(R.id.see_all_button)))
                logTestMessage("Click SUCCESS See All Button $viewComponent")
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click FAILED See All Button $viewComponent")
            }
        }
    }

    private fun clickLihatSemuaPopularKeyword(view: View, viewComponent: String, itemPos: Int) {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.tv_reload)))
                    .perform(ViewActions.click())
            logTestMessage("Click SUCCESS See All Button $viewComponent")
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED See All Button $viewComponent")
        }
    }

    private fun clickOnViewChild(viewId: Int) = object: ViewAction {
        override fun getDescription(): String  = ""

        override fun getConstraints() = null

        override fun perform(uiController: UiController, view: View)
                = ViewActions.click().perform(uiController, view.findViewById<View>(viewId))
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

    private fun clickSingleItemOnRecyclerView(view: View, recyclerViewId: Int, viewComponent: String) {
        val childView = view
        val childRecyclerView = childView.findViewById<RecyclerView>(recyclerViewId)
        val childItemCount = childRecyclerView.adapter?.itemCount ?: 0
        logTestMessage("ChildCount $viewComponent: " + childItemCount + " item")
        try {
            Espresso.onView(firstView(ViewMatchers.withId(recyclerViewId)))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))
            logTestMessage("Click SUCCESS $viewComponent child pos: " + 0)
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED $viewComponent child pos: " + 0)
        }
    }

    private fun clickItemAddToCartListCarousel(view: View, recyclerViewId: Int, viewComponent: String) {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(recyclerViewId)))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickOnViewChild(R.id.buttonAddToCart)))
            logTestMessage("Click SUCCESS atc $viewComponent")
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED atc $viewComponent")
        }
    }

    private fun clickCloseOnListCarousel(view: View, viewComponent: String, itemPos: Int)  {
        val childView = view
        val closeButton = childView.findViewById<View>(R.id.buy_again_close_image_view)
        if (closeButton.visibility == View.VISIBLE) {
            try {
                Espresso.onView(ViewMatchers.withId(R.id.home_fragment_recycler_view))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemPos, clickOnViewChild(R.id.buy_again_close_image_view)))
                logTestMessage("Click SUCCESS close $viewComponent")
            } catch (e: PerformException) {
                e.printStackTrace()
                logTestMessage("Click FAILED close $viewComponent")
            }
        }
    }

    private fun clickOnProductHighlightItem(view: View, viewComponent: String, itemPos: Int) {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.deals_product_card)))
                    .perform(ViewActions.click())
            logTestMessage("Click SUCCESS item $viewComponent")
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED item $viewComponent")
        }
    }

    private fun clickBUWidgetTab(view: View) {
        val childView = view
        //banner item click
        val tabPager = childView.findViewById<ViewPager2>(R.id.view_pager)
        try {
            Espresso.onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
            logTestMessage("Click SUCCESS BU tab pos "  + 1)
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED BU tab pos "  + 1)
        }
    }

    private fun clickRecommendationFeedTab(view: View) {
        val childView = view
        val tabPager = childView.findViewById<ViewPager>(R.id.view_pager_home_feeds)
        try {
            Espresso.onView(withId(R.id.tab_layout_home_feeds)).perform(selectTabAtPosition(1))
            logTestMessage("Click SUCCESS recom tab pos "  + 1)
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED recom tab pos "  + 1)
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

    fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(), ViewMatchers.isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                                .withCause(Throwable("No tab at index $tabIndex"))
                                .build()

                tabAtIndex.select()
            }
        }
    }

    fun selectCollapsingTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(), ViewMatchers.isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as CollapsingTabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                                .withCause(Throwable("No tab at index $tabIndex"))
                                .build()

                tabAtIndex.select()
            }
        }
    }

}