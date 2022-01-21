package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.model.ShopCoreInfoResponse
import com.tokopedia.sellerhome.domain.model.ShopInfoResultResponse
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoByIdUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopLocationUseCase
import com.tokopedia.sellerhome.utils.observeAwaitValue
import com.tokopedia.sellerhome.view.helper.SellerHomeLayoutHelper
import com.tokopedia.sellerhome.view.model.ShopShareDataUiModel
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTracker
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTrackerInput
import com.tokopedia.shop.common.domain.interactor.ShopQuestGeneralTrackerUseCase
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString

/**
 * Created By @ilhamsuaib on 19/03/20
 */

@ExperimentalCoroutinesApi
class SellerHomeViewModelTest {

    companion object {
        private const val DATA_KEY_CARD = "CARD"
        private const val DATA_KEY_LINE_GRAPH = "LINE_GRAPH"
        private const val DATA_KEY_PROGRESS = "PROGRESS"
        private const val DATA_KEY_POST_LIST = "POST_LIST"
        private const val DATA_KEY_CAROUSEL = "CAROUSEL"
        private const val DATA_KEY_TABLE = "TABLE"
        private const val DATA_KEY_PIE_CHART = "PIE_CHART"
        private const val DATA_KEY_BAR_CHART = "BAR_CHART"
        private const val DATA_KEY_MULTI_LINE = "MULTI_LINE"
        private const val DATA_KEY_ANNOUNCEMENT = "ANNOUNCEMENT"
        private const val DATA_KEY_RECOMMENDATION = "RECOMMENDATION"
        private const val DATA_KEY_MILESTONE = "MILESTONE"
    }

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getTickerUseCase: GetTickerUseCase

    @RelaxedMockK
    lateinit var getLayoutUseCase: GetLayoutUseCase

    @RelaxedMockK
    lateinit var getShopLocationUseCase: GetShopLocationUseCase

    @RelaxedMockK
    lateinit var getCardDataUseCase: GetCardDataUseCase

    @RelaxedMockK
    lateinit var getLineGraphDataUseCase: GetLineGraphDataUseCase

    @RelaxedMockK
    lateinit var getProgressDataUseCase: GetProgressDataUseCase

    @RelaxedMockK
    lateinit var getPostDataUseCase: GetPostDataUseCase

    @RelaxedMockK
    lateinit var getCarouselDataUseCase: GetCarouselDataUseCase

    @RelaxedMockK
    lateinit var getTableDataUseCase: GetTableDataUseCase

    @RelaxedMockK
    lateinit var getPieChartDataUseCase: GetPieChartDataUseCase

    @RelaxedMockK
    lateinit var getBarChartDataUseCase: GetBarChartDataUseCase

    @RelaxedMockK
    lateinit var getMultiLineGraphUseCase: GetMultiLineGraphUseCase

    @RelaxedMockK
    lateinit var getAnnouncementDataUseCase: GetAnnouncementDataUseCase

    @RelaxedMockK
    lateinit var getRecommendationDataUseCase: GetRecommendationDataUseCase

    @RelaxedMockK
    lateinit var getMilestoneDataUseCase: GetMilestoneDataUseCase

    @RelaxedMockK
    lateinit var getShopInfoByIdUseCase: GetShopInfoByIdUseCase

    @RelaxedMockK
    lateinit var shopQuestGeneralTrackerUseCase: ShopQuestGeneralTrackerUseCase

    @RelaxedMockK
    lateinit var remoteConfig: SellerHomeRemoteConfig

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var sellerHomeLayoutHelper: SellerHomeLayoutHelper
    private lateinit var viewModel: SellerHomeViewModel
    private lateinit var dynamicParameter: DynamicParameterModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sellerHomeLayoutHelper = SellerHomeLayoutHelper(
            { getCardDataUseCase },
            { getLineGraphDataUseCase },
            { getProgressDataUseCase },
            { getPostDataUseCase },
            { getCarouselDataUseCase },
            { getTableDataUseCase },
            { getPieChartDataUseCase },
            { getBarChartDataUseCase },
            { getMultiLineGraphUseCase },
            { getAnnouncementDataUseCase },
            { getRecommendationDataUseCase },
            { getMilestoneDataUseCase },
            coroutineTestRule.dispatchers
        )

        viewModel = SellerHomeViewModel(
            { userSession },
            { getTickerUseCase },
            { getLayoutUseCase },
            { getShopLocationUseCase },
            { getCardDataUseCase },
            { getLineGraphDataUseCase },
            { getProgressDataUseCase },
            { getPostDataUseCase },
            { getCarouselDataUseCase },
            { getTableDataUseCase },
            { getPieChartDataUseCase },
            { getBarChartDataUseCase },
            { getMultiLineGraphUseCase },
            { getAnnouncementDataUseCase },
            { getRecommendationDataUseCase },
            { getMilestoneDataUseCase },
            { getShopInfoByIdUseCase },
            { shopQuestGeneralTrackerUseCase },
            { sellerHomeLayoutHelper },
            remoteConfig,
            coroutineTestRule.dispatchers
        )

