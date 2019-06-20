package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.GMParamApiContant
import com.tokopedia.gm.common.data.source.cloud.ToggleAutoExtendPowerMerchantCloudSource
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantActivationResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class ToggleAutoExtendPowerMerchantUseCase @Inject constructor(
        private val turnOnOffAutoExtendPowerMerchantCloudSource: ToggleAutoExtendPowerMerchantCloudSource)
    : UseCase<PowerMerchantActivationResult>() {

    override fun createObservable(requestParams: RequestParams?): Observable<PowerMerchantActivationResult> {
        val isAutoExtend = requestParams?.getBoolean(GMParamApiContant.AUTO_EXTEND, false) ?: false
        return turnOnOffAutoExtendPowerMerchantCloudSource.turnOnOffAutoExtendPowerMerchant(isAutoExtend)
    }

    companion object {
        fun createRequestParams(autoExtend: Boolean): RequestParams {
            return RequestParams.create().apply {
                putBoolean(GMParamApiContant.AUTO_EXTEND, autoExtend)
            }
        }
    }
}