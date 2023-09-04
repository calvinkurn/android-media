package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.model.ShopCoreInfoResponse
import com.tokopedia.sellerhome.domain.model.ShopInfoResultResponse
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoByIdUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopLocationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopStateInfoUseCase
import com.tokopedia.sellerhome.utils.observeAwaitValue
import com.tokopedia.sellerhome.view.helper.SellerHomeLayoutHelper
import com.tokopedia.sellerhome.view.model.ShopShareDataUiModel
import com.tokopedia.sellerhome.view.model.ShopStateInfoUiModel
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.WidgetGridSize
import com.tokopedia.sellerhomecommon.domain.model.ParamCommonWidgetModel
import com.tokopedia.sellerhomecommon.domain.model.ParamTableWidgetModel
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.domain.usecase.GetAnnouncementDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetBarChartDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetCalendarDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetCardDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetCarouselDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetLayoutUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetLineGraphDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetMilestoneDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetMultiLineGraphUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetPieChartDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetPostDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetProgressDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetRecommendationDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetRichListDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetSellerHomeTickerUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetTableDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetUnificationDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.SubmitWidgetDismissUseCase
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarFilterDataKeyUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListPagerUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RichListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RichListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.SectionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.SubmitWidgetDismissUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TablePageUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetDismissalResultUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetEmptyStateUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetFilterUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetLayoutUiModel
import com.tokopedia.sellerhomecommon.sse.SellerHomeWidgetSSE
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTracker
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTrackerInput
import com.tokopedia.shop.common.domain.interactor.ShopQuestGeneralTrackerUseCase
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString

/**
 * Created By @ilhamsuaib on 19/03/20
 */