        dynamicParameter = getDynamicParameter()
    }

    private fun getDynamicParameter(): DynamicParameterModel {
        return DynamicParameterModel(
            startDate = "15-07-20202",
            endDate = "21-07-20202",
            pageSource = "seller-home"
        )
    }

    @Test
    fun `get ticker should success`() = runBlocking {
        val isNewCachingEnabled = false
        val tickerList = listOf(TickerItemUiModel())
        val pageName = "seller"

        onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled)
        getTickerUseCase.params = GetTickerUseCase.createParams(pageName)

        coEvery {
            getTickerUseCase.executeOnBackground()
        } returns tickerList

        viewModel.getTicker()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getTickerUseCase.executeOnBackground()
        }

        Assertions.assertEquals(Success(tickerList), viewModel.homeTicker.value)
    }

    @Test
    fun `should failed when get tickers then throws exception`() = runBlocking {
        val throwable = RuntimeException("")
        val pageName = "seller"

        getTickerUseCase.params = GetTickerUseCase.createParams(pageName)

        coEvery {
            getTickerUseCase.executeOnBackground()
        } throws throwable

        viewModel.getTicker()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getTickerUseCase.executeOnBackground()
        }

        assert(viewModel.homeTicker.value is Fail)
    }

    @Test
    fun `get widget layout should success`() = runBlocking {
        val layoutList: List<BaseWidgetUiModel<*>> = provideCompleteSuccessWidgetLayout()
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList

        viewModel.getWidgetLayout()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        verify {
            userSession.shopId
        }

        coVerify {
            getLayoutUseCase.executeOnBackground()
        }

        Assertions.assertEquals(Success(layoutList), viewModel.widgetLayout.value)
    }

    @Test
    fun `when get widget layout and set height as 0f, should also success`() = runBlocking {
        val layoutList: List<BaseWidgetUiModel<*>> = provideCompleteSuccessWidgetLayout()
        val shopId = "123456"
        val page = "seller-home"
        val widgetHeightInDp = 0f

        val cardData = CardDataUiModel(DATA_KEY_CARD, showWidget = true)
        val lineGraphDataUiModel = LineGraphDataUiModel(DATA_KEY_LINE_GRAPH, showWidget = true)
        val progressDataUiModel =
            ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
        val postListDataUiModel = PostListDataUiModel(DATA_KEY_POST_LIST, showWidget = true)
        val carouselDataUiModel = CarouselDataUiModel(DATA_KEY_CAROUSEL, showWidget = true)
        val tableDataUiModel = TableDataUiModel(DATA_KEY_TABLE, showWidget = true)
        val pieChartDataUiModel = PieChartDataUiModel(DATA_KEY_PIE_CHART, showWidget = true)
        val barChartDataUiModel = BarChartDataUiModel(DATA_KEY_BAR_CHART, showWidget = true)
        val multiLineGraphDataUiModel =
            MultiLineGraphDataUiModel(DATA_KEY_MULTI_LINE, showWidget = true)
        val announcementDataUiModel =
            AnnouncementDataUiModel(DATA_KEY_ANNOUNCEMENT, showWidget = true)
        val recommendationDataUiModel =
            RecommendationDataUiModel(DATA_KEY_RECOMMENDATION, showWidget = true)
        val milestoneDataUiModel =
            MilestoneDataUiModel(DATA_KEY_MILESTONE, showWidget = true)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        every {
            remoteConfig.isSellerHomeDashboardNewCachingEnabled()
        } returns false

        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList

        every {
            getLayoutUseCase.isFirstLoad
        } returns true

        everyGetWidgetData_shouldSuccess(
            cardData,
            lineGraphDataUiModel,
            progressDataUiModel,
            postListDataUiModel,
            carouselDataUiModel,
            tableDataUiModel,
            pieChartDataUiModel,
            barChartDataUiModel,
            multiLineGraphDataUiModel,
            announcementDataUiModel,
            recommendationDataUiModel,
            milestoneDataUiModel
        )

        viewModel.getWidgetLayout(widgetHeightInDp)

        verify {
            userSession.shopId
        }

        coVerify {
            getLayoutUseCase.executeOnBackground()
            sellerHomeLayoutHelper.getInitialWidget(layoutList, widgetHeightInDp)
        }

        val successLayoutList = layoutList.map {
            when (it) {
                is CardWidgetUiModel -> it.apply { data = cardData }
                is LineGraphWidgetUiModel -> it.apply { data = lineGraphDataUiModel }
                is ProgressWidgetUiModel -> it.apply { data = progressDataUiModel }
                is PostListWidgetUiModel -> it.apply { data = postListDataUiModel }
                is CarouselWidgetUiModel -> it.apply { data = carouselDataUiModel }
                is TableWidgetUiModel -> it.apply { data = tableDataUiModel }
                is PieChartWidgetUiModel -> it.apply { data = pieChartDataUiModel }
                is BarChartWidgetUiModel -> it.apply { data = barChartDataUiModel }
                is MultiLineGraphWidgetUiModel -> it.apply { data = multiLineGraphDataUiModel }
                is AnnouncementWidgetUiModel -> it.apply { data = announcementDataUiModel }
                is RecommendationWidgetUiModel -> it.apply { data = recommendationDataUiModel }
                is MilestoneWidgetUiModel -> it.apply { data = milestoneDataUiModel }
                else -> it
            }
        }.map {
            it.apply {
                isLoading = false
            }
        }

        assert((viewModel.widgetLayout.value as? Success)?.data?.all { actualWidget ->
            successLayoutList.find { it.data == actualWidget.data } != null
        } == true)
    }

    @Test
    fun `given null widget height, should also success`() = coroutineTestRule.runBlockingTest {
        val layoutList: List<BaseWidgetUiModel<*>> = provideCompleteSuccessWidgetLayout()
        val isFirstLoad = true
        val cachingEnabled = true

        val cardData = CardDataUiModel(DATA_KEY_CARD, showWidget = true)
        val lineGraphDataUiModel = LineGraphDataUiModel(DATA_KEY_LINE_GRAPH, showWidget = true)
        val progressDataUiModel =
            ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
        val postListDataUiModel = PostListDataUiModel(DATA_KEY_POST_LIST, showWidget = true)
        val carouselDataUiModel = CarouselDataUiModel(DATA_KEY_CAROUSEL, showWidget = true)
        val tableDataUiModel = TableDataUiModel(DATA_KEY_TABLE, showWidget = true)
        val pieChartDataUiModel = PieChartDataUiModel(DATA_KEY_PIE_CHART, showWidget = true)
        val barChartDataUiModel = BarChartDataUiModel(DATA_KEY_BAR_CHART, showWidget = true)
        val multiLineGraphDataUiModel =
            MultiLineGraphDataUiModel(DATA_KEY_MULTI_LINE, showWidget = true)
        val announcementDataUiModel =
            AnnouncementDataUiModel(DATA_KEY_ANNOUNCEMENT, showWidget = true)
        val recommendationDataUiModel =
            RecommendationDataUiModel(DATA_KEY_RECOMMENDATION, showWidget = true)
        val milestoneDataUiModel =
            MilestoneDataUiModel(DATA_KEY_MILESTONE, showWidget = true)

        every {
            remoteConfig.isSellerHomeDashboardNewCachingEnabled()
        } returns true

        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns cachingEnabled

        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns isFirstLoad

        onGetLayoutFlow_thenReturn(layoutList)

        everyGetWidgetData_shouldSuccess(
            cardData,
            lineGraphDataUiModel,
            progressDataUiModel,
            postListDataUiModel,
            carouselDataUiModel,
            tableDataUiModel,
            pieChartDataUiModel,
            barChartDataUiModel,
            multiLineGraphDataUiModel,
            announcementDataUiModel,
            recommendationDataUiModel,
            milestoneDataUiModel
        )

        viewModel.getWidgetLayout(null)

        coVerify {
            getLayoutUseCase.executeOnBackground(any(), isFirstLoad && cachingEnabled)
            getLayoutUseCase.getResultFlow()
        }

        val successLayoutList = layoutList.map {
            when (it) {
                is CardWidgetUiModel -> it.apply { data = cardData }
                is LineGraphWidgetUiModel -> it.apply { data = lineGraphDataUiModel }
                is ProgressWidgetUiModel -> it.apply { data = progressDataUiModel }
                is PostListWidgetUiModel -> it.apply { data = postListDataUiModel }
                is CarouselWidgetUiModel -> it.apply { data = carouselDataUiModel }
                is TableWidgetUiModel -> it.apply { data = tableDataUiModel }
                is PieChartWidgetUiModel -> it.apply { data = pieChartDataUiModel }
                is BarChartWidgetUiModel -> it.apply { data = barChartDataUiModel }
                is MultiLineGraphWidgetUiModel -> it.apply { data = multiLineGraphDataUiModel }
                is AnnouncementWidgetUiModel -> it.apply { data = announcementDataUiModel }
                is RecommendationWidgetUiModel -> it.apply { data = recommendationDataUiModel }
                is MilestoneWidgetUiModel -> it.apply { data = milestoneDataUiModel }
                else -> it
            }
        }.map {
            it.apply {
                isLoading = false
            }
        }

        assert((viewModel.widgetLayout.value as? Success)?.data?.all { actualWidget ->
            successLayoutList.find { it.data == actualWidget.data } != null
        } == true)
    }

    @Test
    fun `given null widget height and second load, should also success`() {
        coroutineTestRule.runBlockingTest {
            val layoutList: List<BaseWidgetUiModel<*>> = provideCompleteSuccessWidgetLayout()
            val isFirstLoad = false
            val cachingEnabled = true

            val cardData = CardDataUiModel(DATA_KEY_CARD, showWidget = true)
            val lineGraphDataUiModel = LineGraphDataUiModel(DATA_KEY_LINE_GRAPH, showWidget = true)
            val progressDataUiModel =
                ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
            val postListDataUiModel = PostListDataUiModel(DATA_KEY_POST_LIST, showWidget = true)
            val carouselDataUiModel = CarouselDataUiModel(DATA_KEY_CAROUSEL, showWidget = true)
            val tableDataUiModel = TableDataUiModel(DATA_KEY_TABLE, showWidget = true)
            val pieChartDataUiModel = PieChartDataUiModel(DATA_KEY_PIE_CHART, showWidget = true)
            val barChartDataUiModel = BarChartDataUiModel(DATA_KEY_BAR_CHART, showWidget = true)
            val multiLineGraphDataUiModel =
                MultiLineGraphDataUiModel(DATA_KEY_MULTI_LINE, showWidget = true)
            val announcementDataUiModel =
                AnnouncementDataUiModel(DATA_KEY_ANNOUNCEMENT, showWidget = true)
            val recommendationDataUiModel =
                RecommendationDataUiModel(DATA_KEY_RECOMMENDATION, showWidget = true)
            val milestoneDataUiModel =
                MilestoneDataUiModel(DATA_KEY_MILESTONE, showWidget = true)

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns cachingEnabled

            coEvery {
                getLayoutUseCase.isFirstLoad
            } returns isFirstLoad

            onGetLayoutFlow_thenReturn(layoutList)

            everyGetWidgetData_shouldSuccess(
                cardData,
                lineGraphDataUiModel,
                progressDataUiModel,
                postListDataUiModel,
                carouselDataUiModel,
                tableDataUiModel,
                pieChartDataUiModel,
                barChartDataUiModel,
                multiLineGraphDataUiModel,
                announcementDataUiModel,
                recommendationDataUiModel,
                milestoneDataUiModel
            )

            viewModel.getWidgetLayout(null)

            coVerify {
                getLayoutUseCase.executeOnBackground(any(), isFirstLoad && cachingEnabled)
                getLayoutUseCase.getResultFlow()
            }

            val successLayoutList = layoutList.map {
                when (it) {
                    is CardWidgetUiModel -> it.apply { data = cardData }
                    is LineGraphWidgetUiModel -> it.apply { data = lineGraphDataUiModel }
                    is ProgressWidgetUiModel -> it.apply { data = progressDataUiModel }
                    is PostListWidgetUiModel -> it.apply { data = postListDataUiModel }
                    is CarouselWidgetUiModel -> it.apply { data = carouselDataUiModel }
                    is TableWidgetUiModel -> it.apply { data = tableDataUiModel }
                    is PieChartWidgetUiModel -> it.apply { data = pieChartDataUiModel }
                    is BarChartWidgetUiModel -> it.apply { data = barChartDataUiModel }
                    is MultiLineGraphWidgetUiModel -> it.apply { data = multiLineGraphDataUiModel }
                    is AnnouncementWidgetUiModel -> it.apply { data = announcementDataUiModel }
                    is RecommendationWidgetUiModel -> it.apply { data = recommendationDataUiModel }
                    is MilestoneWidgetUiModel -> it.apply { data = milestoneDataUiModel }
                    else -> it
                }
            }.map {
                it.apply {
                    isLoading = false
                }
            }

            assert((viewModel.widgetLayout.value as? Success)?.data?.all { actualWidget ->
                successLayoutList.find { it.data == actualWidget.data } != null
            } == true)
        }
    }

    @Test
    fun `given null widget height and caching disabled, should also success`() =
        coroutineTestRule.runBlockingTest {
            val layoutList: List<BaseWidgetUiModel<*>> = provideCompleteSuccessWidgetLayout()
            val isFirstLoad = true
            val cachingEnabled = false

            val cardData = CardDataUiModel(DATA_KEY_CARD, showWidget = true)
            val lineGraphDataUiModel = LineGraphDataUiModel(DATA_KEY_LINE_GRAPH, showWidget = true)
            val progressDataUiModel =
                ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
            val postListDataUiModel = PostListDataUiModel(DATA_KEY_POST_LIST, showWidget = true)
            val carouselDataUiModel = CarouselDataUiModel(DATA_KEY_CAROUSEL, showWidget = true)
            val tableDataUiModel = TableDataUiModel(DATA_KEY_TABLE, showWidget = true)
            val pieChartDataUiModel = PieChartDataUiModel(DATA_KEY_PIE_CHART, showWidget = true)
            val barChartDataUiModel = BarChartDataUiModel(DATA_KEY_BAR_CHART, showWidget = true)
            val multiLineGraphDataUiModel =
                MultiLineGraphDataUiModel(DATA_KEY_MULTI_LINE, showWidget = true)
            val announcementDataUiModel =
                AnnouncementDataUiModel(DATA_KEY_ANNOUNCEMENT, showWidget = true)
            val recommendationDataUiModel =
                RecommendationDataUiModel(DATA_KEY_RECOMMENDATION, showWidget = true)
            val milestoneDataUiModel =
                MilestoneDataUiModel(DATA_KEY_MILESTONE, showWidget = true)

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns cachingEnabled

            every {
                getLayoutUseCase.isFirstLoad
            } returns isFirstLoad

            onGetLayoutFlow_thenReturn(layoutList)

            everyGetWidgetData_shouldSuccess(
                cardData,
                lineGraphDataUiModel,
                progressDataUiModel,
                postListDataUiModel,
                carouselDataUiModel,
                tableDataUiModel,
                pieChartDataUiModel,
                barChartDataUiModel,
                multiLineGraphDataUiModel,
                announcementDataUiModel,
                recommendationDataUiModel,
                milestoneDataUiModel
            )

            viewModel.getWidgetLayout(null)

            coVerify {
                getLayoutUseCase.executeOnBackground(any(), isFirstLoad && cachingEnabled)
                getLayoutUseCase.getResultFlow()
            }

            val successLayoutList = layoutList.map {
                when (it) {
                    is CardWidgetUiModel -> it.apply { data = cardData }
                    is LineGraphWidgetUiModel -> it.apply { data = lineGraphDataUiModel }
                    is ProgressWidgetUiModel -> it.apply { data = progressDataUiModel }
                    is PostListWidgetUiModel -> it.apply { data = postListDataUiModel }
                    is CarouselWidgetUiModel -> it.apply { data = carouselDataUiModel }
                    is TableWidgetUiModel -> it.apply { data = tableDataUiModel }
                    is PieChartWidgetUiModel -> it.apply { data = pieChartDataUiModel }
                    is BarChartWidgetUiModel -> it.apply { data = barChartDataUiModel }
                    is MultiLineGraphWidgetUiModel -> it.apply { data = multiLineGraphDataUiModel }
                    is AnnouncementWidgetUiModel -> it.apply { data = announcementDataUiModel }
                    is RecommendationWidgetUiModel -> it.apply { data = recommendationDataUiModel }
                    is MilestoneWidgetUiModel -> it.apply { data = milestoneDataUiModel }
                    else -> it
                }
            }.map {
                it.apply {
                    isLoading = false
                }
            }

            assert((viewModel.widgetLayout.value as? Success)?.data?.all { actualWidget ->
                successLayoutList.find { it.data == actualWidget.data } != null
            } == true)
        }

    @Test
    fun `given null widget height, caching disabled and second load should also success`() =
        coroutineTestRule.runBlockingTest {
            val layoutList: List<BaseWidgetUiModel<*>> = provideCompleteSuccessWidgetLayout()
            val isFirstLoad = false
            val cachingEnabled = false

            val cardData = CardDataUiModel(DATA_KEY_CARD, showWidget = true)
            val lineGraphDataUiModel = LineGraphDataUiModel(DATA_KEY_LINE_GRAPH, showWidget = true)
            val progressDataUiModel =
                ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
            val postListDataUiModel = PostListDataUiModel(DATA_KEY_POST_LIST, showWidget = true)
            val carouselDataUiModel = CarouselDataUiModel(DATA_KEY_CAROUSEL, showWidget = true)
            val tableDataUiModel = TableDataUiModel(DATA_KEY_TABLE, showWidget = true)
            val pieChartDataUiModel = PieChartDataUiModel(DATA_KEY_PIE_CHART, showWidget = true)
            val barChartDataUiModel = BarChartDataUiModel(DATA_KEY_BAR_CHART, showWidget = true)
            val multiLineGraphDataUiModel =
                MultiLineGraphDataUiModel(DATA_KEY_MULTI_LINE, showWidget = true)
            val announcementDataUiModel =
                AnnouncementDataUiModel(DATA_KEY_ANNOUNCEMENT, showWidget = true)
            val recommendationDataUiModel =
                RecommendationDataUiModel(DATA_KEY_RECOMMENDATION, showWidget = true)
            val milestoneDataUiModel =
                MilestoneDataUiModel(DATA_KEY_MILESTONE, showWidget = true)

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns cachingEnabled

            every {
                getLayoutUseCase.isFirstLoad
            } returns isFirstLoad

            onGetLayoutFlow_thenReturn(layoutList)

            everyGetWidgetData_shouldSuccess(
                cardData,
                lineGraphDataUiModel,
                progressDataUiModel,
                postListDataUiModel,
                carouselDataUiModel,
                tableDataUiModel,
                pieChartDataUiModel,
                barChartDataUiModel,
                multiLineGraphDataUiModel,
                announcementDataUiModel,
                recommendationDataUiModel,
                milestoneDataUiModel
            )

            viewModel.getWidgetLayout(null)

            coVerify {
                getLayoutUseCase.executeOnBackground(any(), isFirstLoad && cachingEnabled)
                getLayoutUseCase.getResultFlow()
            }

            val successLayoutList = layoutList.map {
                when (it) {
                    is CardWidgetUiModel -> it.apply { data = cardData }
                    is LineGraphWidgetUiModel -> it.apply { data = lineGraphDataUiModel }
                    is ProgressWidgetUiModel -> it.apply { data = progressDataUiModel }
                    is PostListWidgetUiModel -> it.apply { data = postListDataUiModel }
                    is CarouselWidgetUiModel -> it.apply { data = carouselDataUiModel }
                    is TableWidgetUiModel -> it.apply { data = tableDataUiModel }
                    is PieChartWidgetUiModel -> it.apply { data = pieChartDataUiModel }
                    is BarChartWidgetUiModel -> it.apply { data = barChartDataUiModel }
                    is MultiLineGraphWidgetUiModel -> it.apply { data = multiLineGraphDataUiModel }
                    is AnnouncementWidgetUiModel -> it.apply { data = announcementDataUiModel }
                    is RecommendationWidgetUiModel -> it.apply { data = recommendationDataUiModel }
                    is MilestoneWidgetUiModel -> it.apply { data = milestoneDataUiModel }
                    else -> it
                }
            }.map {
                it.apply {
                    isLoading = false
                }
            }

            assert((viewModel.widgetLayout.value as? Success)?.data?.all { actualWidget ->
                successLayoutList.find { it.data == actualWidget.data } != null
            } == true)
        }

    @Test
    fun `when get widget layout and height param is not null, should also success`() {
        runBlocking {
            val layoutList: List<BaseWidgetUiModel<*>> = provideCompleteSuccessWidgetLayout()
            val shopId = "123456"
            val page = "seller-home"

            val cardData = CardDataUiModel(DATA_KEY_CARD, showWidget = true)
            val lineGraphDataUiModel = LineGraphDataUiModel(DATA_KEY_LINE_GRAPH, showWidget = true)
            val progressDataUiModel =
                ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
            val postListDataUiModel = PostListDataUiModel(DATA_KEY_POST_LIST, showWidget = true)
            val carouselDataUiModel = CarouselDataUiModel(DATA_KEY_CAROUSEL, showWidget = true)
            val tableDataUiModel = TableDataUiModel(DATA_KEY_TABLE, showWidget = true)
            val pieChartDataUiModel = PieChartDataUiModel(DATA_KEY_PIE_CHART, showWidget = true)
            val barChartDataUiModel = BarChartDataUiModel(DATA_KEY_BAR_CHART, showWidget = true)
            val multiLineGraphDataUiModel =
                MultiLineGraphDataUiModel(DATA_KEY_MULTI_LINE, showWidget = true)
            val announcementDataUiModel =
                AnnouncementDataUiModel(DATA_KEY_ANNOUNCEMENT, showWidget = true)
            val recommendationDataUiModel =
                RecommendationDataUiModel(DATA_KEY_RECOMMENDATION, showWidget = true)
            val milestoneDataUiModel =
                MilestoneDataUiModel(DATA_KEY_MILESTONE, showWidget = true)

            getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

            every {
                userSession.shopId
            } returns shopId

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns true
            coEvery {
                getLayoutUseCase.executeOnBackground()
            } returns layoutList
            every {
                getLayoutUseCase.isFirstLoad
            } returns true
            everyGetWidgetData_shouldSuccess(
                cardData,
                lineGraphDataUiModel,
                progressDataUiModel,
                postListDataUiModel,
                carouselDataUiModel,
                tableDataUiModel,
                pieChartDataUiModel,
                barChartDataUiModel,
                multiLineGraphDataUiModel,
                announcementDataUiModel,
                recommendationDataUiModel,
                milestoneDataUiModel
            )

            viewModel.getWidgetLayout(5000f)

            verify {
                userSession.shopId
            }
            coVerify {
                getLayoutUseCase.executeOnBackground()
            }

            val successLayoutList = layoutList.map {
                when (it) {
                    is CardWidgetUiModel -> it.apply { data = cardData }
                    is LineGraphWidgetUiModel -> it.apply { data = lineGraphDataUiModel }
                    is ProgressWidgetUiModel -> it.apply { data = progressDataUiModel }
                    is PostListWidgetUiModel -> it.apply { data = postListDataUiModel }
                    is CarouselWidgetUiModel -> it.apply { data = carouselDataUiModel }
                    is TableWidgetUiModel -> it.apply { data = tableDataUiModel }
                    is PieChartWidgetUiModel -> it.apply { data = pieChartDataUiModel }
                    is BarChartWidgetUiModel -> it.apply { data = barChartDataUiModel }
                    is MultiLineGraphWidgetUiModel -> it.apply { data = multiLineGraphDataUiModel }
                    is AnnouncementWidgetUiModel -> it.apply { data = announcementDataUiModel }
                    is RecommendationWidgetUiModel -> it.apply { data = recommendationDataUiModel }
                    is MilestoneWidgetUiModel -> it.apply { data = milestoneDataUiModel }
                    else -> it
                }
            }.map {
                it.apply {
                    isLoading = false
                }
            }

            assert((viewModel.widgetLayout.value as? Success)?.data?.all { actualWidget ->
                successLayoutList.find { it.data == actualWidget.data } != null
            } == true)
        }
    }

    @Test
    fun `when get widget layout and height param is not null and is new caching enabled, should also success`() {
        coroutineTestRule.runBlockingTest {
            val layoutList: List<BaseWidgetUiModel<*>> = provideCompleteSuccessWidgetLayout()
            val shopId = "123456"
            val page = "seller-home"

            val cardData = CardDataUiModel(DATA_KEY_CARD, showWidget = true)
            val lineGraphDataUiModel = LineGraphDataUiModel(DATA_KEY_LINE_GRAPH, showWidget = true)
            val progressDataUiModel = ProgressDataUiModel(DATA_KEY_PROGRESS, showWidget = true)
            val postListDataUiModel = PostListDataUiModel(DATA_KEY_POST_LIST, showWidget = true)
            val carouselDataUiModel = CarouselDataUiModel(DATA_KEY_CAROUSEL, showWidget = true)
            val tableDataUiModel = TableDataUiModel(DATA_KEY_TABLE, showWidget = true)
            val pieChartDataUiModel = PieChartDataUiModel(DATA_KEY_PIE_CHART, showWidget = true)
            val barChartDataUiModel = BarChartDataUiModel(DATA_KEY_BAR_CHART, showWidget = true)
            val multiLineGraphDataUiModel =
                MultiLineGraphDataUiModel(DATA_KEY_MULTI_LINE, showWidget = true)
            val announcementDataUiModel =
                AnnouncementDataUiModel(DATA_KEY_ANNOUNCEMENT, showWidget = true)
            val recommendationDataUiModel =
                RecommendationDataUiModel(DATA_KEY_RECOMMENDATION, showWidget = true)
            val milestoneDataUiModel =
                MilestoneDataUiModel(DATA_KEY_MILESTONE, showWidget = true)

            getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

            every {
                userSession.shopId
            } returns shopId

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns true

            onGetLayoutFlow_thenReturn(layoutList)

            every {
                getLayoutUseCase.isFirstLoad
            } returns true

            everyGetWidgetData_shouldSuccess(
                cardData,
                lineGraphDataUiModel,
                progressDataUiModel,
                postListDataUiModel,
                carouselDataUiModel,
                tableDataUiModel,
                pieChartDataUiModel,
                barChartDataUiModel,
                multiLineGraphDataUiModel,
                announcementDataUiModel,
                recommendationDataUiModel,
                milestoneDataUiModel
            )

            viewModel.getWidgetLayout(5000f)

            verify {
                userSession.shopId
            }
            coVerify {
                getLayoutUseCase.getResultFlow()
            }

            val successLayoutList = layoutList.map {
                when (it) {
                    is CardWidgetUiModel -> it.apply { data = cardData }
                    is LineGraphWidgetUiModel -> it.apply { data = lineGraphDataUiModel }
                    is ProgressWidgetUiModel -> it.apply { data = progressDataUiModel }
                    is PostListWidgetUiModel -> it.apply { data = postListDataUiModel }
                    is CarouselWidgetUiModel -> it.apply { data = carouselDataUiModel }
                    is TableWidgetUiModel -> it.apply { data = tableDataUiModel }
                    is PieChartWidgetUiModel -> it.apply { data = pieChartDataUiModel }
                    is BarChartWidgetUiModel -> it.apply { data = barChartDataUiModel }
                    is MultiLineGraphWidgetUiModel -> it.apply { data = multiLineGraphDataUiModel }
                    is AnnouncementWidgetUiModel -> it.apply { data = announcementDataUiModel }
                    is RecommendationWidgetUiModel -> it.apply { data = recommendationDataUiModel }
                    is MilestoneWidgetUiModel -> it.apply { data = milestoneDataUiModel }
                    else -> it
                }
            }.map {
                it.apply {
                    isLoading = false
                }
            }

            assert((viewModel.widgetLayout.value as? Success)?.data?.all { actualWidget ->
                successLayoutList.find { it.data == actualWidget.data } != null
            } == true)
        }
    }

    @Test
    fun `when get widget layout with given height and new caching enabled then throws exception should return failed result`() {
        coroutineTestRule.runBlockingTest {
            val exception = Throwable()
            val isCachingEnabled = true
            val isFirstLoad = false

            onGetIsNewCachingEnabled_thenReturn(true)

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            coEvery {
                getLayoutUseCase.getResultFlow()
            } throws exception

            coEvery {
                getLayoutUseCase.isFirstLoad
            } returns isFirstLoad

            viewModel.getWidgetLayout(5000f)

            coVerify {
                getLayoutUseCase.executeOnBackground(any(), isFirstLoad && isCachingEnabled)
            }

            coVerify {
                getLayoutUseCase.getResultFlow()
            }

            val expected = Fail(exception)
            viewModel.widgetLayout.verifyErrorEquals(expected)
        }
    }

    @Test
    fun `when get widget layout with given height and new caching disabled then throws exception should return failed result`() {
        coroutineTestRule.runBlockingTest {
            val exception = Throwable()
            val isCachingEnabled = true
            val isFirstLoad = false

            onGetIsNewCachingEnabled_thenReturn(false)

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            coEvery {
                getLayoutUseCase.executeOnBackground()
            } throws exception

            coEvery {
                getLayoutUseCase.isFirstLoad
            } returns isFirstLoad

            viewModel.getWidgetLayout(5000f)

            coVerify {
                getLayoutUseCase.executeOnBackground()
            }

            val expected = Fail(exception)
            viewModel.widgetLayout.verifyErrorEquals(expected)
        }
    }

    @Test
    fun `given new caching enabled and use case already start collecting, get layout should not being called`() =
        runBlocking {
            val layoutList: List<BaseWidgetUiModel<*>> = provideCompleteSuccessWidgetLayout()
            val shopId = "123456"
            val page = "seller-home"

            getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

            every {
                userSession.shopId
            } returns shopId

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns true
            onGetIsNewCachingEnabled_thenReturn(true)
            coEvery {
                getLayoutUseCase.executeOnBackground()
            } returns layoutList
            onGetLayoutFlow_thenReturn(layoutList)
            coEvery {
                getLayoutUseCase.isFirstLoad
            } returns true
            coEvery {
                getLayoutUseCase.collectingResult
            } returns true

            viewModel.getWidgetLayout(10f)

            assert(viewModel.widgetLayout.value == null)
        }

    @Test
    fun `given old and new caching not enabled, use case with transform flow should only called once`() {
        val layoutList = listOf<BaseWidgetUiModel<*>>()
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)
        every {
            userSession.shopId
        } returns shopId
        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns false
        onGetIsNewCachingEnabled_thenReturn(false)
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList

        viewModel.getWidgetLayout(10f)

        coVerify(exactly = 1) {
            getLayoutUseCase.executeOnBackground()
        }
    }

    @Test
    fun `get widget layout with height provided should failed`() = runBlocking {
        val throwable = MessageErrorException("error message")
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } throws throwable

        onGetIsNewCachingEnabled_thenReturn(false)

        coEvery {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true

        viewModel.getWidgetLayout(10f)

        coVerify {
            userSession.shopId
        }

        coVerify {
            getLayoutUseCase.executeOnBackground()
        }

        assert(viewModel.widgetLayout.value is Fail)
    }

    @Test
    fun `given getting cached data from use case with transform flow first time failed, should still execute use case`() =
        runBlocking {
            val throwable = MessageErrorException("error message")
            val shopId = "123456"
            val page = "seller-home"

            getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

            every {
                userSession.shopId
            } returns shopId

            coEvery {
                getLayoutUseCase.setUseCache(true)
            } throws throwable
            coEvery {
                getLayoutUseCase.executeOnBackground()
            } throws throwable

            onGetIsNewCachingEnabled_thenReturn(false)

            coEvery {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns true

            coEvery {
                getLayoutUseCase.isFirstLoad
            } returns true

            viewModel.getWidgetLayout(10f)

            coVerify {
                userSession.shopId
            }

            coVerify {
                getLayoutUseCase.executeOnBackground()
            }
        }

    @Test
    fun `get shop location then returns success result`() = runBlocking {
        val shopId = "123456"
        getShopLocationUseCase.params = GetShopLocationUseCase.getRequestParams(shopId)

        val shopLocation = ShippingLoc(13)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getShopLocationUseCase.executeOnBackground()
        } returns shopLocation

        viewModel.getShopLocation()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            userSession.shopId
        }
        coVerify {
            getShopLocationUseCase.executeOnBackground()
        }

        Assertions.assertEquals(Success(shopLocation), viewModel.shopLocation.value)
    }

    @Test
    fun `get shop location then returns failed result`() = runBlocking {
        val shopId = "123456"
        val throwable = MessageErrorException("error message")

        getShopLocationUseCase.params = GetShopLocationUseCase.getRequestParams(shopId)

        coEvery {
            userSession.shopId
        } returns shopId

        coEvery {
            getShopLocationUseCase.executeOnBackground()
        } throws throwable

        viewModel.getShopLocation()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            userSession.shopId
        }
        coVerify {
            getShopLocationUseCase.executeOnBackground()
        }

        assert(viewModel.shopLocation.value is Fail)
    }

    @Test
    fun `when get milestone widget data then return success`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(anyString())
            val result = listOf(MilestoneDataUiModel())

            coEvery {
                getMilestoneDataUseCase.executeOnBackground()
            } returns result

            viewModel.getMilestoneWidgetData(dataKeys)

            verify {
                getMilestoneDataUseCase.params = any()
            }

            val expected = Success(result)
            viewModel.milestoneWidgetData.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `when get milestone widget data then throw exception`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(anyString())
            val exception = RuntimeException("")
            getMilestoneDataUseCase.params = GetMilestoneDataUseCase.createParams(dataKeys)

            coEvery {
                getMilestoneDataUseCase.executeOnBackground()
            } throws exception

            viewModel.getMilestoneWidgetData(dataKeys)

            val expected = Fail(exception)
            viewModel.milestoneWidgetData.verifyErrorEquals(expected)
        }
    }

    @Test
    fun `when fetch shop info by id then return success`() {
        coroutineTestRule.runBlockingTest {
            val shopId = "12345"
            val shopInfo = ShopInfoResultResponse(
                coreInfo = ShopCoreInfoResponse("abc"),
                shopSnippetURL = "xyz"
            )
            every {
                userSession.userId
            } returns shopId

            coEvery {
                getShopInfoByIdUseCase.execute(anyLong())
            } returns shopInfo

            viewModel.getShopInfoById()

            val expected = Success(
                ShopShareDataUiModel(
                    shopUrl = shopInfo.coreInfo?.url.orEmpty(),
                    shopSnippetURL = shopInfo.shopSnippetURL.orEmpty()
                )
            )
            viewModel.shopShareData.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `when fetch shop info by id then return null data should still success`() {
        coroutineTestRule.runBlockingTest {
            val shopId = "12345"
            val shopInfo = ShopInfoResultResponse(
                coreInfo = null,
                shopSnippetURL = null
            )
            every {
                userSession.userId
            } returns shopId

            coEvery {
                getShopInfoByIdUseCase.execute(anyLong())
            } returns shopInfo

            viewModel.getShopInfoById()

            val expected = Success(
                ShopShareDataUiModel(
                    shopUrl = "",
                    shopSnippetURL = ""
                )
            )
            viewModel.shopShareData.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `when fetch shop info by id then throw exception`() {
        coroutineTestRule.runBlockingTest {
            val shopId = "12345"
            val exception = MessageErrorException()
            every {
                userSession.userId
            } returns shopId

            coEvery {
                getShopInfoByIdUseCase.execute(anyLong())
            } throws exception

            viewModel.getShopInfoById()

            val expected = Fail(exception)
            viewModel.shopShareData.verifyErrorEquals(expected)
        }
    }

    @Test
    fun `when send shop quest tracker then return success`() {
        coroutineTestRule.runBlockingTest {
            val response = ShopQuestGeneralTracker()
            shopQuestGeneralTrackerUseCase.params =
                ShopQuestGeneralTrackerUseCase.createRequestParams(
                    actionName = anyString(),
                    source = anyString(),
                    channel = anyString(),
                    input = ShopQuestGeneralTrackerInput(anyString())
                )
            coEvery {
                shopQuestGeneralTrackerUseCase.executeOnBackground()
            } returns response

            viewModel.sendShopShareQuestTracker(anyString())

            val expected = Success(response)
            viewModel.shopShareTracker.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `when send shop quest tracker then throw exception`() {
        coroutineTestRule.runBlockingTest {
            val exception = Exception()
            shopQuestGeneralTrackerUseCase.params =
                ShopQuestGeneralTrackerUseCase.createRequestParams(
                    actionName = anyString(),
                    source = anyString(),
                    channel = anyString(),
                    input = ShopQuestGeneralTrackerInput(anyString())
                )
            coEvery {
                shopQuestGeneralTrackerUseCase.executeOnBackground()
            } throws exception

            viewModel.sendShopShareQuestTracker(anyString())

            val expected = Fail(exception)
            viewModel.shopShareTracker.verifyErrorEquals(expected)
        }
    }

    @Test
    fun `get card widget data then returns success result`() = runBlocking {
        val dataKeys = listOf("a", "b", "c")

        val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } returns cardDataResult

        viewModel.getCardWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getCardDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(cardDataResult)
        Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
        Assertions.assertEquals(expectedResult, viewModel.cardWidgetData.value)
    }

    @Test
    fun `get card widget data then returns failed result`() = runBlocking {
        val dataKeys = listOf("a", "b", "c")

        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getCardWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        val result = viewModel.cardWidgetData.value
        assert(result is Fail)
    }

    @Test
    fun `get line graph widget data then returns success result`() = runBlocking {
        val dataKeys = listOf("x", "y", "z")

        val lineGraphDataResult =
            listOf(LineGraphDataUiModel(), LineGraphDataUiModel(), LineGraphDataUiModel())
        getLineGraphDataUseCase.params =
            GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getLineGraphDataUseCase.executeOnBackground()
        } returns lineGraphDataResult

        viewModel.getLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(lineGraphDataResult)
        Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
        Assertions.assertEquals(expectedResult, viewModel.lineGraphWidgetData.value)
    }

    @Test
    fun `get line graph widget data when new cache enabled, on first load and cache enabled then returns success result`() =
        runBlocking {
            val dataKeys = listOf("x", "y", "z")
            val lineGraphDataResult =
                listOf(LineGraphDataUiModel(), LineGraphDataUiModel(), LineGraphDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getLineGraphDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getLineGraphDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<LineGraphDataUiModel>>(replay = 1).apply {
                emit(lineGraphDataResult)
            }

            viewModel.getLineGraphWidgetData(dataKeys)

            coVerify {
                getLineGraphDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getLineGraphDataUseCase.getResultFlow()
            }

            val expectedResult = Success(lineGraphDataResult)
            Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
            viewModel.lineGraphWidgetData.verifySuccessEquals(expectedResult)
        }

    @Test
    fun `get line graph widget data when new cache enabled, on second load and cache enabled then returns success result`() =
        runBlocking {
            val dataKeys = listOf("x", "y", "z")
            val lineGraphDataResult =
                listOf(LineGraphDataUiModel(), LineGraphDataUiModel(), LineGraphDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getLineGraphDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getLineGraphDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<LineGraphDataUiModel>>(replay = 1).apply {
                emit(lineGraphDataResult)
            }

            viewModel.getLineGraphWidgetData(dataKeys)

            coVerify {
                getLineGraphDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getLineGraphDataUseCase.getResultFlow()
            }

            val expectedResult = Success(lineGraphDataResult)
            Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
            viewModel.lineGraphWidgetData.verifySuccessEquals(expectedResult)
        }

    @Test
    fun `get line graph widget data when new cache enabled, on first load and cache disabled then returns success result`() =
        runBlocking {
            val dataKeys = listOf("x", "y", "z")
            val lineGraphDataResult =
                listOf(LineGraphDataUiModel(), LineGraphDataUiModel(), LineGraphDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getLineGraphDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getLineGraphDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<LineGraphDataUiModel>>(replay = 1).apply {
                emit(lineGraphDataResult)
            }

            viewModel.getLineGraphWidgetData(dataKeys)

            coVerify {
                getLineGraphDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getLineGraphDataUseCase.getResultFlow()
            }

            val expectedResult = Success(lineGraphDataResult)
            Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
            viewModel.lineGraphWidgetData.verifySuccessEquals(expectedResult)
        }

    @Test
    fun `get line graph widget data when new cache enabled, on second load and cache disabled then returns success result`() =
        runBlocking {
            val dataKeys = listOf("x", "y", "z")
            val lineGraphDataResult =
                listOf(LineGraphDataUiModel(), LineGraphDataUiModel(), LineGraphDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getLineGraphDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getLineGraphDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<LineGraphDataUiModel>>(replay = 1).apply {
                emit(lineGraphDataResult)
            }

            viewModel.getLineGraphWidgetData(dataKeys)

            coVerify {
                getLineGraphDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getLineGraphDataUseCase.getResultFlow()
            }

            val expectedResult = Success(lineGraphDataResult)
            Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
            viewModel.lineGraphWidgetData.verifySuccessEquals(expectedResult)
        }

    @Test
    fun `get line graph widget data when new cache enabled, on second load and cache disabled then throw exception should return failed result`() =
        runBlocking {
            val dataKeys = listOf("x", "y", "z")
            val throwable = Throwable()
            val isCacheEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getLineGraphDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getLineGraphDataUseCase.getResultFlow()
            } throws throwable

            viewModel.getLineGraphWidgetData(dataKeys)

            coVerify {
                getLineGraphDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getLineGraphDataUseCase.getResultFlow()
            }

            val expectedResult = Fail(throwable)
            viewModel.lineGraphWidgetData.verifyErrorEquals(expectedResult)
        }

    @Test
    fun `get line graph widget data then returns failed result`() = runBlocking {
        val dataKeys = listOf("x", "y", "z")

        val throwable = MessageErrorException("error message")
        getLineGraphDataUseCase.params =
            GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getLineGraphDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getLineGraphWidgetData(dataKeys)

        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        assert(viewModel.lineGraphWidgetData.value is Fail)
    }

    @Test
    fun `get progress widget data from remote when cache disabled on second load then returns success result`() =
        runBlocking {
            val progressWidgetData =
                listOf(ProgressDataUiModel(), ProgressDataUiModel(), ProgressDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = false
            val dataKeys = listOf("x", "y", "z")

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                getProgressDataUseCase.isFirstLoad
            } returns isFirstLoad

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            coEvery {
                getProgressDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<ProgressDataUiModel>>(replay = 1).apply {
                emit(progressWidgetData)
            }

            viewModel.getProgressWidgetData(dataKeys)

            coVerify {
                getProgressDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getProgressDataUseCase.getResultFlow()
            }

            val expected = Success(progressWidgetData)
            viewModel.progressWidgetData.verifySuccessEquals(expected)
        }

    @Test
    fun `get progress widget data from remote when cache disabled on first load then returns success result`() =
        runBlocking {
            val progressWidgetData =
                listOf(ProgressDataUiModel(), ProgressDataUiModel(), ProgressDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = true
            val dataKeys = listOf("x", "y", "z")

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                getProgressDataUseCase.isFirstLoad
            } returns isFirstLoad

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            coEvery {
                getProgressDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<ProgressDataUiModel>>(replay = 1).apply {
                emit(progressWidgetData)
            }

            viewModel.getProgressWidgetData(dataKeys)

            coVerify {
                getProgressDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getProgressDataUseCase.getResultFlow()
            }

            val expected = Success(progressWidgetData)
            viewModel.progressWidgetData.verifySuccessEquals(expected)
        }

    @Test
    fun `get progress widget data from remote for second load then returns success result`() =
        runBlocking {
            val progressWidgetData =
                listOf(ProgressDataUiModel(), ProgressDataUiModel(), ProgressDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = false
            val dataKeys = listOf("x", "y", "z")

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                getProgressDataUseCase.isFirstLoad
            } returns isFirstLoad

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            coEvery {
                getProgressDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<ProgressDataUiModel>>(replay = 1).apply {
                emit(progressWidgetData)
            }

            viewModel.getProgressWidgetData(dataKeys)

            coVerify {
                getProgressDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getProgressDataUseCase.getResultFlow()
            }

            val expected = Success(progressWidgetData)
            viewModel.progressWidgetData.verifySuccessEquals(expected)
        }

    @Test
    fun `get progress widget data from cache then returns success result`() = runBlocking {
        val progressWidgetData =
            listOf(ProgressDataUiModel(), ProgressDataUiModel(), ProgressDataUiModel())
        val isCacheEnabled = true
        val isFirstLoad = true
        val dataKeys = listOf("x", "y", "z")

        every {
            remoteConfig.isSellerHomeDashboardNewCachingEnabled()
        } returns true

        every {
            getProgressDataUseCase.isFirstLoad
        } returns isFirstLoad

        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns isCacheEnabled

        coEvery {
            getProgressDataUseCase.getResultFlow()
        } returns MutableSharedFlow<List<ProgressDataUiModel>>(replay = 1).apply {
            emit(progressWidgetData)
        }

        viewModel.getProgressWidgetData(dataKeys)

        coVerify {
            getProgressDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
        }

        coVerify {
            getProgressDataUseCase.getResultFlow()
        }

        val expected = Success(progressWidgetData)
        viewModel.progressWidgetData.verifySuccessEquals(expected)
    }

    @Test
    fun `get progress widget data from remote then returns success result`() = runBlocking {
        val dateStr = "02-02-2020"
        val dataKeys = listOf("x", "y", "z")
        val progressDataList = listOf(
            ProgressDataUiModel(),
            ProgressDataUiModel(), ProgressDataUiModel()
        )

        getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(dateStr, dataKeys)

        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns progressDataList

        viewModel.getProgressWidgetData(dataKeys)

        coVerify {
            getProgressDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(progressDataList)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.progressWidgetData.value)
    }

    @Test
    fun `get progress widget data then returns failed result`() = runBlocking {
        val dateStr = "02-02-2020"
        val dataKeys = listOf("x", "y", "z")
        val throwable = MessageErrorException("error")

        getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(dateStr, dataKeys)

        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getProgressWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getProgressDataUseCase.executeOnBackground()
        }

        assert(viewModel.progressWidgetData.value is Fail)
    }

    @Test
    fun `should get post widget data from cache and new caching enabled then returns success result`() =
        runBlocking {
            val postDataList = listOf(PostListDataUiModel(), PostListDataUiModel())
            val dataKeys = listOf(
                TableAndPostDataKey("x", "x", 6, 3),
                TableAndPostDataKey("y", "y", 6, 3)
            )

            val isFirstLoad = true
            val isCachingEnabled = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                getPostDataUseCase.isFirstLoad
            } returns isFirstLoad

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            coEvery {
                getPostDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<PostListDataUiModel>>(replay = 1).apply {
                emit(postDataList)
            }

            viewModel.getPostWidgetData(dataKeys)

            coVerify {
                getPostDataUseCase.executeOnBackground(any(), isFirstLoad && isCachingEnabled)
            }

            coVerify {
                getPostDataUseCase.getResultFlow()
            }

            val expected = Success(postDataList)
            assert(postDataList.size == dataKeys.size)
            viewModel.postListWidgetData.verifySuccessEquals(expected)
        }

    @Test
    fun `should get post widget data from remote when first load and new caching enabled then returns success result`() =
        runBlocking {
            val postDataList = listOf(PostListDataUiModel(), PostListDataUiModel())
            val dataKeys = listOf(
                TableAndPostDataKey("x", "x", 6, 3),
                TableAndPostDataKey("y", "y", 6, 3)
            )

            val isFirstLoad = true
            val isCachingEnabled = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                getPostDataUseCase.isFirstLoad
            } returns isFirstLoad

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            coEvery {
                getPostDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<PostListDataUiModel>>(replay = 1).apply {
                emit(postDataList)
            }

            viewModel.getPostWidgetData(dataKeys)

            coVerify {
                getPostDataUseCase.executeOnBackground(any(), isFirstLoad && isCachingEnabled)
            }

            coVerify {
                getPostDataUseCase.getResultFlow()
            }

            val expected = Success(postDataList)
            assert(postDataList.size == dataKeys.size)
            viewModel.postListWidgetData.verifySuccessEquals(expected)
        }

    @Test
    fun `should get post widget data from remote when not first load and new caching enabled then returns success result`() =
        runBlocking {
            val postDataList = listOf(PostListDataUiModel(), PostListDataUiModel())
            val dataKeys = listOf(
                TableAndPostDataKey("x", "x", 6, 3),
                TableAndPostDataKey("y", "y", 6, 3)
            )

            val isFirstLoad = false
            val isCachingEnabled = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                getPostDataUseCase.isFirstLoad
            } returns isFirstLoad

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            coEvery {
                getPostDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<PostListDataUiModel>>(replay = 1).apply {
                emit(postDataList)
            }

            viewModel.getPostWidgetData(dataKeys)

            coVerify {
                getPostDataUseCase.executeOnBackground(any(), isFirstLoad && isCachingEnabled)
            }

            coVerify {
                getPostDataUseCase.getResultFlow()
            }

            val expected = Success(postDataList)
            assert(postDataList.size == dataKeys.size)
            viewModel.postListWidgetData.verifySuccessEquals(expected)
        }

    @Test
    fun `get post widget data and new caching disabled then returns success result`() =
        runBlocking {
            val dataKeys = listOf(
                TableAndPostDataKey("x", "x", 6, 3),
                TableAndPostDataKey("y", "y", 6, 3)
            )
            val postList = listOf(PostListDataUiModel(), PostListDataUiModel())

            getPostDataUseCase.params =
                GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns false

            coEvery {
                getPostDataUseCase.executeOnBackground()
            } returns postList

            viewModel.getPostWidgetData(dataKeys)

            coVerify {
                getPostDataUseCase.executeOnBackground()
            }

            val expectedResult = Success(postList)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.postListWidgetData.value)
        }

    @Test
    fun `get post widget data then returns failed result`() = runBlocking {
        val dataKeys = listOf(
            TableAndPostDataKey("x", "x", 6, 3),
            TableAndPostDataKey("y", "y", 6, 3)
        )
        val exception = MessageErrorException("error msg")

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPostDataUseCase.executeOnBackground()
        } throws exception

        viewModel.getPostWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getPostDataUseCase.executeOnBackground()
        }

        assert(viewModel.postListWidgetData.value is Fail)
    }

    @Test
    fun `get carousel widget data then returns success results`() = runBlocking {
        val dataKeys = listOf(
            anyString(),
            anyString(),
            anyString(),
            anyString()
        )
        val carouselList = listOf(
            CarouselDataUiModel(),
            CarouselDataUiModel(),
            CarouselDataUiModel(),
            CarouselDataUiModel()
        )

        getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)

        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } returns carouselList

        viewModel.getCarouselWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getCarouselDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(carouselList)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        viewModel.carouselWidgetData.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `get carousel widget data on new caching enabled, first load and cache enabled then returns success results`() =
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
            val carouselList = listOf(
                CarouselDataUiModel(),
                CarouselDataUiModel(),
                CarouselDataUiModel(),
                CarouselDataUiModel()
            )
            val isCachingEnabled = true
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            every {
                getCarouselDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getCarouselDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<CarouselDataUiModel>>(replay = 1).apply {
                emit(carouselList)
            }

            viewModel.getCarouselWidgetData(dataKeys)

            coVerify {
                getCarouselDataUseCase.executeOnBackground(any(), isFirstLoad && isCachingEnabled)
            }

            coVerify {
                getCarouselDataUseCase.getResultFlow()
            }

            val expectedResult = Success(carouselList)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            viewModel.carouselWidgetData.verifySuccessEquals(expectedResult)
        }

    @Test
    fun `get carousel widget data on new caching enabled, first load and cache disabled then returns success results`() =
        runBlocking {
            val dataKeys = listOf(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
            val carouselList = listOf(
                CarouselDataUiModel(),
                CarouselDataUiModel(),
                CarouselDataUiModel(),
                CarouselDataUiModel()
            )
            val isCachingEnabled = false
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            every {
                getCarouselDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getCarouselDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<CarouselDataUiModel>>(replay = 1).apply {
                emit(carouselList)
            }

            viewModel.getCarouselWidgetData(dataKeys)

            coVerify {
                getCarouselDataUseCase.executeOnBackground(any(), isFirstLoad && isCachingEnabled)
            }

            coVerify {
                getCarouselDataUseCase.getResultFlow()
            }

            val expectedResult = Success(carouselList)
            assert(expectedResult.data.size == dataKeys.size)
            viewModel.carouselWidgetData.verifySuccessEquals(expectedResult)
        }

    @Test
    fun `get carousel widget data on new caching enabled, on reload load and cache enabled then returns success results`() =
        runBlocking {
            val dataKeys = listOf(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
            val carouselList = listOf(
                CarouselDataUiModel(),
                CarouselDataUiModel(),
                CarouselDataUiModel(),
                CarouselDataUiModel()
            )
            val isCachingEnabled = true
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            every {
                getCarouselDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getCarouselDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<CarouselDataUiModel>>(replay = 1).apply {
                emit(carouselList)
            }

            viewModel.getCarouselWidgetData(dataKeys)

            coVerify {
                getCarouselDataUseCase.executeOnBackground(any(), isFirstLoad && isCachingEnabled)
            }

            coVerify {
                getCarouselDataUseCase.getResultFlow()
            }

            val expectedResult = Success(carouselList)
            assert(expectedResult.data.size == dataKeys.size)
            viewModel.carouselWidgetData.verifySuccessEquals(expectedResult)
        }

    @Test
    fun `get carousel widget data on new caching enabled, on reload load and cache disabled then returns success results`() {
        runBlocking {
            val dataKeys = listOf(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
            val carouselList = listOf(
                CarouselDataUiModel(),
                CarouselDataUiModel(),
                CarouselDataUiModel(),
                CarouselDataUiModel()
            )
            val isCachingEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            every {
                getCarouselDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getCarouselDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<CarouselDataUiModel>>(replay = 1).apply {
                emit(carouselList)
            }

            viewModel.getCarouselWidgetData(dataKeys)

            coVerify {
                getCarouselDataUseCase.executeOnBackground(any(), isFirstLoad && isCachingEnabled)
            }

            coVerify {
                getCarouselDataUseCase.getResultFlow()
            }

            val expectedResult = Success(carouselList)
            assert(expectedResult.data.size == dataKeys.size)
            viewModel.carouselWidgetData.verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `get carousel widget data then returns failed results`() {
        runBlocking {
            val dataKeys = listOf(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
            val throwable = MessageErrorException("error")

            getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)

            coEvery {
                getCarouselDataUseCase.executeOnBackground()
            } throws throwable

            viewModel.getCarouselWidgetData(dataKeys)

            coVerify {
                getCarouselDataUseCase.executeOnBackground()
            }

            assert(viewModel.carouselWidgetData.value is Fail)
        }
    }

    @Test
    fun `should success when get table widget data from cache on new caching enabled`() {
        runBlocking {
            val tableDataList = listOf(TableDataUiModel(), TableDataUiModel())
            val dataKeys = listOf(
                TableAndPostDataKey("x", "x", 6, 3),
                TableAndPostDataKey("y", "y", 6, 3)
            )

            val isNewCachingEnabled = true
            val isCachingEnabled = true
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns isNewCachingEnabled

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            every {
                getTableDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getTableDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<TableDataUiModel>>(replay = 1).apply {
                emit(tableDataList)
            }

            viewModel.getTableWidgetData(dataKeys)

            coVerify {
                getTableDataUseCase.executeOnBackground(any(), isCachingEnabled && isFirstLoad)
            }

            coVerify {
                getTableDataUseCase.getResultFlow()
            }

            val expected = Success(tableDataList)
            assert(dataKeys.size == tableDataList.size)
            viewModel.tableWidgetData.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `should success when get table widget data from remote on new caching enabled`() {
        runBlocking {
            val tableDataList = listOf(TableDataUiModel(), TableDataUiModel())
            val dataKeys = listOf(
                TableAndPostDataKey("x", "x", 6, 3),
                TableAndPostDataKey("y", "y", 6, 3)
            )

            val isCachingEnabled = false
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            every {
                getTableDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getTableDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<TableDataUiModel>>(replay = 1).apply {
                emit(tableDataList)
            }

            viewModel.getTableWidgetData(dataKeys)

            coVerify {
                getTableDataUseCase.executeOnBackground(any(), isCachingEnabled && isFirstLoad)
            }

            coVerify {
                getTableDataUseCase.getResultFlow()
            }

            val expected = Success(tableDataList)
            assert(dataKeys.size == tableDataList.size)
            viewModel.tableWidgetData.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `should success when get table widget data from remote at the second load on new caching enabled`() =
        runBlocking {
            val tableDataList = listOf(TableDataUiModel(), TableDataUiModel())
            val dataKeys = listOf(
                TableAndPostDataKey("x", "x", 6, 3),
                TableAndPostDataKey("y", "y", 6, 3)
            )

            val isCachingEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            every {
                getTableDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getTableDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<TableDataUiModel>>(replay = 1).apply {
                emit(tableDataList)
            }

            viewModel.getTableWidgetData(dataKeys)

            coVerify {
                getTableDataUseCase.executeOnBackground(any(), isCachingEnabled && isFirstLoad)
            }

            coVerify {
                getTableDataUseCase.getResultFlow()
            }

            val expected = Success(tableDataList)
            assert(dataKeys.size == tableDataList.size)
            viewModel.tableWidgetData.verifySuccessEquals(expected)
        }

    @Test
    fun `should success when get table widget data on caching disabled`() = runBlocking {
        val dataKeys = listOf(
            TableAndPostDataKey("x", "x", 6, 3),
            TableAndPostDataKey("y", "y", 6, 3)
        )
        val result = listOf(TableDataUiModel(), TableDataUiModel())

        getTableDataUseCase.params =
            GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        every {
            remoteConfig.isSellerHomeDashboardNewCachingEnabled()
        } returns false

        coEvery {
            getTableDataUseCase.executeOnBackground()
        } returns result

        viewModel.getTableWidgetData(dataKeys)

        coVerify {
            getTableDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        assert(expectedResult.data.size == dataKeys.size)
        viewModel.tableWidgetData.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `should failed when get table widget data`() = runBlocking {
        val dataKeys = listOf(
            TableAndPostDataKey("x", "x", 6, 3),
            TableAndPostDataKey("y", "y", 6, 3)
        )

        getTableDataUseCase.params =
            GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getTableDataUseCase.executeOnBackground()
        } throws MessageErrorException("error")

        viewModel.getTableWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getTableDataUseCase.executeOnBackground()
        }

        assert(viewModel.tableWidgetData.value is Fail)
    }

    @Test
    fun `should success when get pie chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())
        val result = listOf(PieChartDataUiModel(), PieChartDataUiModel())

        getPieChartDataUseCase.params =
            GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPieChartDataUseCase.executeOnBackground()
        } returns result

        viewModel.getPieChartWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getPieChartDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        assert(expectedResult.data.size == dataKeys.size)
        viewModel.pieChartWidgetData.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `get pie chart widget data when new cache enabled, on first load and cache enabled then return success result`() =
        runBlocking {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(PieChartDataUiModel(), PieChartDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getPieChartDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getPieChartDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<PieChartDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getPieChartWidgetData(dataKeys)

            coVerify {
                getPieChartDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getPieChartDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.pieChartWidgetData.value)
        }

    @Test
    fun `get pie chart widget data when new cache enabled, on second load and cache enabled then return success result`() =
        runBlocking {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(PieChartDataUiModel(), PieChartDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getPieChartDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getPieChartDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<PieChartDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getPieChartWidgetData(dataKeys)

            coVerify {
                getPieChartDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getPieChartDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.pieChartWidgetData.value)
        }

    @Test
    fun `get pie chart widget data when new cache enabled, on first load and cache disabled then return success result`() =
        runBlocking {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(PieChartDataUiModel(), PieChartDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getPieChartDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getPieChartDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<PieChartDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getPieChartWidgetData(dataKeys)

            coVerify {
                getPieChartDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getPieChartDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.pieChartWidgetData.value)
        }

    @Test
    fun `get pie chart widget data when new cache enabled, on second load and cache disabled then return success result`() =
        runBlocking {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(PieChartDataUiModel(), PieChartDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getPieChartDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getPieChartDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<PieChartDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getPieChartWidgetData(dataKeys)

            coVerify {
                getPieChartDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getPieChartDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.pieChartWidgetData.value)
        }

    @Test
    fun `get pie chart widget data when new cache enabled, on second load and cache disabled then throw exception should return failed result`() =
        runBlocking {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(PieChartDataUiModel(), PieChartDataUiModel())
            val throwable = Throwable()
            val isCacheEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getPieChartDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getPieChartDataUseCase.getResultFlow()
            } throws throwable

            viewModel.getPieChartWidgetData(dataKeys)

            coVerify {
                getPieChartDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getPieChartDataUseCase.getResultFlow()
            }

            val expectedResult = Fail(throwable)
            viewModel.pieChartWidgetData.verifyErrorEquals(expectedResult)
        }

    @Test
    fun `should failed when get pie chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getPieChartDataUseCase.params =
            GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPieChartDataUseCase.executeOnBackground()
        } throws MessageErrorException("error")

        viewModel.getPieChartWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getPieChartDataUseCase.executeOnBackground()
        }

        assert(viewModel.pieChartWidgetData.value is Fail)
    }

    @Test
    fun `should success when get bar chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())
        val result = listOf(BarChartDataUiModel(), BarChartDataUiModel())

        getBarChartDataUseCase.params =
            GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getBarChartDataUseCase.executeOnBackground()
        } returns result

        viewModel.getBarChartWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getBarChartDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.barChartWidgetData.value)
    }

    @Test
    fun `get bar chart widget data when new caching enabled, on first load and cache enabled then return success result`() =
        runBlocking {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(BarChartDataUiModel(), BarChartDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getBarChartDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getBarChartDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<BarChartDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getBarChartWidgetData(dataKeys)

            coVerify {
                getBarChartDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getBarChartDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.barChartWidgetData.value)
        }

    @Test
    fun `get bar chart widget data when new caching enabled, on first load and cache disabled then return success result`() =
        runBlocking {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(BarChartDataUiModel(), BarChartDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getBarChartDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getBarChartDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<BarChartDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getBarChartWidgetData(dataKeys)

            coVerify {
                getBarChartDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getBarChartDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.barChartWidgetData.value)
        }

    @Test
    fun `get bar chart widget data when new caching enabled, on second load and cache disabled then return success result`() =
        runBlocking {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(BarChartDataUiModel(), BarChartDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getBarChartDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getBarChartDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<BarChartDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getBarChartWidgetData(dataKeys)

            coVerify {
                getBarChartDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getBarChartDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.barChartWidgetData.value)
        }

    @Test
    fun `get bar chart widget data when new caching enabled, on second load and cache disabled then return failed result`() =
        runBlocking {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(BarChartDataUiModel(), BarChartDataUiModel())
            val exception = Throwable()
            val isCacheEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getBarChartDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getBarChartDataUseCase.getResultFlow()
            } throws exception

            viewModel.getBarChartWidgetData(dataKeys)

            coVerify {
                getBarChartDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getBarChartDataUseCase.getResultFlow()
            }

            val expectedResult = Fail(exception)
            Assertions.assertEquals(expectedResult, viewModel.barChartWidgetData.value)
        }

    @Test
    fun `get bar chart widget data when new caching enabled, on second load and cache enabled then return success result`() =
        runBlocking {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(BarChartDataUiModel(), BarChartDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getBarChartDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getBarChartDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<BarChartDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getBarChartWidgetData(dataKeys)

            coVerify {
                getBarChartDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getBarChartDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.barChartWidgetData.value)
        }

    @Test
    fun `should failed when get bar chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getBarChartDataUseCase.params =
            GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getBarChartDataUseCase.executeOnBackground()
        } throws MessageErrorException("error")

        viewModel.getBarChartWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getBarChartDataUseCase.executeOnBackground()
        }

        assert(viewModel.barChartWidgetData.value is Fail)
    }

    @Test
    fun `should success when get multi line graph widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())
        val result = listOf(MultiLineGraphDataUiModel(), MultiLineGraphDataUiModel())

        getMultiLineGraphUseCase.params =
            GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getMultiLineGraphUseCase.executeOnBackground()
        } returns result

        viewModel.getMultiLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getMultiLineGraphUseCase.executeOnBackground()
        }

        //number of data keys and result should same
        Assertions.assertTrue(dataKeys.size == result.size)

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.multiLineGraphWidgetData.value)
    }

    @Test
    fun `get multi line graph widget data when new cache enabled, on first load and cache enabled should return success result`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(MultiLineGraphDataUiModel(), MultiLineGraphDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getMultiLineGraphUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getMultiLineGraphUseCase.getResultFlow()
            } returns MutableSharedFlow<List<MultiLineGraphDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getMultiLineGraphWidgetData(dataKeys)

            coVerify {
                getMultiLineGraphUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getMultiLineGraphUseCase.getResultFlow()
            }

            //number of data keys and result should same
            Assertions.assertTrue(dataKeys.size == result.size)

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.multiLineGraphWidgetData.value)
        }
    }

    @Test
    fun `get multi line graph widget data when new cache enabled, on first load and cache disabled should return success result`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(MultiLineGraphDataUiModel(), MultiLineGraphDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getMultiLineGraphUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getMultiLineGraphUseCase.getResultFlow()
            } returns MutableSharedFlow<List<MultiLineGraphDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getMultiLineGraphWidgetData(dataKeys)

            coVerify {
                getMultiLineGraphUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getMultiLineGraphUseCase.getResultFlow()
            }

            //number of data keys and result should same
            Assertions.assertTrue(dataKeys.size == result.size)

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.multiLineGraphWidgetData.value)
        }
    }

    @Test
    fun `get multi line graph widget data when new cache enabled, on second load and cache enabled should return success result`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(MultiLineGraphDataUiModel(), MultiLineGraphDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getMultiLineGraphUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getMultiLineGraphUseCase.getResultFlow()
            } returns MutableSharedFlow<List<MultiLineGraphDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getMultiLineGraphWidgetData(dataKeys)

            coVerify {
                getMultiLineGraphUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getMultiLineGraphUseCase.getResultFlow()
            }

            //number of data keys and result should same
            Assertions.assertTrue(dataKeys.size == result.size)

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.multiLineGraphWidgetData.value)
        }
    }

    @Test
    fun `get multi line graph widget data when new cache enabled, on second load and cache disabled should return success result`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(anyString(), anyString())
            val result = listOf(MultiLineGraphDataUiModel(), MultiLineGraphDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getMultiLineGraphUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getMultiLineGraphUseCase.getResultFlow()
            } returns MutableSharedFlow<List<MultiLineGraphDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            viewModel.getMultiLineGraphWidgetData(dataKeys)

            coVerify {
                getMultiLineGraphUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getMultiLineGraphUseCase.getResultFlow()
            }

            //number of data keys and result should same
            Assertions.assertTrue(dataKeys.size == result.size)

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.multiLineGraphWidgetData.value)
        }
    }

    @Test
    fun `should failed when get multi line graph widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getMultiLineGraphUseCase.params =
            GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getMultiLineGraphUseCase.executeOnBackground()
        } throws RuntimeException("error")

        viewModel.getMultiLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getMultiLineGraphUseCase.executeOnBackground()
        }

        assert(viewModel.multiLineGraphWidgetData.value is Fail)
    }

    @Test
    fun `should success when get announcement widget data`() = runBlocking {
        val dataKeys = listOf(anyString())
        val result = listOf(AnnouncementDataUiModel())

        getAnnouncementDataUseCase.params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)

        coEvery {
            getAnnouncementDataUseCase.executeOnBackground()
        } returns result

        viewModel.getAnnouncementWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getAnnouncementDataUseCase.executeOnBackground()
        }

        //number of data keys and result should same
        Assertions.assertTrue(dataKeys.size == result.size)

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.announcementWidgetData.value)
    }

    @Test
    fun `given new caching enabled, on first load and cache enabled should success when get announcement widget data`() =
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(anyString())
            val result = listOf(AnnouncementDataUiModel())
            val isCachingEnabled = true
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            coEvery {
                getAnnouncementDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<AnnouncementDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            every {
                getAnnouncementDataUseCase.isFirstLoad
            } returns isFirstLoad

            viewModel.getAnnouncementWidgetData(dataKeys)

            coVerify {
                getAnnouncementDataUseCase.executeOnBackground(
                    any(),
                    isFirstLoad && isCachingEnabled
                )
            }

            coVerify {
                getAnnouncementDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.announcementWidgetData.value)
        }

    @Test
    fun `given new caching enabled, on reload and cache enabled should success when get announcement widget data`() =
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(anyString())
            val result = listOf(AnnouncementDataUiModel())
            val isCachingEnabled = true
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            coEvery {
                getAnnouncementDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<AnnouncementDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            every {
                getAnnouncementDataUseCase.isFirstLoad
            } returns isFirstLoad

            viewModel.getAnnouncementWidgetData(dataKeys)

            coVerify {
                getAnnouncementDataUseCase.executeOnBackground(
                    any(),
                    isFirstLoad && isCachingEnabled
                )
            }

            coVerify {
                getAnnouncementDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.announcementWidgetData.value)
        }

    @Test
    fun `given new caching enabled, on first load and cache disabled should success when get announcement widget data`() =
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(anyString())
            val result = listOf(AnnouncementDataUiModel())
            val isCachingEnabled = false
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            coEvery {
                getAnnouncementDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<AnnouncementDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            every {
                getAnnouncementDataUseCase.isFirstLoad
            } returns isFirstLoad

            viewModel.getAnnouncementWidgetData(dataKeys)

            coVerify {
                getAnnouncementDataUseCase.executeOnBackground(
                    any(),
                    isFirstLoad && isCachingEnabled
                )
            }

            coVerify {
                getAnnouncementDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.announcementWidgetData.value)
        }

    @Test
    fun `given new caching enabled, on second load and cache disabled should success when get announcement widget data`() =
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf(anyString())
            val result = listOf(AnnouncementDataUiModel())
            val isCachingEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCachingEnabled

            coEvery {
                getAnnouncementDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<AnnouncementDataUiModel>>(replay = 1).apply {
                emit(result)
            }

            every {
                getAnnouncementDataUseCase.isFirstLoad
            } returns isFirstLoad

            viewModel.getAnnouncementWidgetData(dataKeys)

            coVerify {
                getAnnouncementDataUseCase.executeOnBackground(
                    any(),
                    isFirstLoad && isCachingEnabled
                )
            }

            coVerify {
                getAnnouncementDataUseCase.getResultFlow()
            }

            val expectedResult = Success(result)
            Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
            Assertions.assertEquals(expectedResult, viewModel.announcementWidgetData.value)
        }

    @Test
    fun `should failed when get announcement widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getAnnouncementDataUseCase.params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)

        coEvery {
            getAnnouncementDataUseCase.executeOnBackground()
        } throws RuntimeException("error")

        viewModel.getAnnouncementWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getAnnouncementDataUseCase.executeOnBackground()
        }

        assert(viewModel.announcementWidgetData.value is Fail)
    }

    @Test
    fun `should success when get recommendation widget data`() = runBlocking {
        val dataKeys = listOf(anyString())
        val result = listOf(RecommendationDataUiModel())

        getRecommendationDataUseCase.params = GetRecommendationDataUseCase.createParams(dataKeys)

        coEvery {
            getRecommendationDataUseCase.executeOnBackground()
        } returns result

        viewModel.getRecommendationWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getRecommendationDataUseCase.executeOnBackground()
        }

        //number of data keys and result should same
        Assertions.assertTrue(dataKeys.size == result.size)

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        viewModel.recommendationWidgetData.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `should failed when get recommendation widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        val throwable = RuntimeException("error")

        getRecommendationDataUseCase.params = GetRecommendationDataUseCase.createParams(dataKeys)

        coEvery {
            getRecommendationDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getRecommendationWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getRecommendationDataUseCase.executeOnBackground()
        }

        val expected = Fail(throwable)
        viewModel.recommendationWidgetData.verifyErrorEquals(expected)
    }

    // example using get card widget data, any usecase is fine
    @Test
    fun `should execute use case two times when caching enabled and is first load`() {
        val dataKeys = listOf("a", "b", "c")

        val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true

        every {
            getCardDataUseCase.isFirstLoad
        } returns true

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } returns cardDataResult

        viewModel.getCardWidgetData(dataKeys)

        verify(exactly = 1) {
            getCardDataUseCase.setUseCache(true)
        }

        verify(exactly = 1) {
            getCardDataUseCase.setUseCache(false)
        }

        coVerify(exactly = 2) {
            getCardDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(cardDataResult)
        Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
        Assertions.assertEquals(expectedResult, viewModel.cardWidgetData.observeAwaitValue())
    }

    @Test
    fun `get card widget data when new cache enabled, on first load and cache enabled then return success result`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf("a", "b", "c")
            val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getCardDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getCardDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<CardDataUiModel>>(replay = 1).apply {
                emit(cardDataResult)
            }

            viewModel.getCardWidgetData(dataKeys)

            coVerify {
                getCardDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getCardDataUseCase.getResultFlow()
            }

            val expectedResult = Success(cardDataResult)
            Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
            viewModel.cardWidgetData.verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `get card widget data when new cache enabled, on second load and cache enabled then return success result`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf("a", "b", "c")
            val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
            val isCacheEnabled = true
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getCardDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getCardDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<CardDataUiModel>>(replay = 1).apply {
                emit(cardDataResult)
            }

            viewModel.getCardWidgetData(dataKeys)

            coVerify {
                getCardDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getCardDataUseCase.getResultFlow()
            }

            val expectedResult = Success(cardDataResult)
            Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
            viewModel.cardWidgetData.verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `get card widget data when new cache enabled, on first load and cache disabled then return success result`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf("a", "b", "c")
            val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = true

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getCardDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getCardDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<CardDataUiModel>>(replay = 1).apply {
                emit(cardDataResult)
            }

            viewModel.getCardWidgetData(dataKeys)

            coVerify {
                getCardDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getCardDataUseCase.getResultFlow()
            }

            val expectedResult = Success(cardDataResult)
            Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
            viewModel.cardWidgetData.verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `get card widget data when new cache enabled, on second load and cache disabled then return success result`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf("a", "b", "c")
            val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
            val isCacheEnabled = false
            val isFirstLoad = false

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getCardDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getCardDataUseCase.getResultFlow()
            } returns MutableSharedFlow<List<CardDataUiModel>>(replay = 1).apply {
                emit(cardDataResult)
            }

            viewModel.getCardWidgetData(dataKeys)

            coVerify {
                getCardDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getCardDataUseCase.getResultFlow()
            }

            val expectedResult = Success(cardDataResult)
            Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
            viewModel.cardWidgetData.verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `get card widget data when new cache enabled then throws exception should return failed result`() {
        coroutineTestRule.runBlockingTest {
            val dataKeys = listOf("a", "b", "c")
            val isCacheEnabled = false
            val isFirstLoad = false
            val throwable = Throwable()

            every {
                remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            } returns true

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns isCacheEnabled

            every {
                getCardDataUseCase.isFirstLoad
            } returns isFirstLoad

            coEvery {
                getCardDataUseCase.getResultFlow()
            } throws throwable

            viewModel.getCardWidgetData(dataKeys)

            coVerify {
                getCardDataUseCase.executeOnBackground(any(), isFirstLoad && isCacheEnabled)
            }

            coVerify {
                getCardDataUseCase.getResultFlow()
            }

            val expectedResult = Fail(throwable)
            viewModel.cardWidgetData.verifyErrorEquals(expectedResult)
        }
    }

    // example using get card widget data, any usecase is fine
    @Test
    fun `should still success when there is no cached data`() {
        var useCaseExecuteCount = 0
        val dataKeys = listOf("a", "b", "c")

        val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true

        every {
            getCardDataUseCase.isFirstLoad
        } returns true

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecuteCount++
            if (useCaseExecuteCount == 1) {
                throw Exception()
            } else {
                cardDataResult
            }
        }

        viewModel.getCardWidgetData(dataKeys)

        verify(exactly = 1) {
            getCardDataUseCase.setUseCache(true)
        }

        verify(exactly = 1) {
            getCardDataUseCase.setUseCache(false)
        }

        coVerify(exactly = 2) {
            getCardDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(cardDataResult)
        Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
        Assertions.assertEquals(expectedResult, viewModel.cardWidgetData.observeAwaitValue())
    }

    @Test
    fun `should not get data from cache if not first load`() {
        val dataKeys = listOf("a", "b", "c")

        val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true

        every {
            getCardDataUseCase.isFirstLoad
        } returns false

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } returns cardDataResult

        viewModel.getCardWidgetData(dataKeys)

        verify(inverse = true) {
            getCardDataUseCase.setUseCache(true)
        }

        verify(exactly = 1) {
            getCardDataUseCase.setUseCache(false)
        }

        coVerify(exactly = 1) {
            getCardDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(cardDataResult)
        Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
        Assertions.assertEquals(expectedResult, viewModel.cardWidgetData.observeAwaitValue())
    }

    @Test
    fun `given new caching enabled when getTicker at first load flow success should set homeTicker liveData success`() {
        coroutineTestRule.runBlockingTest {
            val isNewCachingEnabled = true
            val isCachingEnabled = true
            val isFirstLoad = true
            val tickerList = listOf(TickerItemUiModel())

            onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled)
            onGetIsCachingEnabled_thenReturn(isCachingEnabled)
            onGetTickerIsFirstLoad_thenReturn(isFirstLoad)
            onGetTickerListFlow_thenReturn(tickerList)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled(isFirstLoad, isCachingEnabled)

            val expectedResult = Success(listOf(TickerItemUiModel()))
            val actualResult = viewModel.homeTicker.value

            Assertions.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `given new caching enabled when getTicker at second load flow success should set homeTicker liveData success`() {
        coroutineTestRule.runBlockingTest {
            val isNewCachingEnabled = true
            val isCachingEnabled = true
            val isFirstLoad = false
            val tickerList = listOf(TickerItemUiModel())

            onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled)
            onGetIsCachingEnabled_thenReturn(isCachingEnabled)
            onGetTickerIsFirstLoad_thenReturn(isFirstLoad)
            onGetTickerListFlow_thenReturn(tickerList)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled(isFirstLoad, isCachingEnabled)

            val expectedResult = Success(listOf(TickerItemUiModel()))
            val actualResult = viewModel.homeTicker.value

            Assertions.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `given new caching enabled and caching disabled when getTicker at first load flow success should set homeTicker liveData success`() {
        coroutineTestRule.runBlockingTest {
            val isNewCachingEnabled = true
            val isCachingEnabled = false
            val isFirstLoad = true
            val tickerList = listOf(TickerItemUiModel())

            onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled)
            onGetIsCachingEnabled_thenReturn(isCachingEnabled)
            onGetTickerIsFirstLoad_thenReturn(isFirstLoad)
            onGetTickerListFlow_thenReturn(tickerList)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled(isFirstLoad, isCachingEnabled)

            val expectedResult = Success(listOf(TickerItemUiModel()))
            val actualResult = viewModel.homeTicker.value

            Assertions.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `given new caching enabled and caching disabled when getTicker at second load flow success should set homeTicker liveData success`() {
        coroutineTestRule.runBlockingTest {
            val isNewCachingEnabled = true
            val isCachingEnabled = false
            val isFirstLoad = false
            val tickerList = listOf(TickerItemUiModel())

            onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled)
            onGetIsCachingEnabled_thenReturn(isCachingEnabled)
            onGetTickerIsFirstLoad_thenReturn(isFirstLoad)
            onGetTickerListFlow_thenReturn(tickerList)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled(isFirstLoad, isCachingEnabled)

            val expectedResult = Success(listOf(TickerItemUiModel()))
            val actualResult = viewModel.homeTicker.value

            Assertions.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `given new caching enabled when getTicker twice should set homeTicker liveData with first value`() {
        coroutineTestRule.runBlockingTest {
            val isCollectingResult = true
            val isNewCachingEnabled = true
            val firstTickerList = listOf(TickerItemUiModel(message = "ticker"))
            val secondTickerList = listOf(TickerItemUiModel(message = "another ticker"))
            val isCachingEnabled = false
            val isFirstLoad = true

            onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled)
            onGetIsCachingEnabled_thenReturn(isCachingEnabled)
            onGetTickerIsFirstLoad_thenReturn(isFirstLoad)
            onGetTickerListFlow_thenReturn(firstTickerList)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled(isFirstLoad, isCachingEnabled)

            onGetCollectingResult_thenReturn(isCollectingResult)
            onGetIsCachingEnabled_thenReturn(isCachingEnabled)
            onGetTickerIsFirstLoad_thenReturn(isFirstLoad)
            onGetTickerListFlow_thenReturn(secondTickerList)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled(isFirstLoad, isCachingEnabled)

            val expectedResult = Success(firstTickerList)
            val actualResult = viewModel.homeTicker.value

            Assertions.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `given new caching enabled when getTicker flow error should set homeTicker liveData error`() {
        coroutineTestRule.runBlockingTest {
            val isNewCachingEnabled = true
            val error = IllegalStateException()
            val isCachingEnabled = false
            val isFirstLoad = true

            onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled)
            onGetTickerListFlow_thenReturn(error)
            onGetIsCachingEnabled_thenReturn(isCachingEnabled)
            onGetTickerIsFirstLoad_thenReturn(isFirstLoad)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled(isFirstLoad, isCachingEnabled)

            val expectedResult = Fail(error)
            val actualResult = viewModel.homeTicker.value

            Assertions.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `given getting widget data is success, when getting initial widgets layout, start and stop plt custom trace should not be null`() =
        runBlocking {
            val layoutList: List<BaseWidgetUiModel<*>> = listOf(
                SectionWidgetUiModel(
                    id = "section",
                    widgetType = WidgetType.SECTION,
                    title = "",
                    subtitle = "",
                    tooltip = TooltipUiModel("", "", true, listOf()),
                    tag = "",
                    appLink = "",
                    dataKey = "section",
                    ctaText = "",
                    isShowEmpty = true,
                    data = null,
                    isLoaded = false,
                    isLoading = false,
                    isFromCache = false,
                    isNeedToBeRemoved = false,
                    emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                    gridSize = 4
                ),
                CardWidgetUiModel(
                    id = DATA_KEY_CARD,
                    widgetType = WidgetType.CARD,
                    title = "",
                    subtitle = "",
                    tooltip = TooltipUiModel("", "", true, listOf()),
                    tag = "",
                    appLink = "",
                    dataKey = DATA_KEY_CARD,
                    ctaText = "",
                    gridSize = 2,
                    isShowEmpty = true,
                    data = null,
                    isLoaded = false,
                    isLoading = false,
                    isFromCache = false,
                    isNeedToBeRemoved = false,
                    emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
                ),
                CardWidgetUiModel(
                    id = DATA_KEY_CARD,
                    widgetType = WidgetType.CARD,
                    title = "",
                    subtitle = "",
                    tooltip = TooltipUiModel("", "", true, listOf()),
                    tag = "",
                    appLink = "",
                    dataKey = DATA_KEY_CARD,
                    ctaText = "",
                    gridSize = 2,
                    isShowEmpty = true,
                    data = null,
                    isLoaded = false,
                    isLoading = false,
                    isFromCache = false,
                    isNeedToBeRemoved = false,
                    emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
                ),
                CardWidgetUiModel(
                    id = DATA_KEY_CARD,
                    widgetType = WidgetType.CARD,
                    title = "",
                    subtitle = "",
                    tooltip = TooltipUiModel("", "", true, listOf()),
                    tag = "",
                    appLink = "",
                    dataKey = DATA_KEY_CARD,
                    ctaText = "",
                    gridSize = 2,
                    isShowEmpty = true,
                    data = null,
                    isLoaded = false,
                    isLoading = false,
                    isFromCache = false,
                    isNeedToBeRemoved = false,
                    emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
                )
            )
            val cardData = CardDataUiModel(DATA_KEY_CARD, showWidget = true)
            val shopId = "123456"
            val page = "seller-home"

            getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

            every {
                userSession.shopId
            } returns shopId

            every {
                remoteConfig.isSellerHomeDashboardCachingEnabled()
            } returns true
            coEvery {
                getLayoutUseCase.executeOnBackground()
            } returns layoutList
            coEvery {
                getLayoutUseCase.isFirstLoad
            } returns true
            coEvery {
                getCardDataUseCase.executeOnBackground()
            } returns listOf(cardData)

            viewModel.getWidgetLayout(120f)

            assert(viewModel.startWidgetCustomMetricTag.value != null)
            assert(viewModel.stopWidgetType.value != null)
        }

    @Test
    fun `given some widget data is empty and should be removed, when initial load, should not have included that widget`() {
        val layoutList: List<BaseWidgetUiModel<*>> = listOf(
            SectionWidgetUiModel(
                id = "section",
                widgetType = WidgetType.SECTION,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = "section",
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            AnnouncementWidgetUiModel(
                id = DATA_KEY_ANNOUNCEMENT,
                widgetType = WidgetType.ANNOUNCEMENT,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_ANNOUNCEMENT,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = true,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            SectionWidgetUiModel(
                id = "section_other",
                widgetType = WidgetType.SECTION,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = "section_other",
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            CarouselWidgetUiModel(
                id = DATA_KEY_CAROUSEL,
                widgetType = WidgetType.CAROUSEL,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_CAROUSEL,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = false,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            PostListWidgetUiModel(
                id = DATA_KEY_POST_LIST,
                widgetType = WidgetType.POST_LIST,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_POST_LIST,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                postFilter = listOf(WidgetFilterUiModel("", "", true)),
                maxData = 6,
                maxDisplay = 3
            ),
            SectionWidgetUiModel(
                id = "section_other2",
                widgetType = WidgetType.SECTION,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = "section_other2",
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            ProgressWidgetUiModel(
                id = DATA_KEY_PROGRESS,
                widgetType = WidgetType.PROGRESS,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_PROGRESS,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = false,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            )
        )

        val announcementData =
            AnnouncementDataUiModel(dataKey = DATA_KEY_ANNOUNCEMENT, showWidget = false)
        val carouselData = CarouselDataUiModel(dataKey = DATA_KEY_CAROUSEL, showWidget = true)
        val postData = PostListDataUiModel(
            dataKey = DATA_KEY_POST_LIST,
            postPagers = listOf(PostListPagerUiModel()),
            showWidget = true
        )
        val progressData = ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getAnnouncementDataUseCase.executeOnBackground()
        } returns listOf(announcementData)
        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } returns listOf(carouselData)
        coEvery {
            getPostDataUseCase.executeOnBackground()
        } returns listOf(postData)
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.any { it.id == DATA_KEY_ANNOUNCEMENT } == false)
    }

    @Test
    fun `given the previous widget needs to be removed, get initial layout should check the more previous widget for removability`() {
        val layoutList: List<BaseWidgetUiModel<*>> = listOf(
            ProgressWidgetUiModel(
                id = DATA_KEY_PROGRESS,
                widgetType = WidgetType.PROGRESS,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_PROGRESS,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = false,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = true,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            AnnouncementWidgetUiModel(
                id = DATA_KEY_ANNOUNCEMENT,
                widgetType = WidgetType.ANNOUNCEMENT,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_ANNOUNCEMENT,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = false,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
        )

        val progressData = ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
        val announcementData =
            AnnouncementDataUiModel(dataKey = DATA_KEY_ANNOUNCEMENT, showWidget = true)
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId
        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressData)
        coEvery {
            getAnnouncementDataUseCase.executeOnBackground()
        } returns listOf(announcementData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.any { it.id == DATA_KEY_PROGRESS } == false)
    }

    @Test
    fun `given widget data isShowWidget false, when get initial layout, should remove widget`() {
        val layoutList: List<BaseWidgetUiModel<*>> = listOf(
            ProgressWidgetUiModel(
                id = DATA_KEY_PROGRESS,
                widgetType = WidgetType.PROGRESS,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_PROGRESS,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
        )

        val progressData = ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = false)
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId
        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.any { it.id == DATA_KEY_PROGRESS } == false)
    }

    @Test
    fun `given widget data isShowWidget true and isShowEmpty true, when get initial layout, should not remove widget`() {
        val layoutList: List<BaseWidgetUiModel<*>> = listOf(
            ProgressWidgetUiModel(
                id = DATA_KEY_PROGRESS,
                widgetType = WidgetType.PROGRESS,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_PROGRESS,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
        )

        val progressData = ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId
        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.any { it.id == DATA_KEY_PROGRESS } == true)
    }

    @Test
    fun `given widget data isShowWidget true but isShowEmpty false and data is empty, when get initial layout, should remove widget`() {
        val layoutList: List<BaseWidgetUiModel<*>> = listOf(
            ProgressWidgetUiModel(
                id = DATA_KEY_PROGRESS,
                widgetType = WidgetType.PROGRESS,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_PROGRESS,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = false,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
        )

        val progressData = ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId
        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.any { it.id == DATA_KEY_PROGRESS } == false)
    }

    @Test
    fun `given widget data isShowWidget true but isShowEmpty false and data is not empty, when get initial layout, should not remove widget`() {
        val layoutList: List<BaseWidgetUiModel<*>> = listOf(
            ProgressWidgetUiModel(
                id = DATA_KEY_PROGRESS,
                widgetType = WidgetType.PROGRESS,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_PROGRESS,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = false,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
        )

        val progressData =
            ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, value = 1, showWidget = true)
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId
        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.any { it.id == DATA_KEY_PROGRESS } == true)
    }

    @Test
    fun `given any post list widget filter is not selected, postFilter should still getting post widget data`() {
        val layoutList: List<BaseWidgetUiModel<*>> = listOf(
            PostListWidgetUiModel(
                id = DATA_KEY_POST_LIST,
                widgetType = WidgetType.POST_LIST,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_POST_LIST,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                postFilter = listOf(WidgetFilterUiModel("", "", false)),
                maxData = 6,
                maxDisplay = 3
            )
        )

        val postData = PostListDataUiModel(
            DATA_KEY_POST_LIST,
            postPagers = listOf(PostListPagerUiModel()),
            showWidget = true
        )
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getPostDataUseCase.executeOnBackground()
        } returns listOf(postData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.any { it.id == DATA_KEY_ANNOUNCEMENT } == false)
    }

    @Test
    fun `given any table widget filter is not selected, table widget should still getting the widget data`() {
        val layoutList: List<BaseWidgetUiModel<*>> = listOf(
            TableWidgetUiModel(
                id = DATA_KEY_TABLE,
                widgetType = WidgetType.TABLE,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_TABLE,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                tableFilters = listOf(
                    WidgetFilterUiModel(
                        name = "dummy-name",
                        value = "dummy-value",
                        isSelected = false
                    )
                ),
                maxData = 6,
                maxDisplay = 3
            )
        )

        val tableData = TableDataUiModel(
            DATA_KEY_TABLE,
            dataSet = listOf(TablePageUiModel()),
            showWidget = true
        )
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns true
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getTableDataUseCase.executeOnBackground()
        } returns listOf(tableData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.any { it.id == DATA_KEY_ANNOUNCEMENT } == false)
    }

    private suspend fun onGetTickerListFlow_thenReturn(tickerList: List<TickerItemUiModel>) {
        coEvery {
            getTickerUseCase.getResultFlow()
        } returns MutableSharedFlow<List<TickerItemUiModel>>(replay = 1).apply {
            emit(tickerList)
        }
    }

    private suspend fun onGetLayoutFlow_thenReturn(widgets: List<BaseWidgetUiModel<*>>) {
        coEvery {
            getLayoutUseCase.getResultFlow()
        } returns MutableSharedFlow<List<BaseWidgetUiModel<*>>>(replay = 1).apply {
            emit(widgets)
        }
    }

    private fun onGetCollectingResult_thenReturn(isCollectingResult: Boolean) {
        coEvery {
            getTickerUseCase.collectingResult
        } returns isCollectingResult
    }

    private fun onGetTickerListFlow_thenReturn(error: Throwable) {
        coEvery {
            getTickerUseCase.getResultFlow()
        } throws error
    }

    private fun onGetIsCachingEnabled_thenReturn(cachingEnabled: Boolean) {
        every {
            remoteConfig.isSellerHomeDashboardCachingEnabled()
        } returns cachingEnabled
    }

    private fun onGetTickerIsFirstLoad_thenReturn(isFirstLoad: Boolean) {
        every {
            getTickerUseCase.isFirstLoad
        } returns isFirstLoad
    }

    private fun onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled: Boolean) {
        every {
            remoteConfig.isSellerHomeDashboardNewCachingEnabled()
        } returns isNewCachingEnabled
    }

    private fun verifyGetTickerUseCaseCalled(fistLoad: Boolean, cachingEnabled: Boolean) {
        coVerify {
            getTickerUseCase.executeOnBackground(any(), fistLoad && cachingEnabled)
        }
    }

    private fun verifyGetTickerResultFlowCalled() {
        coVerify {
            getTickerUseCase.getResultFlow()
        }
    }

    private fun provideCompleteSuccessWidgetLayout(): List<BaseWidgetUiModel<*>> {
        return listOf(
            CardWidgetUiModel(
                id = DATA_KEY_CARD,
                widgetType = WidgetType.CARD,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_CARD,
                ctaText = "",
                gridSize = 2,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            ProgressWidgetUiModel(
                id = DATA_KEY_PROGRESS,
                widgetType = WidgetType.PROGRESS,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_PROGRESS,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            LineGraphWidgetUiModel(
                id = DATA_KEY_LINE_GRAPH,
                widgetType = WidgetType.LINE_GRAPH,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_LINE_GRAPH,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            AnnouncementWidgetUiModel(
                id = DATA_KEY_ANNOUNCEMENT,
                widgetType = WidgetType.ANNOUNCEMENT,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_ANNOUNCEMENT,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            CarouselWidgetUiModel(
                id = DATA_KEY_CAROUSEL,
                widgetType = WidgetType.CAROUSEL,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_CAROUSEL,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            PostListWidgetUiModel(
                id = DATA_KEY_POST_LIST,
                widgetType = WidgetType.POST_LIST,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_POST_LIST,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                postFilter = listOf(),
                maxData = 6,
                maxDisplay = 3
            ),
            TableWidgetUiModel(
                id = DATA_KEY_TABLE,
                widgetType = WidgetType.TABLE,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_TABLE,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                tableFilters = listOf(
                    WidgetFilterUiModel(
                        name = "dummy-name",
                        value = "dummy-value",
                        isSelected = true
                    ), WidgetFilterUiModel(
                        name = "dummy-name",
                        value = "dummy-value",
                        isSelected = false
                    )
                ),
                maxData = 6,
                maxDisplay = 3
            ),
            PieChartWidgetUiModel(
                id = DATA_KEY_PIE_CHART,
                widgetType = WidgetType.PIE_CHART,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_PIE_CHART,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            BarChartWidgetUiModel(
                id = DATA_KEY_BAR_CHART,
                widgetType = WidgetType.BAR_CHART,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_BAR_CHART,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", "")
            ),
            MultiLineGraphWidgetUiModel(
                id = DATA_KEY_MULTI_LINE,
                widgetType = WidgetType.MULTI_LINE_GRAPH,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_MULTI_LINE,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                isComparePeriodeOnly = false
            ),
            RecommendationWidgetUiModel(
                id = DATA_KEY_RECOMMENDATION,
                widgetType = WidgetType.RECOMMENDATION,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_RECOMMENDATION,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
            ),
            MilestoneWidgetUiModel(
                id = DATA_KEY_MILESTONE,
                widgetType = WidgetType.MILESTONE,
                title = "",
                subtitle = "",
                tooltip = TooltipUiModel("", "", true, listOf()),
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_MILESTONE,
                ctaText = "",
                gridSize = 4,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                isNeedToBeRemoved = false,
                emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
            )
        )
    }

    private fun everyGetWidgetData_shouldSuccess(
        cardData: CardDataUiModel,
        lineGraphDataUiModel: LineGraphDataUiModel,
        progressDataUiModel: ProgressDataUiModel,
        postListDataUiModel: PostListDataUiModel,
        carouselDataUiModel: CarouselDataUiModel,
        tableDataUiModel: TableDataUiModel,
        pieChartDataUiModel: PieChartDataUiModel,
        barChartDataUiModel: BarChartDataUiModel,
        multiLineGraphDataUiModel: MultiLineGraphDataUiModel,
        announcementDataUiModel: AnnouncementDataUiModel,
        recommendationDataUiModel: RecommendationDataUiModel,
        milestoneDataUiModel: MilestoneDataUiModel
    ) {
        coEvery {
            getCardDataUseCase.executeOnBackground()
        } returns listOf(cardData)
        coEvery {
            getLineGraphDataUseCase.executeOnBackground()
        } returns listOf(lineGraphDataUiModel)
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressDataUiModel)
        coEvery {
            getPostDataUseCase.executeOnBackground()
        } returns listOf(postListDataUiModel)
        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } returns listOf(carouselDataUiModel)
        coEvery {
            getTableDataUseCase.executeOnBackground()
        } returns listOf(tableDataUiModel)
        coEvery {
            getPieChartDataUseCase.executeOnBackground()
        } returns listOf(pieChartDataUiModel)
        coEvery {
            getBarChartDataUseCase.executeOnBackground()
        } returns listOf(barChartDataUiModel)
        coEvery {
            getMultiLineGraphUseCase.executeOnBackground()
        } returns listOf(multiLineGraphDataUiModel)
        coEvery {
            getAnnouncementDataUseCase.executeOnBackground()
        } returns listOf(announcementDataUiModel)
        coEvery {
            getRecommendationDataUseCase.executeOnBackground()
        } returns listOf(recommendationDataUiModel)
        coEvery {
            getMilestoneDataUseCase.executeOnBackground()
        } returns listOf(milestoneDataUiModel)
    }
}