package com.tokopedia.sellerhome.testcase.cassava

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.stub.features.home.presentation.SellerHomeActivityStub
import com.tokopedia.sellerhome.stub.gql.GraphqlRepositoryStub
import com.tokopedia.sellerhomecommon.domain.model.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.trackingoptimizer.gson.GsonSingleton
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.Type

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

    private lateinit var graphqlRepositoryStub: GraphqlRepositoryStub

    @Before
    fun init() {
        getGraphqlRepositoryStub()

        createMockResponse()

        activityRule.launchActivity(SellerHomeActivityStub.createIntent(context))
    }

    @After
    fun after() {
        graphqlRepositoryStub.clearMocks()
    }

    private fun getGraphqlRepositoryStub() {
        graphqlRepositoryStub = GraphqlRepositoryStub.getInstance()
        graphqlRepositoryStub.clearMocks()
    }

    @Test
    fun validateWidgetsImpression() {
        val cardWidget = Pair(
            8, "tracker/seller_home/seller_home_card_widget_impression.json"
        )
        val descriptionWidget = Pair(
            2, "tracker/seller_home/seller_home_description_widget_impression.json"
        )
        val lineGraphWidget = Pair(
            1, "tracker/seller_home/seller_home_line_graph_widget_impression.json"
        )
        val postWidget = Pair(
            2, "tracker/seller_home/seller_home_post_widget_impression.json"
        )
        val progressWidget = Pair(
            1, "tracker/seller_home/seller_home_progress_bar_widget_impression.json"
        )
        val carouselWidget = Pair(
            1, "tracker/seller_home/seller_home_carousel_widget_impression.json"
        )

        actionTest {
            clearQueries()
            addQueriesToValidate(
                cardWidget.second,
                descriptionWidget.second,
                lineGraphWidget.second,
                postWidget.second,
                progressWidget.second,
                carouselWidget.second
            )
            scrollThrough(activityRule.activity, R.id.recycler_view)

            waitForTrackerSent()

            validate(
                cassavaTestRule,
                cardWidget.first,
                descriptionWidget.first,
                lineGraphWidget.first,
                postWidget.first,
                progressWidget.first,
                carouselWidget.first
            )
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

            validate(cassavaTestRule, clickCount)
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

            validate(cassavaTestRule, clickCount)
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

            validate(cassavaTestRule, clickCount)
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

            validate(cassavaTestRule, clickCount)
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

            validate(cassavaTestRule, clickCount)
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

            validate(cassavaTestRule, cardWidgetClickCount)
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

            validate(cassavaTestRule, postWidgetItemClickCount)
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

            validate(cassavaTestRule, carouselWidgetItemClickCount)
        }
    }

    private fun waitForTrackerSent() {
        Thread.sleep(1000)
    }

    private fun waitingForWidgetLoaded() {
        Thread.sleep(2000)
    }

    private fun createMockResponse() {
        createSingleMock(
            GetLayoutResponse::class.java,
            com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_layout
        )
        createSingleMock(
            GetTickerResponse::class.java,
            com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_get_ticker
        )
        createSingleMock(
            GetCardDataResponse::class.java,
            com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_card_widgets
        )
        createSingleMock(
            GetLineGraphDataResponse::class.java,
            com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_line_graph_widgets
        )
        createSingleMock(
            GetProgressDataResponse::class.java,
            com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_progressbar_widgets
        )
        createSingleMock(
            GetPostDataResponse::class.java,
            com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_post_widgets
        )
        createSingleMock(
            GetCarouselDataResponse::class.java,
            com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_carousel_widgets
        )
    }

    private fun createSingleMock(type: Type, raw: Int) {
        graphqlRepositoryStub.createMapResult(
            type,
            GsonSingleton.instance.fromJson(
                InstrumentationMockHelper.getRawString(
                    context,
                    raw
                ), type
            )
        )
    }
}