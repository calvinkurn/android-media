package com.tokopedia.sellerhome.testcase.cassava

import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.stub.features.home.presentation.SellerHomeActivityStub
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.DescriptionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressWidgetUiModel
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class SellerHomeTrackerValidationTest {

    @get:Rule
    var activityRule: IntentsTestRule<SellerHomeActivityStub> =
        object : IntentsTestRule<SellerHomeActivityStub>(
            SellerHomeActivityStub::class.java,
            false,
            false
        ) {
            override fun afterActivityLaunched() {
                super.afterActivityLaunched()
                waitingForWidgetLoaded()
            }
        }

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun init() {
        setupGraphqlMockResponse(createMockModelConfig())
        gtmLogDBSource.deleteAll().toBlocking().first()

        activityRule.launchActivity(SellerHomeActivityStub.createIntent(context))
    }

    @Test
    fun validateWidgetsImpression() {
        actionTest {
            clearQueries()
            addQueriesToValidate(
                "tracker/seller_home/seller_home_card_widget_impression.json",
                "tracker/seller_home/seller_home_description_widget_impression.json",
                "tracker/seller_home/seller_home_line_graph_widget_impression.json",
                "tracker/seller_home/seller_home_post_widget_impression.json",
                "tracker/seller_home/seller_home_progress_bar_widget_impression.json",
                "tracker/seller_home/seller_home_carousel_widget_impression.json"
            )
            scrollThrough(activityRule.activity, R.id.recycler_view)

            waitForTrackerSent()

            validate(
                context,
                gtmLogDBSource,
                8,
                1,
                1,
                2,
                1,
                1
            ) // 8 card, 1 description, 1 line graph, 2 post, 1 progress bar
        }
    }

    @Test
    fun validateLineGraphWidgetsCtaClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(
                "tracker/seller_home/seller_home_line_graph_widget_cta_click.json"
            )
            blockAllIntent()
            var clickCount = 0
            mockRouteManager { clickCount++ }

            clickAllWidgetCtaWithType<LineGraphWidgetUiModel>(
                activityRule.activity,
                R.id.recycler_view,
                com.tokopedia.sellerhomecommon.R.id.btnLineGraphMore
            )

            waitForTrackerSent()

            validate(context, gtmLogDBSource, clickCount)
        }
    }

    @Test
    fun validatePostWidgetsCtaClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(
                "tracker/seller_home/seller_home_post_widget_cta_click.json"
            )
            blockAllIntent()

            var clickCount = 0
            mockRouteManager { clickCount++ }

            clickAllWidgetCtaWithType<PostListWidgetUiModel>(
                activityRule.activity,
                R.id.recycler_view,
                com.tokopedia.sellerhomecommon.R.id.tvPostListSeeDetails
            )

            waitForTrackerSent()

            validate(context, gtmLogDBSource, clickCount)
        }
    }

    @Test
    fun validateProgressBarWidgetsCtaClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(
                "tracker/seller_home/seller_home_progress_bar_widget_cta_click.json"
            )
            blockAllIntent()
            var clickCount = 0
            mockRouteManager { clickCount++ }

            clickAllWidgetCtaWithType<ProgressWidgetUiModel>(
                activityRule.activity,
                R.id.recycler_view,
                com.tokopedia.sellerhomecommon.R.id.tvProgressSeeDetails
            )

            waitForTrackerSent()

            validate(context, gtmLogDBSource, clickCount)
        }
    }

    @Test
    fun validateDescriptionWidgetsCtaClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(
                "tracker/seller_home/seller_home_description_widget_click.json"
            )
            blockAllIntent()
            var clickCount = 0
            mockRouteManager { clickCount++ }

            clickAllWidgetCtaWithType<DescriptionWidgetUiModel>(
                activityRule.activity,
                R.id.recycler_view,
                com.tokopedia.sellerhomecommon.R.id.tvDescriptionCta
            )

            waitForTrackerSent()

            validate(context, gtmLogDBSource, clickCount)
        }
    }

    @Test
    fun validateCarouselWidgetsCtaClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(
                "tracker/seller_home/seller_home_carousel_widget_cta_click.json"
            )
            blockAllIntent()
            var clickCount = 0
            mockRouteManager { clickCount++ }

            clickAllWidgetCtaWithType<CarouselWidgetUiModel>(
                activityRule.activity,
                R.id.recycler_view,
                com.tokopedia.sellerhomecommon.R.id.btnCarouselSeeAll
            )

            waitForTrackerSent()

            validate(context, gtmLogDBSource, clickCount)
        }
    }

    @Test
    fun validateCardWidgetClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(
                "tracker/seller_home/seller_home_card_widget_click.json"
            )
            blockAllIntent()

            var cardWidgetClickCount = 0
            mockRouteManager { cardWidgetClickCount++ }
            clickAllWidgetsWithType<CardWidgetUiModel>(activityRule.activity, R.id.recycler_view)

            waitForTrackerSent()

            validate(context, gtmLogDBSource, cardWidgetClickCount)
        }
    }

    @Test
    fun validatePostWidgetItemClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate("tracker/seller_home/seller_home_post_widget_item_click.json")
            blockAllIntent()

            var postWidgetItemClickCount = 0
            mockRouteManager { postWidgetItemClickCount++ }
            clickAllItemInChildRecyclerView<PostListWidgetUiModel, PostItemUiModel>(
                activityRule.activity,
                R.id.recycler_view,
                com.tokopedia.sellerhomecommon.R.id.rvShcPostList
            )

            validate(context, gtmLogDBSource, postWidgetItemClickCount)
        }
    }

    @Test
    fun validateCarouselWidgetItemClick() {
        actionTest {
            clearQueries()
            addQueriesToValidate(
                "tracker/seller_home/seller_home_carousel_widget_item_click.json"
            )
            blockAllIntent()

            var carouselWidgetItemClickCount = 0
            mockRouteManager { carouselWidgetItemClickCount++ }
            clickAllBannerItem(
                activityRule.activity,
                R.id.recycler_view,
                com.tokopedia.sellerhomecommon.R.id.rvCarouselBanner
            )

            waitForTrackerSent()

            validate(context, gtmLogDBSource, carouselWidgetItemClickCount)
        }
    }

    private fun waitForTrackerSent() {
        Thread.sleep(2000)
    }

    private fun waitingForWidgetLoaded() {
        Thread.sleep(5000)
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse(
                    "GoldGetUserShopInfo",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_get_user_role
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "shopInfoMoengage",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_shop_info_moengage
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "notifications",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_notification
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "GetSellerDashboardPageLayout",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_layout
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "getTicker",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_get_ticker
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "updateShopActive",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_update_shop_active
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "shopInfoByID",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_shop_info_location
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "fetchCardWidgetData",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_card_widgets
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "fetchLineGraphWidgetData",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_line_graph_widgets
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "fetchProgressBarWidgetData",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_progressbar_widgets
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "fetchPostWidgetData",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_post_widgets
                    ),
                    FIND_BY_CONTAINS
                )
                addMockResponse(
                    "fetchCarouselWidgetData",
                    InstrumentationMockHelper.getRawString(
                        context,
                        com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_carousel_widgets
                    ),
                    FIND_BY_CONTAINS
                )
                return this
            }
        }
    }
}