package com.tokopedia.sellerhome.testcase.cassava

import android.app.Application
import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringListener
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeIdlingResource
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

@LargeTest
@ExperimentalStdlibApi
class SellerHomeTrackerValidationTest {
    @get:Rule
    var activityRule: IntentsTestRule<SellerHomeActivity> = object : IntentsTestRule<SellerHomeActivity>(SellerHomeActivity::class.java, false, false) {
        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            sellerHomeLoadTimeMonitoringListener.onStartPltMonitoring()
            activity.loadTimeMonitoringListener = sellerHomeLoadTimeMonitoringListener
            markAsIdleIfPltIsSucceed()
        }
    }

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    // this is only to make the test wait until widgets rendered
    val sellerHomeLoadTimeMonitoringListener = object : LoadTimeMonitoringListener {
        override fun onStartPltMonitoring() {
            SellerHomeIdlingResource.increment()
        }

        override fun onStopPltMonitoring() {
            SellerHomeIdlingResource.decrement()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun init() {
        setUpTimeoutPolicy()
        this.registerIdlingResources()
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(createMockModelConfig())
        login()
        Espresso.onIdle()
        activityRule.launchActivity(SellerHomeActivity.createIntent(InstrumentationRegistry.getInstrumentation().targetContext))
        Espresso.onIdle()
    }

    @After
    fun tearDown() {
        this.unregisterIdlingResources()
    }

    @Test
    fun validateWidgetsImpression() {
        actionTest {
            clearQueries()
            addQueriesToValidate(context,
                    "tracker/seller_home/seller_home_card_widget_impression.json",
                    "tracker/seller_home/seller_home_description_widget_impression.json",
                    "tracker/seller_home/seller_home_line_graph_widget_impression.json",
                    "tracker/seller_home/seller_home_post_widget_impression.json",
                    "tracker/seller_home/seller_home_progress_bar_widget_impression.json",
                    "tracker/seller_home/seller_home_carousel_widget_impression.json")
            scrollThrough(activityRule.activity, R.id.recycler_view)
            validate(gtmLogDBSource, 8, 1, 1, 2, 1, 1) // 8 card, 1 description, 1 line graph, 2 post, 1 progress bar
        }
    }

    @Test
    fun validateLineGraphWidgetsCtaClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(context,
                    "tracker/seller_home/seller_home_line_graph_widget_cta_click.json")
            blockAllIntent()
            var clickCount = 0
            mockRouteManager { clickCount++ }

            clickAllWidgetCtaWithType<LineGraphWidgetUiModel>(activityRule.activity, R.id.recycler_view, com.tokopedia.sellerhomecommon.R.id.btnLineGraphMore)

            validate(gtmLogDBSource, clickCount)
        }
    }

    @Test
    fun validatePostWidgetsCtaClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(context,
                    "tracker/seller_home/seller_home_post_widget_cta_click.json")
            blockAllIntent()
            var clickCount = 0
            mockRouteManager { clickCount++ }

            clickAllWidgetCtaWithType<PostListWidgetUiModel>(activityRule.activity, R.id.recycler_view, com.tokopedia.sellerhomecommon.R.id.tvPostListSeeDetails)

            validate(gtmLogDBSource, clickCount)
        }
    }

    @Test
    fun validateProgressBarWidgetsCtaClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(context,
                    "tracker/seller_home/seller_home_progress_bar_widget_cta_click.json")
            blockAllIntent()
            var clickCount = 0
            mockRouteManager { clickCount++ }

            clickAllWidgetCtaWithType<ProgressWidgetUiModel>(activityRule.activity, R.id.recycler_view, com.tokopedia.sellerhomecommon.R.id.tvProgressSeeDetails)

            validate(gtmLogDBSource, clickCount)
        }
    }

    @Test
    fun validateDescriptionWidgetsCtaClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(context,
                    "tracker/seller_home/seller_home_description_widget_click.json")
            blockAllIntent()
            var clickCount = 0
            mockRouteManager { clickCount++ }

            clickAllWidgetCtaWithType<DescriptionWidgetUiModel>(activityRule.activity, R.id.recycler_view, com.tokopedia.sellerhomecommon.R.id.tvDescriptionCta)

            validate(gtmLogDBSource, clickCount)
        }
    }

    @Test
    fun validateCarouselWidgetsCtaClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(context,
                    "tracker/seller_home/seller_home_carousel_widget_cta_click.json")
            blockAllIntent()
            var clickCount = 0
            mockRouteManager { clickCount++ }

            clickAllWidgetCtaWithType<CarouselWidgetUiModel>(activityRule.activity, R.id.recycler_view, com.tokopedia.sellerhomecommon.R.id.btnCarouselSeeAll)

            validate(gtmLogDBSource, clickCount)
        }
    }

    @Test
    fun validateCardWidgetClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(context,
                    "tracker/seller_home/seller_home_card_widget_click.json")
            blockAllIntent()

            var cardWidgetClickCount = 0
            mockRouteManager { cardWidgetClickCount++ }
            clickAllWidgetsWithType<CardWidgetUiModel>(activityRule.activity, R.id.recycler_view)

            validate(gtmLogDBSource, cardWidgetClickCount)
        }
    }

    @Test
    fun validatePostWidgetItemClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(context,
                    "tracker/seller_home/seller_home_post_widget_item_click.json")
            blockAllIntent()

            var postWidgetItemClickCount = 0
            mockRouteManager { postWidgetItemClickCount++ }
            clickAllItemInChildRecyclerView<PostListWidgetUiModel, PostUiModel>(activityRule.activity, R.id.recycler_view, com.tokopedia.sellerhomecommon.R.id.rvPostList)

            validate(gtmLogDBSource, postWidgetItemClickCount)
        }
    }

    @Test
    fun validateCarouselWidgetItemClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(context,
                    "tracker/seller_home/seller_home_carousel_widget_item_click.json")
            blockAllIntent()

            var carouselWidgetItemClickCount = 0
            mockRouteManager { carouselWidgetItemClickCount++ }
            clickAllBannerItem(activityRule.activity, R.id.recycler_view, com.tokopedia.sellerhomecommon.R.id.rvCarouselBanner)

            validate(gtmLogDBSource, carouselWidgetItemClickCount)
        }
    }

    private fun markAsIdleIfPltIsSucceed() {
        val performanceData = activityRule.activity.performanceMonitoringSellerHomeLayoutPlt?.getPltPerformanceMonitoring()
        if (performanceData?.isSuccess == true) {
            sellerHomeLoadTimeMonitoringListener.onStopPltMonitoring()
        }
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(
                InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application,
                SellerHomeIdlingResource.idlingResource,
                "try.sugiharto+02@tokopedia.com",
                "tokopedia789"
        )
    }

    private fun setUpTimeoutPolicy() {
        IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.MINUTES)
    }

    private fun registerIdlingResources() {
        IdlingRegistry.getInstance().register(SellerHomeIdlingResource.idlingResource)
    }

    private fun unregisterIdlingResources() {
        IdlingRegistry.getInstance().unregister(SellerHomeIdlingResource.idlingResource)
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse("GoldGetUserShopInfo", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_get_user_role), FIND_BY_CONTAINS)
                addMockResponse("shopInfoMoengage", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_shop_info_moengage), FIND_BY_CONTAINS)
                addMockResponse("notifications", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_notification), FIND_BY_CONTAINS)
                addMockResponse("GetSellerDashboardPageLayout", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_layout), FIND_BY_CONTAINS)
                addMockResponse("getTicker", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_get_ticker), FIND_BY_CONTAINS)
                addMockResponse("updateShopActive", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_update_shop_active), FIND_BY_CONTAINS)
                addMockResponse("shopInfoByID", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_shop_info_location), FIND_BY_CONTAINS)
                addMockResponse("fetchCardWidgetData", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_card_widgets), FIND_BY_CONTAINS)
                addMockResponse("fetchLineGraphWidgetData", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_line_graph_widgets), FIND_BY_CONTAINS)
                addMockResponse("fetchProgressBarWidgetData", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_progressbar_widgets), FIND_BY_CONTAINS)
                addMockResponse("fetchPostWidgetData", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_post_widgets), FIND_BY_CONTAINS)
                addMockResponse("fetchCarouselWidgetData", InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_carousel_widgets), FIND_BY_CONTAINS)
                return this
            }
        }
    }
}