@Suppress("DEPRECATION")
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
        private const val DATA_KEY_UNIFICATION = "UNIFICATION"
        private const val DATA_KEY_RICH_LIST = "RICH_LIST"
    }

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getTickerUseCase: GetSellerHomeTickerUseCase

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
    lateinit var getCalendarDataUseCase: GetCalendarDataUseCase

    @RelaxedMockK
    lateinit var getUnificationDataUseCase: GetUnificationDataUseCase

    @RelaxedMockK
    lateinit var getRichListDataUseCase: GetRichListDataUseCase

    @RelaxedMockK
    lateinit var getShopInfoByIdUseCase: GetShopInfoByIdUseCase

    @RelaxedMockK
    lateinit var shopQuestGeneralTrackerUseCase: ShopQuestGeneralTrackerUseCase

    @RelaxedMockK
    lateinit var submitWidgetDismissUseCase: SubmitWidgetDismissUseCase

    @RelaxedMockK
    lateinit var getShopStateInfoUseCase: GetShopStateInfoUseCase

    @RelaxedMockK
    lateinit var widgetSse: SellerHomeWidgetSSE

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var sellerHomeLayoutHelper: SellerHomeLayoutHelper
    private lateinit var viewModel: SellerHomeViewModel
    private lateinit var dynamicParameter: ParamCommonWidgetModel

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
            { getCalendarDataUseCase },
            { getUnificationDataUseCase },
            { getRichListDataUseCase },
            { userSession },
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
            { getCalendarDataUseCase },
            { getUnificationDataUseCase },
            { getRichListDataUseCase },
            { getShopInfoByIdUseCase },
            { shopQuestGeneralTrackerUseCase },
            { submitWidgetDismissUseCase },
            { getShopStateInfoUseCase },
            { sellerHomeLayoutHelper },
            { widgetSse },
            coroutineTestRule.dispatchers
        )

        dynamicParameter = getDynamicParameter()
    }

    private fun getDynamicParameter(): ParamCommonWidgetModel {
        return ParamCommonWidgetModel(
            startDate = "15-07-20202",
            endDate = "21-07-20202",
            pageSource = "seller-home"
        )
    }

    @Test
    fun `get ticker should success`() = runBlocking {
        val tickerList = listOf(TickerItemUiModel())
        coEvery {
            getTickerUseCase.execute(any(), any(), any())
        } returns tickerList

        viewModel.getTicker()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getTickerUseCase.execute(any(), any(), any())
        }

        Assertions.assertEquals(Success(tickerList), viewModel.homeTicker.value)
    }

    @Test
    fun `when get ticker on cache enabled, should retrun success result`() {
        runTest {
            val networkException = Exception("from network")
            val cacheResult = listOf(TickerItemUiModel())

            var useCaseExecutedCount = 0
            coEvery {
                getTickerUseCase.execute(any(), any(), any())
            } coAnswers {
                useCaseExecutedCount++
                if (useCaseExecutedCount <= 1) {
                    throw networkException
                } else {
                    cacheResult
                }
            }

            viewModel.getTicker()

            coVerify(exactly = 1) {
                getTickerUseCase.execute(any(), any(), false)
            }

            coVerify(exactly = 1) {
                getTickerUseCase.execute(any(), any(), true)
            }

            coVerify(exactly = 2) {
                getTickerUseCase.execute(any(), any(), any())
            }

            val expectedResult = Success(cacheResult)
            viewModel.homeTicker.verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `get ticker when cache enabled then throw exception, should return remote exception`() {
        runTest {
            val networkException = Exception("from network")
            val cacheException = Exception("from cache")

            var useCaseExecutedCount = 0
            coEvery {
                getTickerUseCase.execute(any(), any(), any())
            } coAnswers {
                useCaseExecutedCount++
                if (useCaseExecutedCount <= 1) {
                    throw networkException
                } else {
                    throw cacheException
                }
            }

            viewModel.getTicker()

            coVerify(exactly = 1) {
                getTickerUseCase.execute(any(), any(), false)
            }

            coVerify(exactly = 1) {
                getTickerUseCase.execute(any(), any(), true)
            }

            coVerify(exactly = 2) {
                getTickerUseCase.execute(any(), any(), any())
            }

            val expectedResult = Fail(networkException)
            viewModel.homeTicker.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `should failed when get tickers then throws exception`() = runBlocking {
        val throwable = RuntimeException("")

        coEvery {
            getTickerUseCase.execute(any(), any(), any())
        } throws throwable

        viewModel.getTicker()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getTickerUseCase.execute(any(), any(), any())
        }

        assert(viewModel.homeTicker.value is Fail)
    }

    @Test
    fun `get widget layout should success`() = runBlocking {
        val widgetLayout = WidgetLayoutUiModel(
            widgetList = provideCompleteSuccessWidgetLayout()
        )
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout

        viewModel.getWidgetLayout()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        verify {
            userSession.shopId
        }

        coVerify {
            getLayoutUseCase.executeOnBackground()
        }

        Assertions.assertEquals(Success(widgetLayout), viewModel.widgetLayout.value)
        Assertions.assertEquals(widgetLayout.widgetList, viewModel.rawWidgetList)
    }

    @Test
    fun `when get widget layout and set height as 0f, should also success`() = runBlocking {
        val widgetLayout = WidgetLayoutUiModel(
            widgetList = provideCompleteSuccessWidgetLayout()
        )
        val shopId = "123456"
        val page = "seller-home"
        val widgetHeightInDp = 0f
        val isCachingEnabled = true

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
        val unificationDataUiModel =
            UnificationDataUiModel(DATA_KEY_UNIFICATION, showWidget = true)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout

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
            milestoneDataUiModel,
            unificationDataUiModel
        )

        viewModel.getWidgetLayout(widgetHeightInDp)

        verify {
            userSession.shopId
        }

        coVerify {
            getLayoutUseCase.executeOnBackground()
            sellerHomeLayoutHelper.getInitialWidget(
                widgetLayout.widgetList,
                widgetHeightInDp,
                isCachingEnabled
            )
        }

        val successLayoutList = widgetLayout.widgetList.map {
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
                is UnificationWidgetUiModel -> it.apply { data = unificationDataUiModel }
                else -> it
            }
        }.map {
            it.apply {
                isLoading = false
            }
        }

        assert((viewModel.widgetLayout.value as? Success)?.data?.widgetList?.all { actualWidget ->
            successLayoutList.find { it.data == actualWidget.data } != null
        } == true)
        Assertions.assertEquals(widgetLayout.widgetList, viewModel.rawWidgetList)
    }

    @Test
    fun `when get widget layout and height param is not null, should also success`() {
        runBlocking {
            val widgetLayout = WidgetLayoutUiModel(
                widgetList = provideCompleteSuccessWidgetLayout()
            )
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
            val unificationDataUiModel =
                UnificationDataUiModel(DATA_KEY_UNIFICATION, showWidget = true)

            getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

            every {
                userSession.shopId
            } returns shopId

            coEvery {
                getLayoutUseCase.executeOnBackground()
            } returns widgetLayout
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
                milestoneDataUiModel,
                unificationDataUiModel
            )

            viewModel.getWidgetLayout(5000f)

            verify {
                userSession.shopId
            }
            coVerify {
                getLayoutUseCase.executeOnBackground()
            }

            val successLayoutList = widgetLayout.widgetList.map {
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
                    is UnificationWidgetUiModel -> it.apply { data = unificationDataUiModel }
                    else -> it
                }
            }.map {
                it.apply {
                    isLoading = false
                }
            }

            assert((viewModel.widgetLayout.value as? Success)?.data?.widgetList?.all { actualWidget ->
                successLayoutList.find { it.data == actualWidget.data } != null
            } == true)
            Assertions.assertEquals(widgetLayout.widgetList, viewModel.rawWidgetList)
        }
    }

    @Test
    fun `when get widget layout with given height and new caching disabled then throws exception should return failed result`() {
        runTest {
            val exception = Throwable()
            val isFirstLoad = false

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
    fun `given old and use case with transform flow should only called once`() {
        val widgetLayout = WidgetLayoutUiModel(widgetList = listOf())
        val shopId = "123456"
        val page = "seller-home"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)
        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout

        viewModel.getWidgetLayout(10f)

        coVerify(exactly = 1) {
            getLayoutUseCase.executeOnBackground()
        }
        Assertions.assertEquals(widgetLayout.widgetList, viewModel.rawWidgetList)
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
    fun `get widget layout on first load with height provided should failed`() =
        runBlocking {
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
        runTest {
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
        runTest {
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
        runTest {
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
        runTest {
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
        runTest {
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
        runTest {
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
        runTest {
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
        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(
            dataKey = dataKeys,
            dynamicParam = dynamicParameter
        )

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
    fun `get line graph widget data then returns failed result`() = runBlocking {
        val dataKeys = listOf("x", "y", "z")

        val throwable = MessageErrorException("error message")
        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(
            dataKey = dataKeys,
            dynamicParam = dynamicParameter
        )

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
    fun `when get line graph from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf("x", "y", "z")

        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(
            dataKey = dataKeys,
            dynamicParam = dynamicParameter
        )
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getLineGraphDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getLineGraphWidgetData(dataKeys)

        verify(exactly = 1) {
            getLineGraphDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getLineGraphDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getLineGraphDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.lineGraphWidgetData.verifyErrorEquals(expectedResult)
    }

    @Test
    fun `get calendar widget data then returns success result`() = runBlocking {
        val dataKeys = listOf(CalendarFilterDataKeyUiModel("x"))

        val result = listOf(CalendarDataUiModel())
        getCalendarDataUseCase.params = GetCalendarDataUseCase.createParams(dataKeys)

        coEvery {
            getCalendarDataUseCase.executeOnBackground()
        } returns result

        viewModel.getCalendarWidgetData(dataKeys)

        coVerify {
            getCalendarDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        Assertions.assertTrue(dataKeys.size == expectedResult.data.size)
        Assertions.assertEquals(expectedResult, viewModel.calendarWidgetData.value)
    }

    @Test
    fun `get calendar widget data then returns failed result`() = runBlocking {
        val dataKeys = listOf(CalendarFilterDataKeyUiModel("x"))

        val throwable = MessageErrorException("error message")
        getCalendarDataUseCase.params = GetCalendarDataUseCase.createParams(dataKeys)

        coEvery {
            getCalendarDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getCalendarWidgetData(dataKeys)

        coVerify {
            getCalendarDataUseCase.executeOnBackground()
        }

        assert(viewModel.calendarWidgetData.value is Fail)
    }

    @Test
    fun `when get calendar from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf(CalendarFilterDataKeyUiModel("x"))

        getCalendarDataUseCase.params = GetCalendarDataUseCase.createParams(dataKeys)
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getCalendarDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getCalendarWidgetData(dataKeys)

        verify(exactly = 1) {
            getCalendarDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getCalendarDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getCalendarDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.calendarWidgetData.verifyErrorEquals(expectedResult)
    }

    @Test
    fun `get unification widget data then returns success result`() = runBlocking {
        val widgets = getUnificationMockData()
        val shopId = "123"

        val result = listOf(UnificationDataUiModel())

        getUnificationDataUseCase.setParam(
            shopId = shopId,
            widgets = widgets,
            dynamicParameter = dynamicParameter
        )

        coEvery {
            getUnificationDataUseCase.executeOnBackground()
        } returns result

        viewModel.getUnificationWidgetData(widgets)

        coVerify {
            getUnificationDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        Assertions.assertTrue(widgets.size == expectedResult.data.size)
        viewModel.unificationWidgetData.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `get unification widget data then returns failed result`() = runBlocking {
        val widgets = getUnificationMockData()
        val shopId = "123"
        val throwable = MessageErrorException("error message")

        getUnificationDataUseCase.setParam(
            shopId = shopId,
            widgets = widgets,
            dynamicParameter = dynamicParameter
        )

        coEvery {
            getUnificationDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getUnificationWidgetData(widgets)

        coVerify {
            getUnificationDataUseCase.executeOnBackground()
        }

        viewModel.unificationWidgetData.verifyErrorEquals(Fail(throwable))
    }

    @Test
    fun `when get unification from network and cache are failed, will return throwable from network`() {
        val widgets = getUnificationMockData()
        val shopId = "123"

        getUnificationDataUseCase.setParam(
            shopId = shopId,
            widgets = widgets,
            dynamicParameter = dynamicParameter
        )
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getUnificationDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getUnificationWidgetData(widgets)

        verify(exactly = 1) {
            getUnificationDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getUnificationDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getUnificationDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.unificationWidgetData.verifyErrorEquals(expectedResult)
    }

    @Test
    fun `get rich list widget data then returns success result`() = runBlocking {
        val widgets = getRichListMockData()
        val dataKeys = listOf(DATA_KEY_RICH_LIST)
        val shopId = "123"
        val result = listOf(RichListDataUiModel(dataKey = DATA_KEY_RICH_LIST))

        getRichListDataUseCase.params = GetRichListDataUseCase.createParam(
            dataKeys = dataKeys,
            shopId = shopId,
            pageSource = dynamicParameter.pageSource
        )

        coEvery {
            getRichListDataUseCase.executeOnBackground()
        } returns result

        viewModel.getRichListWidgetData(dataKeys)

        coVerify {
            getRichListDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        Assertions.assertTrue(widgets.size == expectedResult.data.size)
        viewModel.richListWidgetData.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `get rich list widget data then returns failed result`() = runBlocking {
        val dataKeys = listOf(DATA_KEY_RICH_LIST)
        val shopId = "123"
        val throwable = MessageErrorException("error message")

        getRichListDataUseCase.params = GetRichListDataUseCase.createParam(
            dataKeys = dataKeys,
            shopId = shopId,
            pageSource = dynamicParameter.pageSource
        )

        coEvery {
            getRichListDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getRichListWidgetData(dataKeys)

        coVerify {
            getRichListDataUseCase.executeOnBackground()
        }

        viewModel.richListWidgetData.verifyErrorEquals(Fail(throwable))
    }

    @Test
    fun `when get rich list from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf(DATA_KEY_RICH_LIST)

        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getRichListDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getRichListWidgetData(dataKeys)

        verify(exactly = 1) {
            getRichListDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getRichListDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getRichListDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.richListWidgetData.verifyErrorEquals(expectedResult)
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
    fun `when get progress from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf("x", "y", "z")
        val dateStr = "02-02-2020"

        getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(dateStr, dataKeys)
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getProgressWidgetData(dataKeys)

        verify(exactly = 1) {
            getProgressDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getProgressDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getProgressDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.progressWidgetData.verifyErrorEquals(expectedResult)
    }

    @Test
    fun `get post widget data then returns success result`() =
        runBlocking {
            val dataKeys = listOf(
                TableAndPostDataKey("x", "x", 6, 3),
                TableAndPostDataKey("y", "y", 6, 3)
            )
            val postList = listOf(PostListDataUiModel(), PostListDataUiModel())

            getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(
                dataKey = dataKeys,
                startDate = dynamicParameter.startDate,
                endDate = dynamicParameter.endDate
            )

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

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(
            dataKey = dataKeys,
            startDate = dynamicParameter.startDate,
            endDate = dynamicParameter.endDate
        )

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
    fun `when get post list from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf(
            TableAndPostDataKey("x", "x", 6, 3),
            TableAndPostDataKey("y", "y", 6, 3)
        )

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(
            dataKey = dataKeys,
            startDate = dynamicParameter.startDate,
            endDate = dynamicParameter.endDate
        )
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getPostDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getPostWidgetData(dataKeys)

        verify(exactly = 1) {
            getPostDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getPostDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getPostDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.postListWidgetData.verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when submit feedback dismissal widget should return success result`() {
        runTest {
            val param = SubmitWidgetDismissUiModel()
            val mockResult = WidgetDismissalResultUiModel()

            coEvery {
                submitWidgetDismissUseCase.execute(any())
            } returns mockResult

            viewModel.submitWidgetDismissal(param)

            coVerify {
                submitWidgetDismissUseCase.execute(param)
            }

            assert(param.action == mockResult.action)

            val expected = Success(mockResult)
            viewModel.submitWidgetDismissal.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `when submit feedback dismissal widget should return error result`() {
        runTest {
            val param = SubmitWidgetDismissUiModel(
                action = SubmitWidgetDismissUiModel.Action.DISMISS
            )
            val exception = MessageErrorException()

            coEvery {
                submitWidgetDismissUseCase.execute(any())
            } throws exception

            viewModel.submitWidgetDismissal(param)

            coVerify {
                submitWidgetDismissUseCase.execute(param)
            }

            val expectedResult = Fail(exception)
            viewModel.submitWidgetDismissal.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when get shop state info should return success result`() {
        runTest {
            val shopId = "123"
            val dataKeys = "shopStateChanged"
            val pageSource = "seller-home"

            val response = ShopStateInfoUiModel()

            coEvery {
                userSession.shopId
            } returns shopId

            coEvery {
                getShopStateInfoUseCase.executeInBackground(any(), any(), any())
            } returns response

            viewModel.getShopStateInfo()

            coVerify {
                getShopStateInfoUseCase.executeInBackground(shopId, dataKeys, pageSource)
            }

            val expected = Success(response)
            viewModel.shopStateInfo.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `when get shop state info should return error result`() {
        runTest {
            val shopId = "123"
            val dataKeys = "shopStateChanged"
            val pageSource = "seller-home"

            val throwable = RuntimeException()

            coEvery {
                userSession.shopId
            } returns shopId

            coEvery {
                getShopStateInfoUseCase.executeInBackground(any(), any(), any())
            } throws throwable

            viewModel.getShopStateInfo()

            coVerify {
                getShopStateInfoUseCase.executeInBackground(shopId, dataKeys, pageSource)
            }

            val expected = Fail(throwable)
            viewModel.shopStateInfo.verifyValueEquals(expected)
        }
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
    fun `when get carousel from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf(
            anyString(),
            anyString(),
            anyString(),
            anyString()
        )

        getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getCarouselWidgetData(dataKeys)

        verify(exactly = 1) {
            getCarouselDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getCarouselDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getCarouselDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.carouselWidgetData.verifyErrorEquals(expectedResult)
    }

    @Test
    fun `should success when get table widget data on caching disabled`() = runBlocking {
        val dataKeys = listOf(
            TableAndPostDataKey("x", "x", 6, 3),
            TableAndPostDataKey("y", "y", 6, 3)
        )
        val result = listOf(TableDataUiModel(), TableDataUiModel())

        val dynamicParam = ParamTableWidgetModel(
            startDate = dynamicParameter.startDate,
            endDate = dynamicParameter.endDate,
            pageSource = dynamicParameter.pageSource
        )
        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParam)

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

        val dynamicParam = ParamTableWidgetModel(
            startDate = dynamicParameter.startDate,
            endDate = dynamicParameter.endDate,
            pageSource = dynamicParameter.pageSource
        )
        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParam)

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
    fun `when get table data from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf(
            TableAndPostDataKey("x", "x", 6, 3),
            TableAndPostDataKey("y", "y", 6, 3)
        )

        val dynamicParam = ParamTableWidgetModel(
            startDate = dynamicParameter.startDate,
            endDate = dynamicParameter.endDate,
            pageSource = dynamicParameter.pageSource
        )
        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParam)

        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getTableDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getTableWidgetData(dataKeys)

        verify(exactly = 1) {
            getTableDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getTableDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getTableDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.tableWidgetData.verifyErrorEquals(expectedResult)
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
    fun `when get pie chart data from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf(anyString())

        getPieChartDataUseCase.params =
            GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getPieChartDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getPieChartWidgetData(dataKeys)

        verify(exactly = 1) {
            getPieChartDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getPieChartDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getPieChartDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.pieChartWidgetData.verifyErrorEquals(expectedResult)
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
    fun `when get bar chart data from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf(anyString())

        getBarChartDataUseCase.params =
            GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getBarChartDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getBarChartWidgetData(dataKeys)

        verify(exactly = 1) {
            getBarChartDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getBarChartDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getBarChartDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.barChartWidgetData.verifyErrorEquals(expectedResult)
    }

    @Test
    fun `should success when get multi line graph widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())
        val result = listOf(MultiLineGraphDataUiModel(), MultiLineGraphDataUiModel())

        getMultiLineGraphUseCase.params = GetMultiLineGraphUseCase.getRequestParams(
            dataKey = dataKeys,
            dynamicParam = dynamicParameter
        )

        coEvery {
            getMultiLineGraphUseCase.executeOnBackground()
        } returns result

        viewModel.getMultiLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getMultiLineGraphUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        Assertions.assertTrue(expectedResult.data.size == dataKeys.size)
        Assertions.assertEquals(expectedResult, viewModel.multiLineGraphWidgetData.value)
    }

    @Test
    fun `should failed when get multi line graph widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getMultiLineGraphUseCase.params = GetMultiLineGraphUseCase.getRequestParams(
            dataKey = dataKeys,
            dynamicParam = dynamicParameter
        )

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
    fun `when get multi line graph data from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf(anyString())

        getMultiLineGraphUseCase.params = GetMultiLineGraphUseCase.getRequestParams(
            dataKey = dataKeys,
            dynamicParam = dynamicParameter
        )
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getMultiLineGraphUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getMultiLineGraphWidgetData(dataKeys)

        verify(exactly = 1) {
            getMultiLineGraphUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getMultiLineGraphUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getMultiLineGraphUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.multiLineGraphWidgetData.verifyErrorEquals(expectedResult)
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
    fun `when get announcement data from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf(anyString())

        getAnnouncementDataUseCase.params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getAnnouncementDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getAnnouncementWidgetData(dataKeys)

        verify(exactly = 1) {
            getAnnouncementDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getAnnouncementDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getAnnouncementDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.announcementWidgetData.verifyErrorEquals(expectedResult)
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

    @Test
    fun `when get recommendation data from network and cache are failed, will return throwable from network`() {
        val dataKeys = listOf(anyString())

        getRecommendationDataUseCase.params = GetRecommendationDataUseCase.createParams(dataKeys)
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getRecommendationDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getRecommendationWidgetData(dataKeys)

        verify(exactly = 1) {
            getRecommendationDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getRecommendationDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getRecommendationDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.recommendationWidgetData.verifyErrorEquals(expectedResult)
    }

    // example using get card widget data, any usecase is fine
    @Test
    fun `should execute use case two times when caching enabled and is first load`() {
        val dataKeys = listOf("a", "b", "c")

        val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        
        every {
            getCardDataUseCase.isFirstLoad
        } returns true

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } returns cardDataResult

        viewModel.getCardWidgetData(dataKeys)

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

    // example using get card widget data, any usecase is fine
    @Test
    fun `should still success when there is no cached data`() {
        var useCaseExecuteCount = 0
        val dataKeys = listOf("a", "b", "c")

        val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)

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
    fun `when get card widget data should not get from cache if not first load`() {
        val dataKeys = listOf("a", "b", "c")

        val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)

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
    fun `when get card data from network and cache failed, return throwable from network result`() {
        val dataKeys = listOf("a", "b", "c")

        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        val networkException = Exception("from network")
        val cacheException = Exception("from cache")

        var useCaseExecutedCount = 0
        coEvery {
            getCardDataUseCase.executeOnBackground()
        } coAnswers {
            useCaseExecutedCount++
            if (useCaseExecutedCount <= 1) {
                throw networkException
            } else {
                throw cacheException
            }
        }

        viewModel.getCardWidgetData(dataKeys)

        verify(exactly = 1) {
            getCardDataUseCase.setUseCache(false)
        }

        verify(exactly = 1) {
            getCardDataUseCase.setUseCache(true)
        }

        coVerify(exactly = 2) {
            getCardDataUseCase.executeOnBackground()
        }

        val expectedResult = Fail(networkException)
        viewModel.cardWidgetData.verifyErrorEquals(expectedResult)
    }

    @Test
    fun `given getting widget data is success, when getting initial widgets layout, start and stop plt custom trace should not be null`() {
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
            val widgetLayout = WidgetLayoutUiModel(widgetList = layoutList)

            getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

            every {
                userSession.shopId
            } returns shopId

            coEvery {
                getLayoutUseCase.executeOnBackground()
            } returns widgetLayout
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
        val widgetLayout = WidgetLayoutUiModel(widgetList = layoutList)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout
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

        assert((viewModel.widgetLayout.value as? Success)?.data?.widgetList?.any { it.id == DATA_KEY_ANNOUNCEMENT } == false)
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
        val widgetLayout = WidgetLayoutUiModel(widgetList = layoutList)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout
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

        assert((viewModel.widgetLayout.value as? Success)?.data?.widgetList?.any { it.id == DATA_KEY_PROGRESS } == false)
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
        val widgetLayout = WidgetLayoutUiModel(widgetList = layoutList)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.widgetList?.any { it.id == DATA_KEY_PROGRESS } == false)
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
        val widgetLayout = WidgetLayoutUiModel(widgetList = layoutList)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.widgetList?.any { it.id == DATA_KEY_PROGRESS } == true)
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
        val widgetLayout = WidgetLayoutUiModel(widgetList = layoutList)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.widgetList?.any { it.id == DATA_KEY_PROGRESS } == false)
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
        val widgetLayout = WidgetLayoutUiModel(widgetList = layoutList)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId
        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns listOf(progressData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.widgetList?.any { it.id == DATA_KEY_PROGRESS } == true)
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
        val widgetLayout = WidgetLayoutUiModel(widgetList = layoutList)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getPostDataUseCase.executeOnBackground()
        } returns listOf(postData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.widgetList?.any { it.id == DATA_KEY_ANNOUNCEMENT } == false)
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
        val widgetLayout = WidgetLayoutUiModel(widgetList = layoutList)

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, page)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns widgetLayout
        coEvery {
            getLayoutUseCase.isFirstLoad
        } returns true
        coEvery {
            getTableDataUseCase.executeOnBackground()
        } returns listOf(tableData)

        viewModel.getWidgetLayout(1000f)

        assert((viewModel.widgetLayout.value as? Success)?.data?.widgetList?.any { it.id == DATA_KEY_ANNOUNCEMENT } == false)
    }

    @Test
    fun `on stop sse`() {
        viewModel.stopSSE()
        verify { widgetSse.closeSse() }
    }

    @Test
    fun `on start SSE when realTimeDataKeys is not empty then set card widgets data liveData`() {
        runTest {
            val page = "home"
            val param = listOf("cardWidget")
            val cardData = CardDataUiModel()
            val mockResponse = listOf(cardData)
            every { widgetSse.listen() } returns flow { flowOf(mockResponse) }

            viewModel.startSse(param)

            verify { widgetSse.connect(page, param) }
            coVerify { widgetSse.listen() }

            widgetSse.listen().collect {
                viewModel.cardWidgetData.verifySuccessEquals(Success(listOf(cardData)))
            }
        }
    }

    @Test
    fun `on start SSE when realTimeDataKeys is not empty and sse is not connected then set card widgets data liveData`() {
        runTest {
            val page = "home"
            val param = listOf("cardWidget")
            val cardData = CardDataUiModel()
            val mockResponse = listOf(cardData)
            every { widgetSse.isConnected() } returns false
            every { widgetSse.listen() } returns flow { flowOf(mockResponse) }

            viewModel.startSse(param)

            verify { widgetSse.isConnected() }
            verify { widgetSse.connect(page, param) }
            coVerify { widgetSse.listen() }

            widgetSse.listen().collect {
                viewModel.cardWidgetData.verifySuccessEquals(Success(listOf(cardData)))
            }
        }
    }

    @Test
    fun `on start SSE when realTimeDataKeys is not empty then set milestone widgets data liveData`() {
        runTest {
            val page = "home"
            val param = listOf("milestoneWidget")
            val milestoneData = MilestoneDataUiModel()
            val mockResponse = listOf(milestoneData)
            every { widgetSse.listen() } returns flow { flowOf(mockResponse) }

            viewModel.startSse(param)

            verify { widgetSse.connect(page, param) }
            coVerify { widgetSse.listen() }
            widgetSse.listen().collect {
                viewModel.milestoneWidgetData.verifySuccessEquals(Success(listOf(milestoneData)))
            }
        }
    }

    @Test
    fun `on start SSE when realTimeDataKeys is not empty and sse is not connected then set milestone widgets data liveData`() {
        runTest {
            val page = "home"
            val param = listOf("milestoneWidget")
            val milestoneData = MilestoneDataUiModel()
            val mockResponse = listOf(milestoneData)
            every { widgetSse.isConnected() } returns false
            every { widgetSse.listen() } returns flow { flowOf(mockResponse) }

            viewModel.startSse(param)

            verify { widgetSse.isConnected() }
            verify { widgetSse.connect(page, param) }
            coVerify { widgetSse.listen() }
            widgetSse.listen().collect {
                viewModel.milestoneWidgetData.verifySuccessEquals(Success(listOf(milestoneData)))
            }
        }
    }

    @Test
    fun `on start SSE when realTimeDataKeys is empty && sse is connected`() {
        val param = emptyList<String>()
        every { widgetSse.isConnected() } returns true

        viewModel.startSse(param)

        verify(inverse = true) { widgetSse.connect(anyString(), param) }
        verify(inverse = true) { widgetSse.listen() }
    }

    @Test
    fun `on start SSE when realTimeDataKeys is empty && sse is not connected`() {
        val param = emptyList<String>()
        every { widgetSse.isConnected() } returns false

        viewModel.startSse(param)

        verify(inverse = true) { widgetSse.connect(anyString(), param) }
        verify(inverse = true) { widgetSse.listen() }
    }

    @Test
    fun `on start SSE when realTimeDataKeys is not empty && sse is connected`() {
        val param = listOf("cardWidget")
        every { widgetSse.isConnected() } returns true

        viewModel.startSse(param)

        verify { widgetSse.isConnected() }
        verify(inverse = true) { widgetSse.connect(anyString(), param) }
        verify(inverse = true) { widgetSse.listen() }
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
                isComparePeriodOnly = false
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
        milestoneDataUiModel: MilestoneDataUiModel,
        unificationDataUiModel: UnificationDataUiModel
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
        coEvery {
            getUnificationDataUseCase.executeOnBackground()
        } returns listOf(unificationDataUiModel)
    }

    private fun getUnificationMockData(): List<UnificationWidgetUiModel> {
        return listOf(
            UnificationWidgetUiModel(
                id = "123",
                widgetType = WidgetType.UNIFICATION,
                title = "unification",
                subtitle = "",
                tooltip = null,
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_UNIFICATION,
                ctaText = "",
                gridSize = WidgetGridSize.GRID_SIZE_1,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                emptyState = WidgetEmptyStateUiModel()
            )
        )
    }

    private fun getRichListMockData(): List<RichListWidgetUiModel> {
        return listOf(
            RichListWidgetUiModel(
                id = "123",
                widgetType = WidgetType.RICH_LIST,
                title = "richlist",
                subtitle = "",
                tooltip = null,
                tag = "",
                appLink = "",
                dataKey = DATA_KEY_RICH_LIST,
                ctaText = "",
                gridSize = WidgetGridSize.GRID_SIZE_1,
                isShowEmpty = true,
                data = null,
                isLoaded = false,
                isLoading = false,
                isFromCache = false,
                emptyState = WidgetEmptyStateUiModel()
            )
        )
    }
}
