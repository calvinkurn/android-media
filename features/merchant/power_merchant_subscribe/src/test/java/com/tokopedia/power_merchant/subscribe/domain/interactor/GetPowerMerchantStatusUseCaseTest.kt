package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetPowerMerchantStatusUseCaseTest: GetPowerMerchantStatusUseCaseTestFixture() {

    @Test
    fun `given get power merchant status success when execute use case should return power merchant status`() {
        runBlocking {
            val requestParams = RequestParams()
            val goldGetPmOsStatus = GoldGetPmOsStatus()
            val shopScoreResult = ShopScoreResult()
            val kycUserProjectInfoPojo = KycUserProjectInfoPojo()
            val powerMerchantStatus = PowerMerchantStatus(goldGetPmOsStatus, kycUserProjectInfoPojo, shopScoreResult)

            onGetShopStatus_thenReturn(goldGetPmOsStatus)
            onGetShopScore_thenReturn(shopScoreResult)
            onGetUserProjectInfo_thenReturn(kycUserProjectInfoPojo)

            val getPowerMerchantStatus = useCase.createObservable(requestParams).test()

            verifyAllUseCaseCalled()

            getPowerMerchantStatus
                .assertNoErrors()
                .assertCompleted()
                .assertValue(powerMerchantStatus)
        }
    }

    @Test
    fun `given get power merchant status error when execute use case should return error`() {
        runBlocking {
            val requestParams = RequestParams()
            val error = NullPointerException()

            onGetShopStatus_thenReturn(error)

            useCase.createObservable(requestParams)
                .test()
                .assertError(error)
        }
    }
}