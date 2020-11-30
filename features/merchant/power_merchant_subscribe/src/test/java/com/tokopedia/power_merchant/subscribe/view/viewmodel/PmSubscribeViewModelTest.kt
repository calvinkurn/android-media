package com.tokopedia.power_merchant.subscribe.view.viewmodel

import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.power_merchant.subscribe.view.model.PowerMerchantFreeShippingStatus
import com.tokopedia.shop.common.data.source.cloud.model.ShopFreeShippingStatus
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
    fun `given free shipping config disabled when getFreeShippingStatus should not call use case`() {
        val freeShippingEnabled = false

        onGetFreeShippingEnabledConfig_thenReturn(freeShippingEnabled)

        viewModel.getFreeShippingStatus()

        verifyGetFreeShippingStatusUseCaseNotCalled()
    }

    @Test
    fun `when getFreeShippingStatus should set result success`() {
        val freeShippingStatus = ShopFreeShippingStatus(
            active = true,
            status = false,
            statusEligible = true
        )

        onGetFreeShippingEnabledConfig_thenReturn(isEnabled = true)
        onGetFreeShippingStatusUseCase_thenReturn(freeShippingStatus)

        viewModel.getFreeShippingStatus()

        val data = PowerMerchantFreeShippingStatus(
            isActive = true,
            isEligible = true,
            isTransitionPeriod = false,
            isPowerMerchantIdle = false,
            isPowerMerchantActive = false
        )
        val expectedResult = Success(data)

        viewModel.getPmFreeShippingStatusResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when getFreeShippingStatus should set result error`() {
        val error = NullPointerException()

        onGetFreeShippingEnabledConfig_thenReturn(isEnabled = true)
        onGetFreeShippingStatusUseCase_thenReturn(error)

        viewModel.getFreeShippingStatus()

        val expectedResult = Fail(error)

        viewModel.getPmFreeShippingStatusResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when onActivatePmSuccess should set result success`() {
        val freeShippingStatus = ShopFreeShippingStatus(
            active = true,
            status = false,
            statusEligible = true
        )

        onGetFreeShippingStatusUseCase_thenReturn(freeShippingStatus)

        viewModel.onActivatePmSuccess()

        val data = PowerMerchantFreeShippingStatus(
            isActive = true,
            isEligible = true,
            isTransitionPeriod = false,
            isPowerMerchantIdle = false,
            isPowerMerchantActive = false
        )
        val expectedResult = Success(data)

        viewModel.onActivatePmSuccess
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when onActivatePmSuccess should set result error`() {
        val error = NullPointerException()

        onGetFreeShippingStatusUseCase_thenReturn(error)

        viewModel.onActivatePmSuccess()

        val expectedResult = Fail(error)

        viewModel.onActivatePmSuccess
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when detach view should unsubscribe use case`() {
        viewModel.detachView()

        verifyUnsubscribeUseCase()
    }
}