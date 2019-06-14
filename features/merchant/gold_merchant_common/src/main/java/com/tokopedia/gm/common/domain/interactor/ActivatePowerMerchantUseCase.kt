package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.ActivatePowerMerchantCloudSource
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantActivationResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class ActivatePowerMerchantUseCase @Inject constructor(
        private val activatePowerMerchantCloudSource: ActivatePowerMerchantCloudSource): UseCase<PowerMerchantActivationResult>() {
    override fun createObservable(requestParams: RequestParams?): Observable<PowerMerchantActivationResult> {
        return activatePowerMerchantCloudSource.activatePowerMerchant()
    }
}