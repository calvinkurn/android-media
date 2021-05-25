package com.tokopedia.logisticorder.usecase

import android.content.Context
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.logisticorder.domain.response.GetDeliveryImageResponse
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.RequestParams
import okhttp3.Interceptor

class GetDeliveryImageUseCase (authInterceptor: List<Interceptor>, context: Context) : RestRequestSupportInterceptorUseCase(authInterceptor, context) {

    override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val networkRequest = mutableListOf<RestRequest>()

        val restRequest = RestRequest.Builder(TokopediaUrl.getInstance().API + "logistic/tracking/get-delivery-image", GetDeliveryImageResponse::class.java)
                .setRequestType(RequestType.GET)
                .setQueryParams(requestParams.parameters)
                .build()
        networkRequest.add(restRequest)
        return networkRequest
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