package com.tokopedia.smartbills.data.api

import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common_digital.common.data.request.DataRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.smartbills.data.MultiCheckoutRequest
import com.tokopedia.smartbills.data.RechargeMultiCheckoutResponse
import java.io.IOException
import javax.inject.Inject

class SmartBillsRepositoryImpl @Inject constructor(private val smartBillsApi: SmartBillsApi): SmartBillsRepository {

    override suspend fun postMultiCheckout(request: MultiCheckoutRequest,
                                           idempotencyKey: String): RechargeMultiCheckoutResponse {
        val response = smartBillsApi.postMultiCheckout(DataRequest(request), idempotencyKey)
        if (response.isSuccessful) {
            return response.body()!!.data
        } else {
            throw MessageErrorException(response.errorBody().toString())
        }
    }

    companion object {
        const val ERROR_DEFAULT = "Terjadi Kesalahan, silakan ulang beberapa saat lagi"
    }
}