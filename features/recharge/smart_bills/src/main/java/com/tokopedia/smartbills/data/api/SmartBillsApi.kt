package com.tokopedia.smartbills.data.api

import com.tokopedia.common_digital.common.data.request.DataRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.smartbills.data.MultiCheckoutRequest
import com.tokopedia.smartbills.data.RechargeMultiCheckoutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface SmartBillsApi {

    @POST(PATH_MULTI_CHECKOUT)
    @Headers("Content-Type: application/json")
    suspend fun postMultiCheckout(
            @Body request: DataRequest<MultiCheckoutRequest>,
            @Header("Idempotency-Key") idemPotencyKey: String
    ): Response<DataResponse<RechargeMultiCheckoutResponse>>

    companion object {
        const val PATH_MULTI_CHECKOUT = "checkout/multi-item"
    }
}