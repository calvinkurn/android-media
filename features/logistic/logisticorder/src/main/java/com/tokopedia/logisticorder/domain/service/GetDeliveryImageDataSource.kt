package com.tokopedia.logisticorder.domain.service

import com.tokopedia.logisticCommon.data.apiservice.TrackingOrderApi
import com.tokopedia.logisticorder.domain.response.GetDeliveryImageResponse
import okhttp3.RequestBody
import rx.Observable
import javax.inject.Inject

class GetDeliveryImageDataSource @Inject constructor(private val getDeliveryImageApi: GetDeliveryImageApi)  {

   /* suspend fun getDeliveryImage(url: String): GetDeliveryImageResponse =
            getDeliveryImageApi.getDeliveryImage(url).let {
                it.body()?.data ?: GetDeliveryImageResponse()
            }*/

    suspend fun getDeliveryImage(params: Map<String, Any>): String =
            getDeliveryImageApi.getDeliveryImage(params).let {
                it.body()?.data ?: String()
            }
}

