package com.tokopedia.minicart.common.simplified.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.promo.domain.usecase.ValidateUseMvcUseCase
import com.tokopedia.minicart.common.simplified.MiniCartSimplifiedState
import com.tokopedia.minicart.common.simplified.MiniCartSimplifiedViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetMiniCartListSimplifiedTest {

    private lateinit var viewModel: MiniCartSimplifiedViewModel

    private var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private var validateUseMvcUseCase: ValidateUseMvcUseCase = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MiniCartSimplifiedViewModel(getMiniCartListSimplifiedUseCase, validateUseMvcUseCase)
    }

    @Test
    fun `WHEN set current shop id, promo id, promo code THEN should set current shop id, promo id, promo code`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"

        //when
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode

        //then
        assert(viewModel.currentShopIds == shopId)
        assert(viewModel.currentPromoId == promoId)
        assert(viewModel.currentPromoCode == promoCode)
    }

    @Test
    fun `WHEN fetch last widget state without set shopId THEN should return default data`() {
        //when
        viewModel.getLatestWidgetState()

        //then
        assert(viewModel.miniCartSimplifiedData.value == MiniCartSimplifiedData())
    }

    @Test
    fun `WHEN fetch last widget state with current data and without set shopId THEN should not return default data`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } just Runs
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode

        viewModel.getLatestWidgetState()

        //when
        viewModel.currentShopIds = emptyList()
        viewModel.getLatestWidgetState()

        //then
        assert(viewModel.miniCartSimplifiedData.value != MiniCartSimplifiedData())
    }

    @Test
    fun `WHEN set current shop id, promo id, promo code THEN should fetch with current shop id, promo id, promo code`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } just Runs
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode

        //when
        viewModel.getLatestWidgetState()

        //then
        verify { getMiniCartListSimplifiedUseCase.setParams(shopId, promoId, promoCode, any()) }
        coVerify { getMiniCartListSimplifiedUseCase.execute(any(), any()) }
    }

    @Test
    fun `WHEN fetch latest widget failed THEN should set default data`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val errorMessage = "Error Message"
        val exception = ResponseErrorException(errorMessage)
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode

        //when
        viewModel.getLatestWidgetState()

        //then
        assert(viewModel.miniCartSimplifiedData.value == MiniCartSimplifiedData())
    }

    @Test
    fun `WHEN fetch latest widget failed with current data available THEN should not set default data`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } just Runs

        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode
        viewModel.getLatestWidgetState()

        val errorMessage = "Error Message"
        val exception = ResponseErrorException(errorMessage)
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        //when
        viewModel.getLatestWidgetState()

        //then
        assert(viewModel.miniCartSimplifiedData.value != MiniCartSimplifiedData())
    }

    @Test
    fun `WHEN fetch latest widget failed THEN should set error state`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val errorMessage = "Error Message"
        val exception = ResponseErrorException(errorMessage)
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode

        //when
        viewModel.getLatestWidgetState()

        //then
        assert(viewModel.miniCartSimplifiedState.value == MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_FAILED_MINICART, throwable = exception))
    }

    @Test
    fun `WHEN fetch latest data success with empty cart THEN should not validate mvc`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessEmptyData()
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } just Runs
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode

        //when
        viewModel.getLatestWidgetState()

        //then
        coVerify(inverse = true) { validateUseMvcUseCase.setParam(any()).execute(any(), any()) }
    }

    @Test
    fun `WHEN fetch latest data success with empty cart THEN should set state failed validate mvc`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessEmptyData()
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } just Runs
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode

        //when
        viewModel.getLatestWidgetState()

        //then
        assert(viewModel.miniCartSimplifiedState.value == MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_FAILED_VALIDATE_USE))
    }

    @Test
    fun `WHEN fetch latest data success with all unavailable cart THEN should not validate mvc`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllUnavailable()
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } just Runs
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode

        //when
        viewModel.getLatestWidgetState()

        //then
        coVerify(inverse = true) { validateUseMvcUseCase.setParam(any()).execute(any(), any()) }
    }

    @Test
    fun `WHEN fetch latest data success with all unavailable cart THEN should set state failed validate mvc`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllUnavailable()
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } just Runs
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode

        //when
        viewModel.getLatestWidgetState()

        //then
        assert(viewModel.miniCartSimplifiedState.value == MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_FAILED_VALIDATE_USE))
    }

    @Test
    fun `WHEN fetch latest data success with available cart THEN should validate mvc with only available products`() {
        //given
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAvailableAndUnavailable()
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } just Runs
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode

        //when
        viewModel.getLatestWidgetState()

        //then
        coVerify { validateUseMvcUseCase.setParam(match { it.orders[0].productDetails.size == 3 }).execute(any(), any()) }
    }
}