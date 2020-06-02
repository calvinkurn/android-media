package com.tokopedia.power_merchant.subscribe.view.viewmodel

import com.tokopedia.power_merchant.subscribe.verification.verifyErrorEquals
import com.tokopedia.power_merchant.subscribe.verification.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class PmTermsViewModelTest: PmTermsViewModelTestFixture() {

    @Test
    fun `when activate pm success should set result success`() {
        val isSuccess = true

        onActivatePm_thenReturn(isSuccess)

        viewModel.activatePowerMerchant()

        val expectedResult = Success(true)

        viewModel.activatePmResult
            .verifySuccessEquals(expectedResult)

        verifyHideLoading()
    }

    @Test
    fun `when activate pm NOT success should set result fail`() {
        val isSuccess = false

        onActivatePm_thenReturn(isSuccess)

        viewModel.activatePowerMerchant()

        val expectedResult = Fail(RuntimeException())

        viewModel.activatePmResult
            .verifyErrorEquals(expectedResult)

        verifyHideLoading()
    }

    @Test
    fun `when activate pm returns error should set result fail`() {
        val error = NullPointerException()

        onActivatePm_thenReturn(error)

        viewModel.activatePowerMerchant()

        val expectedResult = Fail(error)

        viewModel.activatePmResult
            .verifyErrorEquals(expectedResult)

        verifyHideLoading()
    }

    @Test
    fun `when detach view should unsubscribe use case`() {
        viewModel.detachView()

        verifyUnsubscribeUseCase()
    }
}