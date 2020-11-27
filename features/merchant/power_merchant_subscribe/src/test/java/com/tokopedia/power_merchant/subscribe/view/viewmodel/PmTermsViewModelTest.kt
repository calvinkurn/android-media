package com.tokopedia.power_merchant.subscribe.view.viewmodel

import com.tokopedia.abstraction.common.network.exception.Header
import com.tokopedia.power_merchant.subscribe.domain.model.ValidatePowerMerchantResponse
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.power_merchant.subscribe.view.model.PowerMerchantActivationResult.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class PmTermsViewModelTest: PmTermsViewModelTestFixture() {

    @Test
    fun `when activate pm success should set result success`() {
        val isSuccess = true

        onActivatePm_thenReturn(isSuccess)

        viewModel.activatePowerMerchant()

        val expectedResult = Success(ActivationSuccess)

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
    fun `when validate pm returns kyc not verified should set result kyc not verified`() {
        val errorCode = "err.validation.kyc"
        val header = Header().apply { this.errorCode = errorCode }
        val validationResponse = ValidatePowerMerchantResponse(header, "invalid")

        onValidatePm_thenReturn(validationResponse)

        viewModel.activatePowerMerchant()

        val expectedResult = Success(KycNotVerified)

        viewModel.activatePmResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when validate pm returns shop score not eligible should set result shop score not eligible`() {
        val errorCode = "err.validation.shop_score"
        val header = Header().apply { this.errorCode = errorCode }
        val validationResponse = ValidatePowerMerchantResponse(header, "invalid")

        onValidatePm_thenReturn(validationResponse)

        viewModel.activatePowerMerchant()

        val expectedResult = Success(ShopScoreNotEligible)

        viewModel.activatePmResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when validate pm returns shop moderated should set result general error`() {
        val errorCode = "err.moderate.shop_not_eligible"
        val message = "Shop is moderated"
        val header = Header().apply {
            this.errorCode = errorCode
            this.messages = listOf(message)
        }
        val validationResponse = ValidatePowerMerchantResponse(header, "invalid")

        onValidatePm_thenReturn(validationResponse)

        viewModel.activatePowerMerchant()

        val expectedResult = Success(GeneralError(message))

        viewModel.activatePmResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when detach view should unsubscribe use case`() {
        viewModel.detachView()

        verifyUnsubscribeUseCase()
    }
}