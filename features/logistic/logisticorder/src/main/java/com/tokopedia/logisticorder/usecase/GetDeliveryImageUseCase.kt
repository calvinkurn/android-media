package com.tokopedia.logisticorder.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.logisticorder.domain.response.GetDeliveryImageResponse
import com.tokopedia.logisticorder.utils.TrackingPageUtil
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type

class GetDeliveryImageUseCase (private val repository: RestRepository) : RestRequestUseCase(repository) {

    /*override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val networkRequest = mutableListOf<RestRequest>()

        val restRequest = RestRequest.Builder(TokopediaUrl.getInstance().API + "logistic/tracking/get-delivery-image", GetDeliveryImageResponse::class.java)
                .setRequestType(RequestType.GET)
                .setQueryParams(requestParams.parameters)
                .build()
        networkRequest.add(restRequest)
        return networkRequest
    }*/

    var url: String = ""

    fun getDeliveryImageUrl(imageId: String, orderId: Long, size: String) {
        val baseUrl = TokopediaUrl.getInstance().API + TrackingPageUtil.PATH_IMAGE_LOGISTIC
        url =  "$baseUrl?order_id=$orderId&image_id=$imageId&size=$size"
    }

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, GetDeliveryImageResponse::class.java)
                .setRequestType(RequestType.GET)
                .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    fun createRequestParams(imageId: String, order_id: Long, size: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_IMAGE_ID, imageId)
        requestParams.putLong(PARAM_ORDER_ID, order_id)
        requestParams.putString(PARAM_SIZE, size)
        return requestParams
    }

    companion object {
        private const val PARAM_IMAGE_ID = "image_id"
        private const val PARAM_ORDER_ID = "order_id"
        private const val PARAM_SIZE = "size"
    }

}