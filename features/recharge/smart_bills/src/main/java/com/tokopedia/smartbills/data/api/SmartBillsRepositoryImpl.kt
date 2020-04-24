package com.tokopedia.smartbills.data.api

import com.tokopedia.smartbills.data.RechargeMultiCheckoutResponse
import java.io.IOException
import javax.inject.Inject

class SmartBillsRepositoryImpl @Inject constructor(private val smartBillsApi: SmartBillsApi) : SmartBillsRepository {

    override suspend fun postMultiCheckout(mapParam: Map<String, Any>,
                                           idempotencyKey: String,
                                           userId: String): RechargeMultiCheckoutResponse {
        val response = smartBillsApi.postMultiCheckout(mapParam, idempotencyKey, userId)
        if (response.isSuccessful) {
            return response.body()!!.data
        } else {
            throw IOException(ERROR_DEFAULT)
        }
    }

    companion object {
        const val ERROR_DEFAULT = "Terjadi Kesalahan, silakan ulang beberapa saat lagi"
    }
}