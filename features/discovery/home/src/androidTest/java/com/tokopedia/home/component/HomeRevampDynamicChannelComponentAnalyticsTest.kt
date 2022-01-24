package com.tokopedia.home.component

import android.app.Activity
import android.app.Instrumentation
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.home.util.ViewVisibilityIdlingResource
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass


private const val TAG = "HomeDynamicChannelComponentAnalyticsTest"
/**
 * Created by yfsx on 2/9/21.
 */
@CassavaTest
class HomeRevampDynamicChannelComponentAnalyticsTest {
    @get:Rule
    var activityRule = object: IntentsTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            disableCoachMark(context)
            setupGraphqlMockResponse(HomeMockResponseConfig())
        }
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var visibilityIdlingResource: ViewVisibilityIdlingResource? = null
    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null

    @Before
    fun resetAll() {
        disableCoachMark(context)
        intending(isInternal()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
        val recyclerView: RecyclerView =
                activityRule.activity.findViewById(R.id.home_fragment_recycler_view)
        homeRecyclerViewIdlingResource = HomeRecyclerViewIdlingResource(
                recyclerView = recyclerView
        )
        IdlingRegistry.getInstance().register(homeRecyclerViewIdlingResource)
    }

    @After
    fun tearDown() {
        visibilityIdlingResource?.let {
            IdlingRegistry.getInstance().unregister(visibilityIdlingResource)
        }
        IdlingRegistry.getInstance().unregister(homeRecyclerViewIdlingResource)
    }

    @Test
    fun testComponentProductHighlight() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = ProductHighlightDataModel::class) { _: RecyclerView.ViewHolder, _: Int ->
                clickOnProductHighlightItem()
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_PRODUCT_HIGHLIGHT)
        }
    }

    @Test
    fun testComponentPopularKeyword() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(delayBeforeRender = 2000, dataModelClass = PopularKeywordListDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnPopularKeywordSection(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_POPULAR_KEYWORD)
        }
    }

    @Test
    fun testComponentMixLeft() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = MixLeftDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnMixLeftSection(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_LEFT)
        }
    }

    @Test
    fun testComponentMixTop() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = MixTopDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnMixTopSection(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_TOP)
        }
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
        HomeDCCassavaTest {
            initTest()
            doActivityTest(
                listOf(
                    DynamicLegoBannerViewHolder::class.simpleName!!,
                    Lego4AutoBannerViewHolder::class.simpleName!!,
                    DynamicLegoBannerSixAutoViewHolder::class.simpleName!!)
            ){ viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnLegoBannerSection(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LEGO_BANNER)
        }
    }

    @Test
    fun testRecommendationFeedBanner() {
        onView(withId(R.id.home_fragment_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = HomeRecommendationFeedDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnRecommendationFeedSection(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_BANNER)
        }
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
        onView(withId(R.id.home_fragment_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        HomeDCCassavaTest {
            initTest()
            login()
            doActivityTestByModelClass(dataModelClass = HomeRecommendationFeedDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnRecommendationFeedSection(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_PRODUCT_LOGIN)
        }
    }

    @Test
    fun testOpenScreenHomepage() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = HomeRecommendationFeedDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnRecommendationFeedSection(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_SCREEN)
        }
    }

    @Test
    fun testTicker() {
        visibilityIdlingResource = ViewVisibilityIdlingResource(
                activity = activityRule.activity,
                viewId = R.id.ticker_description,
                expectedVisibility = View.VISIBLE
        )
        IdlingRegistry.getInstance().register(visibilityIdlingResource)

        initTest()
        doActivityTestByModelClass(dataModelClass = TickerDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnTickerSection(viewHolder)
        }

        MatcherAssert.assertThat(cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TICKER), hasAllSuccess())
    }

    @Test
    fun testCloseReminderWidget(){
        HomeDCCassavaTest {
            initTest()
            doActivityTest(ReminderWidgetViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int, homeRecycleView: RecyclerView ->
                //close salam widget
                clickCloseOnReminderWidget(viewHolder, i, homeRecycleView)
                //close digital widget
                clickCloseOnReminderWidget(viewHolder, i, homeRecycleView)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_RECHARGE_CLOSE)
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_SALAM_CLOSE)
        }
    }

    @Test
    fun testReminderWidget(){
        HomeDCCassavaTest {
            initTest()
            doActivityTest(ReminderWidgetViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int, homeRecycleView: RecyclerView ->
                //click salam widget
                clickOnReminderWidget(viewHolder, i, homeRecycleView)
                //click digital widget
                clickOnReminderWidget(viewHolder, i, homeRecycleView)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_RECHARGE)
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_SALAM)
        }
    }

    @Test
    fun testRecommendationFeedProductNonLogin() {
        onView(withId(R.id.home_fragment_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = HomeRecommendationFeedDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnRecommendationFeedSection(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_PRODUCT_NONLOGIN)
        }
    }

    @Test
    fun testComponentCategoryWidget() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = DynamicChannelDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnCategoryWidgetSection(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_CATEGORY_WIDGET)
        }
    }

    @Test
    fun testRechargeBUWidget() {
        HomeDCCassavaTest {
            initTest()
            login()
            doActivityTestByModelClass(dataModelClass = RechargeBUWidgetDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                checkRechargeBUWidget(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECHARGE_BU_WIDGET)
        }
    }

    @Test
    fun testComponentListCarousel() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = RecommendationListCarouselDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                activityRule.runOnUiThread { viewHolder.itemView.findViewById<View>(R.id.buttonAddToCart).performClick() }
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recycleList, 0)
                onView(withId(R.id.buy_again_close_image_view)).perform(ViewActions.click())
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LIST_CAROUSEL)
        }
    }

    @Test
    fun testBannerComponentWidget() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = BannerDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnBannerCarouselWidget(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_BANNER_CAROUSEL)
        }
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

    private fun <T: Any> doActivityTestByModelClass(delayBeforeRender: Long = 2000L, dataModelClass : KClass<T>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemClickLimit: Int)-> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val homeRecycleAdapter = homeRecyclerView.adapter as? HomeRecycleAdapter

        val visitableList = homeRecycleAdapter?.currentList?: listOf()
        val targetModel = visitableList.find { it.javaClass.simpleName == dataModelClass.simpleName }
        val targetModelIndex = visitableList.indexOf(targetModel)

        targetModelIndex.let { targetModelIndex->
            scrollHomeRecyclerViewToPosition(homeRecyclerView, targetModelIndex)
            if (delayBeforeRender > 0) Thread.sleep(delayBeforeRender)
            val targetModelViewHolder = homeRecyclerView.findViewHolderForAdapterPosition(targetModelIndex)
            targetModelViewHolder?.let { targetModelViewHolder-> isTypeClass.invoke(targetModelViewHolder, targetModelIndex) }
        }
        endActivityTest()
    }

    private fun <T: Any> doActivityTest(viewClass : KClass<T>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int)-> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount)  {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            Thread.sleep(1000)
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
        activityRule.activity.moveTaskToBack(true)
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

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }
}