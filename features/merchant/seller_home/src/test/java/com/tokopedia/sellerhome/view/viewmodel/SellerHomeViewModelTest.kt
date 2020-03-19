package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.model.GetShopStatusResponse
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.usecase.*
import com.tokopedia.sellerhome.view.model.BaseWidgetUiModel
import com.tokopedia.sellerhome.view.model.CardDataUiModel
import com.tokopedia.sellerhome.view.model.TickerUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

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

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val mViewModel: SellerHomeViewModel by lazy {
        SellerHomeViewModel(getShopStatusUseCase, userSession, getTickerUseCase, getLayoutUseCase,
                getShopLocationUseCase, getCardDataUseCase, getLineGraphDataUseCase, getProgressDataUseCase,
                getPostDataUseCase, getCarouselDataUseCase, Dispatchers.Unconfined)
    }

    @Test
    fun `should success when get ticker`() {
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
    fun `should success when get shop status`() {
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
    fun `should failed when get shop status`() {
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

        assertEquals(Fail(throwable), mViewModel.shopStatus.value)
    }

    @Test
    fun `should success when get widget layout`() {
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
    fun `should failed when get widget layout`() {
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

        assertEquals(Fail(throwable), mViewModel.widgetLayout.value)
    }

    @Test
    fun `should success when get shop location`() {
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
    fun `should failed when get shop location`() {
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

        verify {
            userSession.shopId
        }

        coVerify {
            getShopLocationUseCase.executeOnBackground()
        }

        assertEquals(Fail(throwable), mViewModel.shopLocation.value)
    }

    @Test
    fun `should success when get card widget data`() {
        val shopId = 12345
        val dataKeys = listOf("a", "b", "c")
        val startDate = "02-03-20202"
        val endDate = "09-03-20202"

        val cardData = listOf(CardDataUiModel(), CardDataUiModel(), CardDataUiModel())
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)

        every {
            userSession.shopId
        } returns shopId.toString()

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } returns cardData

        mViewModel.getCardWidgetData(dataKeys)

        verify {
            userSession.shopId
        }

        coVerify {
            getCardDataUseCase.executeOnBackground()
        }

        assertEquals(Success(cardData), mViewModel.cardWidgetData.value)
    }

    @Test
    fun `should failed when get card widget data`() {
        val shopId = 12345
        val dataKeys = listOf("a", "b", "c")
        val startDate = "02-03-20202"
        val endDate = "09-03-20202"

        val throwable = Throwable()
        getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)

        every {
            userSession.shopId
        } returns shopId.toString()

        coEvery {
            getCardDataUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getCardWidgetData(dataKeys)

        verify {
            userSession.shopId
        }

        coVerify {
            getCardDataUseCase.executeOnBackground()
        }

        assertEquals(Fail(throwable), mViewModel.cardWidgetData.value)
    }

    /*@Test
    fun `should success get line graph widget data`() {

    }

    @Test
    fun `should failed get line graph widget data`() {

    }

    @Test
    fun `should success get progress widget data`() {

    }

    @Test
    fun `should failed get progress widget data`() {

    }

    @Test
    fun `should success get post widget data`() {

    }

    @Test
    fun `should failed get post widget data`() {

    }

    @Test
    fun `should success get carousel widget data`() {

    }

    @Test
    fun `should failed get carousel widget data`() {

    }*/
}