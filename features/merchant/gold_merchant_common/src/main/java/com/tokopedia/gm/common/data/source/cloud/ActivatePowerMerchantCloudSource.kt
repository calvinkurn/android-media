package com.tokopedia.gm.common.data.source.cloud

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantActivationResult
import rx.Observable
import javax.inject.Inject

class ActivatePowerMerchantCloudSource @Inject constructor(private val gmCommonApi: GMCommonApi) {
    fun activatePowerMerchant(): Observable<PowerMerchantActivationResult> {
        return gmCommonApi.activatePowerMerchant().map {
            if (!it.isSuccessful) {
                throw RuntimeException(it.code().toString())
            } else {
                if (it.body().header.messages != null
                        && it.body().header.messages.isNotEmpty()
                        && it.body().header.messages.first().isNotBlank()) {
                    throw MessageErrorException(it.body().header.messages.first())
                } else {
                    it.body().data
                }
            }
        }
    }
}