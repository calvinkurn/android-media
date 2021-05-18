package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.usecase.GetShopLocationUseCase
import com.tokopedia.sellerhome.utils.observeAwaitValue
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
    lateinit var remoteConfig: SellerHomeRemoteConfig

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: SellerHomeViewModel
    private lateinit var dynamicParameter: DynamicParameterModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = SellerHomeViewModel(
                dagger.Lazy { userSession },
                dagger.Lazy { getTickerUseCase },
                dagger.Lazy { getLayoutUseCase },
                dagger.Lazy { getShopLocationUseCase },
                dagger.Lazy { getCardDataUseCase },
                dagger.Lazy { getLineGraphDataUseCase },
                dagger.Lazy { getProgressDataUseCase },
                dagger.Lazy { getPostDataUseCase },
                dagger.Lazy { getCarouselDataUseCase },
                dagger.Lazy { getTableDataUseCase },
                dagger.Lazy { getPieChartDataUseCase },
                dagger.Lazy { getBarChartDataUseCase },
                dagger.Lazy { getMultiLineGraphUseCase },
                dagger.Lazy { getAnnouncementDataUseCase },
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

        assertEquals(Success(tickerList), viewModel.homeTicker.value)
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
        coVerify {
            userSession.shopId
        }
        coVerify {
            getLayoutUseCase.executeOnBackground()
        }

        assertEquals(Success(layoutList), viewModel.widgetLayout.value)
    }

    @Test
    fun `when get widget layout and height param is not null, should also success`() = runBlocking {

        val layoutList: List<BaseWidgetUiModel<*>> = provideCompleteSuccessWidgetLayout()
        val shopId = "123456"
        val page = "seller-home"

        val cardData = CardDataUiModel(DATA_KEY_CARD, showWidget = true)
        val lineGraphDataUiModel = LineGraphDataUiModel(DATA_KEY_LINE_GRAPH, showWidget = true)
        val progressDataUiModel = ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
        val postListDataUiModel = PostListDataUiModel(DATA_KEY_POST_LIST, showWidget = true)
        val carouselDataUiModel = CarouselDataUiModel(DATA_KEY_CAROUSEL, showWidget = true)
        val tableDataUiModel = TableDataUiModel(DATA_KEY_TABLE, showWidget = true)
        val pieChartDataUiModel = PieChartDataUiModel(DATA_KEY_PIE_CHART, showWidget = true)
        val barChartDataUiModel = BarChartDataUiModel(DATA_KEY_BAR_CHART, showWidget = true)
        val multiLineGraphDataUiModel = MultiLineGraphDataUiModel(DATA_KEY_MULTI_LINE, showWidget = true)
        val announcementDataUiModel = AnnouncementDataUiModel(DATA_KEY_ANNOUNCEMENT, showWidget = true)

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
        everyGetWidgetData_shouldSuccess(cardData, lineGraphDataUiModel, progressDataUiModel, postListDataUiModel,
                carouselDataUiModel, tableDataUiModel, pieChartDataUiModel, barChartDataUiModel, multiLineGraphDataUiModel, announcementDataUiModel)

        viewModel.getWidgetLayout(5000f)

        coVerify {
            userSession.shopId
        }
        coVerify {
            getLayoutUseCase.executeOnBackground()
        }

        val successLayoutList = layoutList.map {
            when(it) {
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
    fun `when get widget layout and height param is not null and is new caching enabled, should also success`() = runBlocking {

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
        val multiLineGraphDataUiModel = MultiLineGraphDataUiModel(DATA_KEY_MULTI_LINE, showWidget = true)
        val announcementDataUiModel = AnnouncementDataUiModel(DATA_KEY_ANNOUNCEMENT, showWidget = true)

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
        everyGetWidgetData_shouldSuccess(cardData, lineGraphDataUiModel, progressDataUiModel, postListDataUiModel,
                carouselDataUiModel, tableDataUiModel, pieChartDataUiModel, barChartDataUiModel, multiLineGraphDataUiModel, announcementDataUiModel)

        viewModel.getWidgetLayout(5000f)

        coVerify {
            userSession.shopId
        }
        coVerify {
            getLayoutUseCase.getResultFlow()
        }

        val successLayoutList = layoutList.map {
            when(it) {
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
    fun `given new caching enabled and use case already start collecting, get layout should not being called`() = runBlocking {
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
    fun `get widget layout should failed`() = runBlocking {
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

        viewModel.getWidgetLayout()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            userSession.shopId
        }

        coVerify {
            getLayoutUseCase.executeOnBackground()
        }

        assert(viewModel.widgetLayout.value is Fail)
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
    fun `given getting cached data from use case with transform flow first time failed, should still execute use case`() = runBlocking {
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

        assertEquals(Success(shopLocation), viewModel.shopLocation.value)
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
        assertTrue(dataKeys.size == expectedResult.data.size)
        assertEquals(expectedResult, viewModel.cardWidgetData.value)
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

        val lineGraphDataResult = listOf(LineGraphDataUiModel(), LineGraphDataUiModel(), LineGraphDataUiModel())
        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getLineGraphDataUseCase.executeOnBackground()
        } returns lineGraphDataResult

        viewModel.getLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(lineGraphDataResult)
        assertTrue(dataKeys.size == expectedResult.data.size)
        assertEquals(expectedResult, viewModel.lineGraphWidgetData.value)
    }

    @Test
    fun `get line graph widget data then returns failed result`() = runBlocking {
        val dataKeys = listOf("x", "y", "z")

        val throwable = MessageErrorException("error message")
        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getLineGraphDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        assert(viewModel.lineGraphWidgetData.value is Fail)
    }

    @Test
    fun `get progress widget data then returns success result`() = runBlocking {
        val dateStr = "02-02-2020"
        val dataKeys = listOf("x", "y", "z")
        val progressDataList = listOf(ProgressDataUiModel(), ProgressDataUiModel(), ProgressDataUiModel())

        getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(dateStr, dataKeys)

        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns progressDataList

        viewModel.getProgressWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            getProgressDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(progressDataList)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, viewModel.progressWidgetData.value)
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
    fun `get post widget data then returns success result`() = runBlocking {
        val dataKeys = listOf(Pair("x", "x"),  Pair("y", "y"))
        val postList = listOf(PostListDataUiModel(), PostListDataUiModel())

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPostDataUseCase.executeOnBackground()
        } returns postList

        viewModel.getPostWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getPostDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(postList)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, viewModel.postListWidgetData.value)
    }

    @Test
    fun `get post widget data then returns failed result`() = runBlocking {
        val dataKeys = listOf(Pair("x", "x"),  Pair("y", "y"))
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
        val dataKeys = listOf(anyString(), anyString(), anyString(), anyString())
        val carouselList = listOf(CarouselDataUiModel(), CarouselDataUiModel(), CarouselDataUiModel(), CarouselDataUiModel())

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
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, viewModel.carouselWidgetData.value)
    }

    @Test
    fun `get carousel widget data then returns failed results`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString(), anyString(), anyString())
        val throwable = MessageErrorException("error")

        getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)

        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } throws throwable

        viewModel.getCarouselWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            getCarouselDataUseCase.executeOnBackground()
        }

        assert(viewModel.carouselWidgetData.value is Fail)
    }

    @Test
    fun `should success when get table widget data`() = runBlocking {
        val dataKeys = listOf(Pair("x", "x"),  Pair("y", "y"))
        val result = listOf(TableDataUiModel(), TableDataUiModel())

        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getTableDataUseCase.executeOnBackground()
        } returns result

        viewModel.getTableWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getTableDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, viewModel.tableWidgetData.value)
    }

    @Test
    fun `should failed when get table widget data`() = runBlocking {
        val dataKeys = listOf(Pair("x", "x"),  Pair("y", "y"))

        getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)

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

        getPieChartDataUseCase.params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getPieChartDataUseCase.executeOnBackground()
        } returns result

        viewModel.getPieChartWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getPieChartDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, viewModel.pieChartWidgetData.value)
    }

    @Test
    fun `should failed when get pie chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getPieChartDataUseCase.params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

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

        getBarChartDataUseCase.params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getBarChartDataUseCase.executeOnBackground()
        } returns result

        viewModel.getBarChartWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getBarChartDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(result)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, viewModel.barChartWidgetData.value)
    }

    @Test
    fun `should failed when get bar chart widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getBarChartDataUseCase.params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)

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

        getMultiLineGraphUseCase.params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)

        coEvery {
            getMultiLineGraphUseCase.executeOnBackground()
        } returns result

        viewModel.getMultiLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getMultiLineGraphUseCase.executeOnBackground()
        }

        //number of data keys and result should same
        assertTrue(dataKeys.size == result.size)

        val expectedResult = Success(result)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, viewModel.multiLineGraphWidgetData.value)
    }

    @Test
    fun `should failed when get multi line graph widget data`() = runBlocking {
        val dataKeys = listOf(anyString(), anyString())

        getMultiLineGraphUseCase.params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)

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
        assertTrue(dataKeys.size == result.size)

        val expectedResult = Success(result)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, viewModel.announcementWidgetData.value)
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

        verify (exactly = 1) {
            getCardDataUseCase.setUseCache(true)
        }

        verify (exactly = 1) {
            getCardDataUseCase.setUseCache(false)
        }

        coVerify (exactly = 2) {
            getCardDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(cardDataResult)
        assertTrue(dataKeys.size == expectedResult.data.size)
        assertEquals(expectedResult, viewModel.cardWidgetData.observeAwaitValue())
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

        verify (exactly = 1) {
            getCardDataUseCase.setUseCache(true)
        }

        verify (exactly = 1) {
            getCardDataUseCase.setUseCache(false)
        }

        coVerify (exactly = 2) {
            getCardDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(cardDataResult)
        assertTrue(dataKeys.size == expectedResult.data.size)
        assertEquals(expectedResult, viewModel.cardWidgetData.observeAwaitValue())
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

        verify (inverse = true) {
            getCardDataUseCase.setUseCache(true)
        }

        verify (exactly = 1) {
            getCardDataUseCase.setUseCache(false)
        }

        coVerify (exactly = 1) {
            getCardDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(cardDataResult)
        assertTrue(dataKeys.size == expectedResult.data.size)
        assertEquals(expectedResult, viewModel.cardWidgetData.observeAwaitValue())
    }

    @Test
    fun `given new caching enabled when getTicker flow success should set homeTicker liveData success`() {
        coroutineTestRule.runBlockingTest {
            val isNewCachingEnabled = true
            val tickerList = listOf(TickerItemUiModel())

            onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled)
            onGetTickerListFlow_thenReturn(tickerList)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled()

            val expectedResult = Success(listOf(TickerItemUiModel()))
            val actualResult = viewModel.homeTicker.value

            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `given new caching enabled when getTicker twice should set homeTicker liveData with first value`() {
        coroutineTestRule.runBlockingTest {
            val isCollectingResult = true
            val isNewCachingEnabled = true
            val firstTickerList = listOf(TickerItemUiModel(message = "ticker"))
            val secondTickerList = listOf(TickerItemUiModel(message = "another ticker"))

            onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled)
            onGetTickerListFlow_thenReturn(firstTickerList)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled()

            onGetCollectingResult_thenReturn(isCollectingResult)
            onGetTickerListFlow_thenReturn(secondTickerList)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled()

            val expectedResult = Success(firstTickerList)
            val actualResult = viewModel.homeTicker.value

            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `given new caching enabled when getTicker flow error should set homeTicker liveData error`() {
        coroutineTestRule.runBlockingTest {
            val isNewCachingEnabled = true
            val error = IllegalStateException()

            onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled)
            onGetTickerListFlow_thenReturn(error)

            viewModel.getTicker()

            verifyGetTickerResultFlowCalled()
            verifyGetTickerUseCaseCalled()

            val expectedResult = Fail(error)
            val actualResult = viewModel.homeTicker.value

            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `given getting widget data is success, when getting initial widgets layout, start and stop plt custom trace should not be null`() = runBlocking {
        val layoutList: List<BaseWidgetUiModel<*>> = listOf(
                SectionWidgetUiModel(
                        id = "section",
                        widgetType = WidgetType.SECTION,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = "section",
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                CardWidgetUiModel(
                        id = DATA_KEY_CARD,
                        widgetType = WidgetType.CARD,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_CARD,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                CardWidgetUiModel(
                        id = DATA_KEY_CARD,
                        widgetType = WidgetType.CARD,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_CARD,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                CardWidgetUiModel(
                        id = DATA_KEY_CARD,
                        widgetType = WidgetType.CARD,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_CARD,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", ""))
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
                        appLink = "",
                        dataKey = "section",
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                AnnouncementWidgetUiModel(
                        id = DATA_KEY_ANNOUNCEMENT,
                        widgetType = WidgetType.ANNOUNCEMENT,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_ANNOUNCEMENT,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = true,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                SectionWidgetUiModel(
                        id = "section_other",
                        widgetType = WidgetType.SECTION,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = "section_other",
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                CarouselWidgetUiModel(
                        id = DATA_KEY_CAROUSEL,
                        widgetType = WidgetType.CAROUSEL,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_CAROUSEL,
                        ctaText = "",
                        isShowEmpty = false,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                PostListWidgetUiModel(
                        id = DATA_KEY_POST_LIST,
                        widgetType = WidgetType.POST_LIST,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_POST_LIST,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                        postFilter = listOf(WidgetFilterUiModel("", "", true))),
                SectionWidgetUiModel(
                        id = "section_other2",
                        widgetType = WidgetType.SECTION,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = "section_other2",
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                ProgressWidgetUiModel(
                        id = DATA_KEY_PROGRESS,
                        widgetType = WidgetType.PROGRESS,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_PROGRESS,
                        ctaText = "",
                        isShowEmpty = false,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", ""))
        )

        val announcementData = AnnouncementDataUiModel(dataKey = DATA_KEY_ANNOUNCEMENT, showWidget = false)
        val carouselData = CarouselDataUiModel(dataKey = DATA_KEY_CAROUSEL, showWidget = true)
        val postData = PostListDataUiModel(dataKey = DATA_KEY_POST_LIST, items = listOf(PostUiModel()), showWidget = true)
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
                        appLink = "",
                        dataKey = DATA_KEY_PROGRESS,
                        ctaText = "",
                        isShowEmpty = false,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = true,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                AnnouncementWidgetUiModel(
                        id = DATA_KEY_ANNOUNCEMENT,
                        widgetType = WidgetType.ANNOUNCEMENT,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_ANNOUNCEMENT,
                        ctaText = "",
                        isShowEmpty = false,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
        )

        val progressData = ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, showWidget = true)
        val announcementData = AnnouncementDataUiModel(dataKey = DATA_KEY_ANNOUNCEMENT, showWidget = true)
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
                        appLink = "",
                        dataKey = DATA_KEY_PROGRESS,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
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
                        appLink = "",
                        dataKey = DATA_KEY_PROGRESS,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
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
                        appLink = "",
                        dataKey = DATA_KEY_PROGRESS,
                        ctaText = "",
                        isShowEmpty = false,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
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
                        appLink = "",
                        dataKey = DATA_KEY_PROGRESS,
                        ctaText = "",
                        isShowEmpty = false,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
        )

        val progressData = ProgressDataUiModel(dataKey = DATA_KEY_PROGRESS, value = 1, showWidget = true)
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
                        appLink = "",
                        dataKey = DATA_KEY_POST_LIST,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                        postFilter = listOf(WidgetFilterUiModel("", "", false)))
        )

        val postData = PostListDataUiModel(DATA_KEY_POST_LIST, items = listOf(PostUiModel()), showWidget = true)
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

    private suspend fun onGetTickerListFlow_thenReturn(tickerList: List<TickerItemUiModel>) {
        coEvery {
            getTickerUseCase.getResultFlow()
        } returns  MutableSharedFlow<List<TickerItemUiModel>>(replay = 1).apply {
            emit(tickerList)
        }
    }

    private suspend fun onGetLayoutFlow_thenReturn(tickerList: List<BaseWidgetUiModel<*>>) {
        coEvery {
            getLayoutUseCase.getResultFlow()
        } returns  MutableSharedFlow<List<BaseWidgetUiModel<*>>>(replay = 1).apply {
            emit(tickerList)
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

    private fun onGetIsNewCachingEnabled_thenReturn(isNewCachingEnabled: Boolean) {
        coEvery {
            remoteConfig.isSellerHomeDashboardNewCachingEnabled()
        } returns isNewCachingEnabled
    }

    private fun verifyGetTickerUseCaseCalled() {
        coVerify {
            getTickerUseCase.executeOnBackground(any(), any())
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
                        appLink = "",
                        dataKey = DATA_KEY_CARD,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                ProgressWidgetUiModel(
                        id = DATA_KEY_PROGRESS,
                        widgetType = WidgetType.PROGRESS,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_PROGRESS,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                LineGraphWidgetUiModel(
                        id = DATA_KEY_LINE_GRAPH,
                        widgetType = WidgetType.LINE_GRAPH,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_LINE_GRAPH,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                AnnouncementWidgetUiModel(
                        id = DATA_KEY_ANNOUNCEMENT,
                        widgetType = WidgetType.ANNOUNCEMENT,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_ANNOUNCEMENT,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                CarouselWidgetUiModel(
                        id = DATA_KEY_CAROUSEL,
                        widgetType = WidgetType.CAROUSEL,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_CAROUSEL,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                PostListWidgetUiModel(
                        id = DATA_KEY_POST_LIST,
                        widgetType = WidgetType.POST_LIST,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_POST_LIST,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                        postFilter = listOf()),
                TableWidgetUiModel(
                        id = DATA_KEY_TABLE,
                        widgetType = WidgetType.TABLE,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_TABLE,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                        tableFilters = listOf()),
                PieChartWidgetUiModel(
                        id = DATA_KEY_PIE_CHART,
                        widgetType = WidgetType.PIE_CHART,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_PIE_CHART,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                BarChartWidgetUiModel(
                        id = DATA_KEY_BAR_CHART,
                        widgetType = WidgetType.BAR_CHART,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_BAR_CHART,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", "")),
                MultiLineGraphWidgetUiModel(
                        id = DATA_KEY_MULTI_LINE,
                        widgetType = WidgetType.MULTI_LINE_GRAPH,
                        title = "",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", true, listOf()),
                        appLink = "",
                        dataKey = DATA_KEY_MULTI_LINE,
                        ctaText = "",
                        isShowEmpty = true,
                        data = null,
                        isLoaded = false,
                        isLoading = false,
                        isFromCache = false,
                        isNeedToBeRemoved = false,
                        emptyState = WidgetEmptyStateUiModel("", "", "", "", ""),
                        isComparePeriodeOnly = false),
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
            announcementDataUiModel: AnnouncementDataUiModel
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
    }
}