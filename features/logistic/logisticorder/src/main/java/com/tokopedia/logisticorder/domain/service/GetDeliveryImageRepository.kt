package com.tokopedia.logisticorder.domain.service

import com.tokopedia.logisticorder.domain.response.GetDeliveryImageResponse
import okhttp3.RequestBody

interface GetDeliveryImageRepository {
//    suspend fun getDeliveryImage(url: String): GetDeliveryImageResponse
    suspend fun getDeliveryImage(params: Map<String, Any>): String
}