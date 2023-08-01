package com.tokopedia.home.component

import android.app.Activity
import android.app.Instrumentation
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.ui.HomeMockValueHelper
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.home.util.ViewVisibilityIdlingResource
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.home_component.visitable.*
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters
import kotlin.reflect.KClass

private const val TAG = "HomeDynamicChannelComponentAnalyticsTest"

/**
 * Created by yfsx on 2/9/21.
 */
@Ignore("ignored due to many intermittent issues")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@CassavaTest
class HomeRevampDynamicChannelComponentAnalyticsTest {
    @get:Rule
    var activityRule = object : IntentsTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            disableCoachMark(context)
            HomeMockValueHelper.setupAbTestRemoteConfig()
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
        login()
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
    fun testLoggedInBalanceWidget() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = HomeHeaderDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnBalanceWidget(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_BALANCE_WIDGET)
        }
    }

    @Test
    fun testOpenScreenHomepage() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = HomeRecommendationFeedDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
                scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_SCREEN)
        }
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
                clickOnPopularKeywordSection(viewHolder, i)
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
    fun testComponentMixLeftPadding() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = MixLeftPaddingDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnMixLeftPaddingSection(viewHolder, i)
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
                    DynamicLegoBannerSixAutoViewHolder::class.simpleName!!
                )
            ) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnLegoBannerSection(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LEGO_BANNER)
        }
    }

    @Ignore("Ignored due to intermittent error megafeed gone after clicked")
    @Test
    fun testComponentRecommendationFeed() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = HomeRecommendationFeedDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                onView(withId(R.id.home_fragment_recycler_view)).perform(ViewActions.swipeUp())
                CommonActions.clickOnEachItemRecyclerView(
                    viewHolder.itemView,
                    R.id.home_feed_fragment_recycler_view,
                    0
                )
                clickAllRecommendationFeedTabs(viewHolder.itemView)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED)
        }
    }

    @Test
    fun testComponentCueWidgetCategory() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = CueCategoryDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnCueWidgetCategory(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_CUE_WIDGET_CATEGORY)
        }
    }

    @Test
    fun testComponentVpsWidget() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = VpsDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnVpsWidget(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_VPS_WIDGET)
        }
    }

    @Test
    fun testComponentLego4Product() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = Lego4ProductDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnLego4Product(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LEGO_4_PRODUCT)
        }
    }

    @Test
    fun testComponentTicker() {
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
    fun testComponentReminderWidgetClose() {
        HomeDCCassavaTest {
            initTest()
            doActivityTest(ReminderWidgetViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int, homeRecycleView: RecyclerView ->
                // close salam widget
                clickCloseOnReminderWidget(viewHolder, i, homeRecycleView)
                // close digital widget
                clickCloseOnReminderWidget(viewHolder, i, homeRecycleView)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_RECHARGE_CLOSE)
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_SALAM_CLOSE)
        }
    }

    @Test
    fun testComponentReminderWidgetRecharge() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(
                dataModelClass = ReminderWidgetModel::class,
                predicate = { it?.source == ReminderEnum.RECHARGE }
            ) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
                // click digital widget
                clickOnReminderWidget(viewHolder, i, homeRecyclerView)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_RECHARGE)
        }
    }

    @Test
    fun testComponentReminderWidgetSalam() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(
                dataModelClass = ReminderWidgetModel::class,
                predicate = { it?.source == ReminderEnum.SALAM }
            ) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
                // click salam widget
                clickOnReminderWidget(viewHolder, i, homeRecyclerView)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_SALAM)
        }
    }

    @Test
    fun testComponentCategoryWidget() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = CategoryWidgetDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                clickOnCategoryWidgetSection(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_CATEGORY_WIDGET)
        }
    }

    @Test
    fun testComponentRechargeBUWidget() {
        HomeDCCassavaTest {
            initTest()
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
    fun testComponentBannerWidget() {
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

    @Test
    fun testComponentMerchantVoucherWidget() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = MerchantVoucherDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnMerchantVoucherWidget(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MERCHANT_VOUCHER)
        }
    }

    @Test
    fun testComponentSpecialReleaseWidget() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = SpecialReleaseDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnSpecialReleaseWidget(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_SPECIAL_RELEASE)
        }
    }

    @Test
    fun testComponentSpecialCampaignWidget() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = CampaignWidgetDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnCampaignWidget(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_CAMPAIGN_WIDGET)
        }
    }

    @Test
    fun testComponentHomeDynamicIcon() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = DynamicIconComponentDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnDynamicIcon(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_DYNAMIC_ICON)
        }
    }

    @Test
    fun testComponentDealsWidget() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = DealsDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnVpsWidget(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_DEALS_WIDGET)
        }
    }

    @Test
    fun testComponentFlashSaleWidget() {
        HomeDCCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = FlashSaleDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnFlashSaleWidget(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_FLASH_SALE_WIDGET)
        }
    }

    @Test
    fun testComponentMissionWidget() {
        HomeDCCassavaTest {
            initTest()
            login()
            waitForData()
            doActivityTestByModelClass(dataModelClass = MissionWidgetListDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnMissionWidget(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MISSION_WIDGET)
        }
    }

    @Test
    fun testComponentTodoWidget() {
        HomeDCCassavaTest {
            initTest()
            login()
            waitForData()
            doActivityTestByModelClass(dataModelClass = TodoWidgetListDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnTodoWidget(viewHolder, i)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TODO_WIDGET)
        }
    }

    private fun initTest() {
        login()
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

    private fun <T : Any> doActivityTestByModelClass(
        delayBeforeRender: Long = 2000L,
        dataModelClass: KClass<T>,
        predicate: (T?) -> Boolean = { true },
        isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemClickLimit: Int) -> Unit
    ) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val homeRecycleAdapter = homeRecyclerView.adapter as? HomeRecycleAdapter

        val visitableList = homeRecycleAdapter?.currentList ?: listOf()
        val targetModel = visitableList.find { it.javaClass.simpleName == dataModelClass.simpleName && predicate.invoke(it as? T) }
        val targetModelIndex = visitableList.indexOf(targetModel)

        targetModelIndex.let { targetModelIndex ->
            scrollHomeRecyclerViewToPosition(homeRecyclerView, targetModelIndex)
            if (delayBeforeRender > 0) Thread.sleep(delayBeforeRender)
            val targetModelViewHolder = homeRecyclerView.findViewHolderForAdapterPosition(targetModelIndex)
            targetModelViewHolder?.let { targetModelViewHolder -> isTypeClass.invoke(targetModelViewHolder, targetModelIndex) }
        }
        endActivityTest()
    }

    private fun <T : Any> doActivityTest(viewClass: KClass<T>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int) -> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            Thread.sleep(1000)
            val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)
            if (viewHolder != null && viewClass.simpleName == viewHolder.javaClass.simpleName) {
                isTypeClass.invoke(viewHolder, i)
            }
        }
        endActivityTest()
    }

    private fun <T : Any> doActivityTest(viewClass: KClass<T>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int, recycleView: RecyclerView) -> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)
            if (viewHolder != null && viewClass.simpleName == viewHolder.javaClass.simpleName) {
                isTypeClass.invoke(viewHolder, i, homeRecyclerView)
            }
        }
        endActivityTest()
    }

    private fun doActivityTest(viewClassName: List<String>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int) -> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount) {
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
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 400) }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }
}
