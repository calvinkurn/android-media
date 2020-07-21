package com.tokopedia.power_merchant.subscribe.view.viewmodel

import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.power_merchant.subscribe.verification.verifyErrorEquals
import com.tokopedia.power_merchant.subscribe.verification.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class PmSubscribeViewModelTest: PmSubscribeViewModelTestFixture() {

    @Test
    fun `when activate pm success should set result success`() {
        val powerMerchantStatus = PowerMerchantStatus()

        onGetPowerMerchantStatusUseCase_thenReturn(powerMerchantStatus)

        viewModel.getPmStatusInfo()

        val expectedResult = Success(powerMerchantStatus)

        viewModel.getPmStatusInfoResult
            .verifySuccessEquals(expectedResult)

        verifyHideLoading()
    }

    @Test
    fun `when activate pm returns error should set result fail`() {
        val error = NullPointerException()

        onGetPowerMerchantStatusUseCase_thenReturn(error)

        viewModel.getPmStatusInfo()

        val expectedResult = Fail(error)

        viewModel.getPmStatusInfoResult
            .verifyErrorEquals(expectedResult)

        verifyHideLoading()
    }

    @Test
    fun `when detach view should unsubscribe use case`() {
        viewModel.detachView()

        verifyUnsubscribeUseCase()
    }
}