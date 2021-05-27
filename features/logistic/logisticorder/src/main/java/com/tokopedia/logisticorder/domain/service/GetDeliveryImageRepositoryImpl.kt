package com.tokopedia.logisticorder.domain.service

import com.tokopedia.logisticorder.domain.response.GetDeliveryImageResponse
import okhttp3.RequestBody

class GetDeliveryImageRepositoryImpl(private val getDeliveryImageDataSource: GetDeliveryImageDataSource) : GetDeliveryImageRepository {

    /*override suspend fun getDeliveryImage(url: String): GetDeliveryImageResponse {
        return getDeliveryImageDataSource.getDeliveryImage(url)
    }*/


    override suspend fun getDeliveryImage(params: Map<String, Any>): GetDeliveryImageResponse {
        return getDeliveryImageDataSource.getDeliveryImage(params)
    }

}