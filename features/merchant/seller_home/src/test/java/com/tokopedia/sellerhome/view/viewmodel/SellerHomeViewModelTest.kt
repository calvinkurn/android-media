package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.usecase.GetShopLocationUseCase
import com.tokopedia.sellerhome.utils.observeAwaitValue
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
        val layoutList: List<BaseWidgetUiModel<*>> = emptyList()
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
        val dataKeys = listOf(anyString(), anyString())
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
        val dataKeys = listOf(anyString(), anyString())

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

    private suspend fun onGetTickerListFlow_thenReturn(tickerList: List<TickerItemUiModel>) {
        coEvery {
            getTickerUseCase.getResultFlow()
        } returns  MutableSharedFlow<List<TickerItemUiModel>>(replay = 1).apply {
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
}