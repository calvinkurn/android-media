package com.tokopedia.minicart.common.simplified.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.promo.domain.data.ValidateUseMvcData
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ValidateUseMvcTest {

    private lateinit var viewModel: MiniCartSimplifiedViewModel

    private var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private var validateUseMvcUseCase: ValidateUseMvcUseCase = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MiniCartSimplifiedViewModel(getMiniCartListSimplifiedUseCase, validateUseMvcUseCase)
        initializeViewModel()
    }

    private fun initializeViewModel() {
        val shopId = listOf("123")
        val promoId = "promoId"
        val promoCode = "promoCode"
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAvailableAndUnavailable()
        every { getMiniCartListSimplifiedUseCase.setParams(any(), any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }
        viewModel.currentShopIds = shopId
        viewModel.currentPromoId = promoId
        viewModel.currentPromoCode = promoCode
    }

    @Test
    fun `WHEN validate mvc success THEN should update validate use mvc data`() {
        //given
        val validateUseResponse = ValidateUseMvcData(status = "OK")
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } answers {
            firstArg<(ValidateUseMvcData) -> Unit>().invoke(validateUseResponse)
        }

        //when
        viewModel.getLatestWidgetState()

        //then
        assert(viewModel.validateUseMvcData.value == validateUseResponse)
    }

    @Test
    fun `WHEN validate mvc failed THEN should set state failed validate use`() {
        //given
        val throwable = IOException()
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        //when
        viewModel.getLatestWidgetState()

        //then
        assert(viewModel.miniCartSimplifiedState.value == MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_FAILED_VALIDATE_USE, throwable = throwable))
    }

    @Test
    fun `WHEN move to cart without data THEN should not validate mvc with apply true and directly set state move to cart`() {
        //when
        viewModel.moveToCart()

        //then
        coVerify(inverse = true) { validateUseMvcUseCase.setParam(match { it.apply }).execute(any(), any()) }
        assert(viewModel.miniCartSimplifiedState.value == MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_MOVE_TO_CART))
    }

    @Test
    fun `WHEN move to cart without full progress THEN should not validate mvc with apply true and directly set state move to cart`() {
        //given
        val validateUseResponse = ValidateUseMvcData(status = "OK", progressPercentage = 90)
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } answers {
            firstArg<(ValidateUseMvcData) -> Unit>().invoke(validateUseResponse)
        }
        viewModel.getLatestWidgetState()

        //when
        viewModel.moveToCart()

        //then
        coVerify(inverse = true) { validateUseMvcUseCase.setParam(match { it.apply }).execute(any(), any()) }
        assert(viewModel.miniCartSimplifiedState.value == MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_MOVE_TO_CART))
    }

    @Test
    fun `WHEN move to cart with full progress THEN should validate mvc with apply true`() {
        //given
        val validateUseResponse = ValidateUseMvcData(status = "OK", progressPercentage = 100)
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } answers {
            firstArg<(ValidateUseMvcData) -> Unit>().invoke(validateUseResponse)
        }
        viewModel.getLatestWidgetState()

        //when
        viewModel.moveToCart()

        //then
        coVerify { validateUseMvcUseCase.setParam(match { it.apply }).execute(any(), any()) }
    }

    @Test
    fun `WHEN move to cart with full progress success THEN should set state move to cart`() {
        //given
        val validateUseResponse = ValidateUseMvcData(status = "OK", progressPercentage = 100)
        coEvery { validateUseMvcUseCase.setParam(any()).execute(any(), any()) } answers {
            firstArg<(ValidateUseMvcData) -> Unit>().invoke(validateUseResponse)
        }
        viewModel.getLatestWidgetState()

        //when
        viewModel.moveToCart()

        //then
        assert(viewModel.miniCartSimplifiedState.value == MiniCartSimplifiedState(state = MiniCartSimplifiedState.STATE_MOVE_TO_CART))
    }
}