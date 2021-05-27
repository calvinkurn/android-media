package com.tokopedia.logisticorder.domain.service

import com.tokopedia.logisticorder.domain.response.GetDeliveryImageResponse
import com.tokopedia.logisticorder.utils.TrackingPageUrl
import com.tokopedia.network.data.model.response.DataResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface GetDeliveryImageApi {

 /*   @GET
    @Headers("Content-Type: application/json")
    suspend fun getDeliveryImage(@Url url: String): Response<DataResponse<GetDeliveryImageResponse>>
*/
    @GET("logistic/tracking/get-delivery-image")
    @Headers("Content-Type: application/json")
    suspend fun getDeliveryImage(@QueryMap params: Map<String, Any>): Response<DataResponse<GetDeliveryImageResponse>>



}