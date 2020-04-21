package com.tokopedia.smartbills.data.api

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.smartbills.data.RechargeMultiCheckoutResponse
import retrofit2.Response
import retrofit2.http.*

interface SmartBillsApi {

    @FormUrlEncoded
    @POST(PATH_MULTI_CHECKOUT)
    @Headers("Content-Type: application/json")
    suspend fun postMultiCheckout(
            @FieldMap map: MutableMap<String, Any>,
            @Header("Idempotency-Key") idemPotencyKey: String,
            @Header("X-Tkpd-UserId") userId: String
    ): Response<DataResponse<RechargeMultiCheckoutResponse>>

    companion object {
        const val PATH_MULTI_CHECKOUT = "v1.4/checkout/multi-item"
    }
}