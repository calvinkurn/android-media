package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.sellerhome.domain.model.GetShopStatusResponse
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.usecase.*
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Matchers.anyString

/**
 * Created By @ilhamsuaib on 19/03/20
 */

@ExperimentalCoroutinesApi
class SellerHomeViewModelTest {

    @RelaxedMockK
    lateinit var getShopStatusUseCase: GetStatusShopUseCase

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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        testDispatcher = TestCoroutineDispatcher()
    }

    private fun createViewModel(): SellerHomeViewModel {
        return SellerHomeViewModel(getShopStatusUseCase, userSession, getTickerUseCase, getLayoutUseCase,
                getShopLocationUseCase, getCardDataUseCase, getLineGraphDataUseCase, getProgressDataUseCase,
                getPostDataUseCase, getCarouselDataUseCase, testDispatcher)
    }

    @Test
    fun `get ticker should success`() {
        val tickerList = listOf(
                TickerUiModel("", "", "", "", "", "",
                        "", "", "", "", "", "", "")
        )

        coEvery {
            getTickerUseCase.executeOnBackground()
        } returns tickerList

        val viewModel = createViewModel()
        runBlocking {
            viewModel.getTicker()
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            coVerify {
                getTickerUseCase.executeOnBackground()
            }
        }
        assertEquals(Success(tickerList), viewModel.homeTicker.value)
    }

    @Test
    fun `get shop status should success`() = runBlocking {
        val shopStatus = GetShopStatusResponse()
        val shopId = "123456"

        getShopStatusUseCase.params = GetStatusShopUseCase.createRequestParams(shopId)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getShopStatusUseCase.executeOnBackground()
        } returns shopStatus
        val viewModel = createViewModel()
        viewModel.getShopStatus()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            userSession.shopId
        }
        coVerify {
            getShopStatusUseCase.executeOnBackground()
        }

        assertEquals(Success(shopStatus), viewModel.shopStatus.value)
    }

    @Test
    fun `get shop status should failed`() = runBlocking {
        val throwable = MessageErrorException("error")
        val shopId = "123456"

        getShopStatusUseCase.params = GetStatusShopUseCase.createRequestParams("123456")

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getShopStatusUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getShopStatus()
        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            userSession.shopId
        }

        coEvery {
            getShopStatusUseCase.executeOnBackground()
        }

        assert(viewModel.shopStatus.value is Fail)
    }

    @Test
    fun `get widget layout should success`() = runBlocking {
        val layoutList: List<BaseWidgetUiModel<*>> = emptyList()
        val shopId = "123456"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList

        val viewModel = createViewModel()
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

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getWidgetLayout()

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

        val viewModel = createViewModel()
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
        val throwable = MessageErrorException("error message")
        val shopId = "123456"

        getShopLocationUseCase.params = GetShopLocationUseCase.getRequestParams(shopId)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getShopLocationUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getShopLocation()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            getShopLocationUseCase.executeOnBackground()
        }

        assert(viewModel.shopLocation.value is Fail)
    }

    @Test
    fun `get card widget data then returns success result`() {
        val shopId = 12345
        val dataKeys = listOf("a", "b", "c")
        val startDate = "02-03-20202"
        val endDate = "09-03-20202"

        val cardDataResult = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)

        every {
            userSession.shopId
        } returns shopId.toString()

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } returns cardDataResult

        val viewModel = createViewModel()
        runBlocking {
            viewModel.getCardWidgetData(dataKeys)
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verify {
                userSession.shopId
            }
            coVerify {
                getCardDataUseCase.executeOnBackground()
            }
        }

        val expectedResult = Success(cardDataResult)
        assertTrue(dataKeys.size == expectedResult.data.size)
        assertEquals(expectedResult, viewModel.cardWidgetData.value)
    }

    @Test
    fun `get card widget data then returns failed result`()  {
        val shopId = "12345"
        val dataKeys = listOf("a", "b", "c")
        val startDate = "02-03-20202"
        val endDate = "09-03-20202"

        val throwable = ResponseErrorException()

        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(shopId.toIntOrZero(), dataKeys, startDate, endDate)

        every {
            userSession.shopId
        } returns shopId

        val viewModel = createViewModel()
        runBlocking {
            coEvery {
                getCardDataUseCase.executeOnBackground()
            } throws throwable
            viewModel.getCardWidgetData(dataKeys)
          
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            val result = viewModel.cardWidgetData.value
            assert(result is Fail)
        }
    }

    @Test
    fun `get line graph widget data then returns success result`() = runBlocking {
        val shopId = "12345"
        val dataKeys = listOf("x", "y", "z")
        val startDate = "02-03-20202"
        val endDate = "09-03-20202"

        val lineGraphDataResult = listOf(LineGraphDataUiModel(), LineGraphDataUiModel(), LineGraphDataUiModel())
        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLineGraphDataUseCase.executeOnBackground()
        } returns lineGraphDataResult

        val viewModel = createViewModel()
        viewModel.getLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            userSession.shopId
        }
        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(lineGraphDataResult)
        assertTrue(dataKeys.size == expectedResult.data.size)
        assertEquals(expectedResult, viewModel.lineGraphWidgetData.value)
    }

    @Test
    fun `get line graph widget data then returns failed result`() = runBlocking {
        val shopId = "12345"
        val dataKeys = listOf("x", "y", "z")
        val startDate = "02-03-20202"
        val endDate = "09-03-20202"

        val throwable= MessageErrorException("error message")
        getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLineGraphDataUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getLineGraphWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        assert(viewModel.lineGraphWidgetData.value is Fail)
    }

    @Test
    fun `get progress widget data then returns success result`() = runBlocking {
        val shopId = "124456"
        val dateStr = "02-02-2020"
        val dataKeys = listOf("x", "y", "z")
        val progressDataList = listOf(ProgressDataUiModel(), ProgressDataUiModel(), ProgressDataUiModel())

        getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(shopId, dateStr, dataKeys)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } returns progressDataList

        val viewModel = createViewModel()
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
        val shopId = "124456"
        val dateStr = "02-02-2020"
        val dataKeys = listOf("x", "y", "z")
        val throwable = MessageErrorException("error")

        getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(shopId, dateStr, dataKeys)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getProgressDataUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getProgressWidgetData(dataKeys)

        delay (100)
        coVerify {
            getProgressDataUseCase.executeOnBackground()
        }

        assert(viewModel.progressWidgetData.value is Fail)
    }

    @Test
    fun `get post widget data then returns success result`() = runBlocking {
        val shopId = 12345
        val dataKeys = listOf("x", "x")
        val startDate = "02-02-2020"
        val endDate = "07-02-2020"
        val postList = listOf(PostListDataUiModel(), PostListDataUiModel())

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)

        every {
            userSession.shopId
        } returns shopId.toString()

        coEvery {
            getPostDataUseCase.executeOnBackground()
        } returns postList

        val viewModel = createViewModel()
        viewModel.getPostWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            userSession.shopId
        }

        coVerify {
            getPostDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(postList)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, viewModel.postListWidgetData.value)
    }

    @Test
    fun `get post widget data then returns failed result`() {
        val shopId = 12345
        val dataKeys = listOf("x", "x")
        val startDate = "02-02-2020"
        val endDate = "07-02-2020"
        val exception = MessageErrorException("error msg")

        getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)

        every {
            userSession.shopId
        } returns shopId.toString()

        coEvery {
            getPostDataUseCase.executeOnBackground()
        } throws exception

        val viewModel = createViewModel()
        runBlocking {
            viewModel.getPostWidgetData(dataKeys)
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verify {
                userSession.shopId
            }
            coVerify {
                getPostDataUseCase.executeOnBackground()
            }
        }

        assert(viewModel.postListWidgetData.value is Fail)
    }

    @Test
    fun `get carousel widget data then returns success results`() {
        val dataKeys = listOf(anyString(), anyString(), anyString(), anyString())
        val carouselList = listOf(CarouselDataUiModel(), CarouselDataUiModel(), CarouselDataUiModel(), CarouselDataUiModel())

        getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)

        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } returns carouselList

        val viewModel = createViewModel()
        runBlocking {
            viewModel.getCarouselWidgetData(dataKeys)

            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            coVerify {
                getCarouselDataUseCase.executeOnBackground()
            }
        }

        val expectedResult = Success(carouselList)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, viewModel.carouselWidgetData.value)
    }

    @Test
    fun `get carousel widget data then returns failed results`() = runBlocking{
        val dataKeys = listOf(anyString(), anyString(), anyString(), anyString())
        val throwable = MessageErrorException("error")

        getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)

        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getCarouselWidgetData(dataKeys)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        coVerify {
            getCarouselDataUseCase.executeOnBackground()
        }

        assert(viewModel.carouselWidgetData.value is Fail)
    }
}