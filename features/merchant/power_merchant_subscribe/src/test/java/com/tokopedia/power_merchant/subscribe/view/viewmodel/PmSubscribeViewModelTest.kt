package com.tokopedia.power_merchant.subscribe.view.viewmodel

import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.power_merchant.subscribe.verification.verifyErrorEquals
import com.tokopedia.power_merchant.subscribe.verification.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import org.junit.Test

class PmSubscribeViewModelTest: PmSubscribeViewModelTestFixture() {

    @Test
    fun `when getPmStatusInfo success should set result success`() {
        val kycUserProjectInfoPojo = KycUserProjectInfoPojo().apply {
            kycProjectInfo = KycProjectInfo()
        }
        val powerMerchantStatus = PowerMerchantStatus(kycUserProjectInfoPojo = kycUserProjectInfoPojo)

        onGetPowerMerchantStatusUseCase_thenReturn(powerMerchantStatus)

        viewModel.getPmStatusInfo()

        val expectedResult = Success(powerMerchantStatus)

        viewModel.getPmStatusInfoResult
            .verifySuccessEquals(expectedResult)

        verifyHideLoading()
    }

    @Test
    fun `when getPmStatusInfo returns error should set result fail`() {
        val error = NullPointerException()

        onGetPowerMerchantStatusUseCase_thenReturn(error)

        viewModel.getPmStatusInfo()

        val expectedResult = Fail(error)

        viewModel.getPmStatusInfoResult
            .verifyErrorEquals(expectedResult)

        verifyHideLoading()
    }

    @Test
    fun `given kycProjectInfo null when getPmStatusInfo should set result error`() {
        val kycUserProjectInfoPojo = KycUserProjectInfoPojo().apply {
            kycProjectInfo = null
        }
        val powerMerchantStatus = PowerMerchantStatus(kycUserProjectInfoPojo = kycUserProjectInfoPojo)

        onGetPowerMerchantStatusUseCase_thenReturn(powerMerchantStatus)

        viewModel.getPmStatusInfo()

        val error = NullPointerException("kycProjectInfo must not be null")
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