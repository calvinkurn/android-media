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

    private lateinit var mViewModel: SellerHomeViewModel
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        testDispatcher = TestCoroutineDispatcher()
        mViewModel = SellerHomeViewModel(getShopStatusUseCase, userSession, getTickerUseCase, getLayoutUseCase,
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

        mViewModel.getTicker()

        coVerify {
            getTickerUseCase.executeOnBackground()
        }

        assertEquals(Success(tickerList), mViewModel.homeTicker.value)
    }

    @Test
    fun `get shop status should success`() {
        val shopStatus = GetShopStatusResponse()
        val shopId = "123456"

        getShopStatusUseCase.params = GetStatusShopUseCase.createRequestParams(shopId)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getShopStatusUseCase.executeOnBackground()
        } returns shopStatus

        mViewModel.getShopStatus()

        verify {
            userSession.shopId
        }

        coVerify {
            getShopStatusUseCase.executeOnBackground()
        }

        assertEquals(Success(shopStatus), mViewModel.shopStatus.value)
    }

    @Test
    fun `get shop status should failed`() {
        val throwable = MessageErrorException("error")
        val shopId = "123456"

        getShopStatusUseCase.params = GetStatusShopUseCase.createRequestParams("123456")

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getShopStatusUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getShopStatus()

        verify {
            userSession.shopId
        }

        coEvery {
            getShopStatusUseCase.executeOnBackground()
        }

        assert(mViewModel.shopStatus.value is Fail)
    }

    @Test
    fun `get widget layout should success`() {
        val layoutList: List<BaseWidgetUiModel<*>> = emptyList()
        val shopId = "123456"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } returns layoutList

        mViewModel.getWidgetLayout()

        verify {
            userSession.shopId
        }

        coEvery {
            getLayoutUseCase.executeOnBackground()
        }

        assertEquals(Success(layoutList), mViewModel.widgetLayout.value)
    }

    @Test
    fun `get widget layout should failed`() {
        val throwable = MessageErrorException("error message")
        val shopId = "123456"

        getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getLayoutUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getWidgetLayout()

        verify {
            userSession.shopId
        }

        coEvery {
            getLayoutUseCase.executeOnBackground()
        }

        assert(mViewModel.widgetLayout.value is Fail)
    }

    @Test
    fun `get shop location then returns success result`() {
        val shopId = "123456"
        getShopLocationUseCase.params = GetShopLocationUseCase.getRequestParams(shopId)

        val shopLocation = ShippingLoc(13)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getShopLocationUseCase.executeOnBackground()
        } returns shopLocation

        mViewModel.getShopLocation()

        verify {
            userSession.shopId
        }

        coVerify {
            getShopLocationUseCase.executeOnBackground()
        }

        assertEquals(Success(shopLocation), mViewModel.shopLocation.value)
    }

    @Test
    fun `get shop location then returns failed result`() {
        val throwable = MessageErrorException("error message")
        val shopId = "123456"

        getShopLocationUseCase.params = GetShopLocationUseCase.getRequestParams(shopId)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getShopLocationUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getShopLocation()

        coVerify {
            getShopLocationUseCase.executeOnBackground()
        }

        assert(mViewModel.shopLocation.value is Fail)
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

        mViewModel.getCardWidgetData(dataKeys)

        verify {
            userSession.shopId
        }

        coVerify {
            getCardDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(cardDataResult)
        assertTrue(dataKeys.size == expectedResult.data.size)
        assertEquals(expectedResult, mViewModel.cardWidgetData.value)
    }

    @Test
    fun `get card widget data then returns failed result`() = runBlocking {
        val shopId = "12345"
        val dataKeys = listOf("a", "b", "c")
        val startDate = "02-03-20202"
        val endDate = "09-03-20202"

        val throwable = ResponseErrorException()

        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(shopId.toIntOrZero(), dataKeys, startDate, endDate)

        every {
            userSession.shopId
        } returns shopId

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getCardWidgetData(dataKeys)

        mViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        val result = mViewModel.cardWidgetData.value
        assert(result is Fail)
    }

    @Test
    fun `get line graph widget data then returns success result`() {
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

        mViewModel.getLineGraphWidgetData(dataKeys)

        verify {
            userSession.shopId
        }

        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(lineGraphDataResult)
        assertTrue(dataKeys.size == expectedResult.data.size)
        assertEquals(expectedResult, mViewModel.lineGraphWidgetData.value)
    }

    @Test
    fun `get line graph widget data then returns failed result`() {
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

        mViewModel.getLineGraphWidgetData(dataKeys)

        coVerify {
            getLineGraphDataUseCase.executeOnBackground()
        }

        assert(mViewModel.lineGraphWidgetData.value is Fail)
    }

    @Test
    fun `get progress widget data then returns success result`() {
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

        mViewModel.getProgressWidgetData(dataKeys)

        coVerify {
            getProgressDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(progressDataList)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, mViewModel.progressWidgetData.value)
    }

    @Test
    fun `get progress widget data then returns failed result`() {
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

        mViewModel.getProgressWidgetData(dataKeys)

        coVerify {
            getProgressDataUseCase.executeOnBackground()
        }

        assert(mViewModel.progressWidgetData.value is Fail)
    }

    @Test
    fun `get post widget data then returns success result`() {
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

        mViewModel.getPostWidgetData(dataKeys)

        verify {
            userSession.shopId
        }

        coVerify {
            getPostDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(postList)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, mViewModel.postListWidgetData.value)
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

        mViewModel.getPostWidgetData(dataKeys)

        verify {
            userSession.shopId
        }

        coVerify {
            getPostDataUseCase.executeOnBackground()
        }

        assert(mViewModel.postListWidgetData.value is Fail)
    }

    @Test
    fun `get carousel widget data then returns success results`() {
        val dataKeys = listOf(anyString(), anyString(), anyString(), anyString())
        val carouselList = listOf(CarouselDataUiModel(), CarouselDataUiModel(), CarouselDataUiModel(), CarouselDataUiModel())

        getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)

        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } returns carouselList

        mViewModel.getCarouselWidgetData(dataKeys)

        coVerify {
            getCarouselDataUseCase.executeOnBackground()
        }

        val expectedResult = Success(carouselList)
        assertTrue(expectedResult.data.size == dataKeys.size)
        assertEquals(expectedResult, mViewModel.carouselWidgetData.value)
    }

    @Test
    fun `get carousel widget data then returns failed results`() {
        val dataKeys = listOf(anyString(), anyString(), anyString(), anyString())
        val throwable = MessageErrorException("error")

        getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)

        coEvery {
            getCarouselDataUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getCarouselWidgetData(dataKeys)

        coVerify {
            getCarouselDataUseCase.executeOnBackground()
        }

        assert(mViewModel.carouselWidgetData.value is Fail)
    }
}