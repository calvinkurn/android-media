package com.tokopedia.home.component

import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.BannerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.CategoryWidgetViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PopularKeywordViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.TickerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.NewBusinessViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass

private const val TAG = "HomeDynamicChannelComponentAnalyticsTest"
/**
 * Created by yfsx on 2/9/21.
 */
class HomeRevampDynamicChannelComponentAnalyticsTest {
    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
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
        disableCoachMark(context)
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testComponentProductHighlight() {
        initTest()

        doActivityTest(ProductHighlightComponentViewHolder::class) { _: RecyclerView.ViewHolder, _: Int ->
            clickOnProductHighlightItem()
        }

        getAssertProductHighlight(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testComponentPopularKeyword() {
        initTest()

        doActivityTest(PopularKeywordViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnPopularKeywordSection(viewHolder, i)
        }

        getAssertPopularKeyword(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testComponentMixLeft() {
        initTest()

        doActivityTest(MixLeftComponentViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnMixLeftSection(viewHolder, i)
        }

        getAssertMixLeft(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testComponentMixTop() {
        initTest()

        doActivityTest(MixTopComponentViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnMixTopSection(viewHolder, i)
        }

        getAssertMixTop(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

//    @Test
//    fun testComponentBUWidget() {
//        initTest()
//
//        doActivityTest(NewBusinessViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
//            clickOnBusinessWidgetSection(viewHolder)
//        }
//
//        getAssertBUWiddet(gtmLogDBSource, context)
//
//        onFinishTest()
//
//        addDebugEnd()
//    }

    @Test
    fun testComponentLegoBanner() {
        initTest()
        doActivityTest(
                listOf(
                        DynamicLegoBannerViewHolder::class.simpleName!!,
                        Lego4AutoBannerViewHolder::class.simpleName!!,
                        DynamicLegoBannerSixAutoViewHolder::class.simpleName!!)
        ){ viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnLegoBannerSection(viewHolder, i)
        }

        getAssertLegoBanner(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testRecommendationFeedBanner() {
        initTest()

        doActivityTest(HomeRecommendationFeedViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnRecommendationFeedSection(viewHolder)
        }

        getAssertRecommendationFeedBanner(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

//    @Test
//    fun testRecommendationFeedTab() {
//        initTest()
//
//        login()
//
//        doActivityTest(HomeRecommendationFeedViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
//            clickOnRecommendationFeedSection(viewHolder)
//        }
//
//        getAssertRecommendationFeedTab(gtmLogDBSource, context)
//
//        onFinishTest()
//
//        addDebugEnd()
//    }

    @Test
    fun testRecommendationFeedProductLogin() {
        initTest()

        login()

        doActivityTest(HomeRecommendationFeedViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnRecommendationFeedSection(viewHolder)
        }

        getAssertRecommendationFeedProductLogin(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testOpenScreenHomepage() {
        initTest()

        doActivityTest(HomeRecommendationFeedViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnRecommendationFeedSection(viewHolder)
        }

        getAssertHomepageScreen(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testTicker() {
        initTest()

        doActivityTest(TickerViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnTickerSection(viewHolder)
        }

        getAssertTicker(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testCloseReminderWidget(){
        initTest()

        doActivityTest(ReminderWidgetViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int, homeRecycleView: RecyclerView ->
            clickCloseOnReminderWidget(viewHolder, i, homeRecycleView)
        }

        getAssertCloseReminderWidget(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testReminderWidget(){
        initTest()

        doActivityTest(ReminderWidgetViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int, homeRecycleView: RecyclerView ->
            clickOnReminderWidget(viewHolder, i, homeRecycleView)
        }

        getAssertReminderWidget(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testRecommendationFeedProductNonLogin() {
        initTest()

        doActivityTest(HomeRecommendationFeedViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnRecommendationFeedSection(viewHolder)
        }

        getAssertRecommendationFeedProductNonLogin(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testComponentCategoryWidget() {
        initTest()

        doActivityTest(CategoryWidgetViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnCategoryWidgetSection(viewHolder, i)
        }

        getAssertCategoryWidget(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        waitForData()
        hideStickyLogin()
    }

    private fun hideStickyLogin() {
        activityRule.runOnUiThread {
            val layout = activityRule.activity.findViewById<ConstraintLayout>(R.id.layout_sticky_container)
            if (layout.visibility == View.VISIBLE) {
                layout.visibility = View.GONE
            }
        }
    }

    private fun <T: Any> doActivityTest(viewClass : KClass<T>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int)-> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount)  {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)
            if (viewHolder != null && viewClass.simpleName == viewHolder.javaClass.simpleName) {
                isTypeClass.invoke(viewHolder, i)
            }
        }
        endActivityTest()
    }

    private fun <T: Any> doActivityTest(viewClass : KClass<T>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int, recycleView: RecyclerView)-> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount)  {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)
            if (viewHolder != null && viewClass.simpleName == viewHolder.javaClass.simpleName) {
                isTypeClass.invoke(viewHolder, i, homeRecyclerView)
            }
        }
        endActivityTest()
    }

    private fun doActivityTest(viewClassName: List<String>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int)-> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount)  {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)
            if (viewHolder != null && viewClassName.contains(viewHolder.javaClass.simpleName)) {
                isTypeClass.invoke(viewHolder, i)
            }
        }
        endActivityTest()
    }

    private fun endActivityTest() {
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
        waitForLoadCassavaAssert()
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }

    private fun onFinishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }
}