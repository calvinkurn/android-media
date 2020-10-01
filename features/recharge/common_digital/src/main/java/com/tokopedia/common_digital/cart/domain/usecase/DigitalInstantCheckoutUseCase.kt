package com.tokopedia.common_digital.cart.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCartData
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import okhttp3.Interceptor

class DigitalInstantCheckoutUseCase(authInterceptor: List<Interceptor>, context: Context)
    : RestRequestSupportInterceptorUseCase(authInterceptor, context) {

    override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val networkRequest = mutableListOf<RestRequest>()
        val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type

        val url = DigitalUrl.CHECKOUT

        val requestBodyCheckout = requestParams.getObject(PARAM_REQUEST_BODY_CHECKOUT) as RequestBodyCheckout
        val jsonElement = JsonParser().parse(Gson().toJson(requestBodyCheckout))
        val requestBody = JsonObject()
        requestBody.add("data", jsonElement)

        val mapHeader = mutableMapOf<String, String>()
        mapHeader[KEY_CONTENT_TYPE] = VALUE_CONTENT_TYPE

        val restRequest = RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody(requestBody)
                .setHeaders(mapHeader)
                .build()
        networkRequest.add(restRequest)
        return networkRequest
    }

    fun createRequestParams(requestBodyCheckout: RequestBodyCheckout): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_REQUEST_BODY_CHECKOUT, requestBodyCheckout)
        return requestParams
    }

    companion object {
        private const val PARAM_REQUEST_BODY_CHECKOUT = "PARAM_REQUEST_BODY_CHECKOUT"
        private const val KEY_CONTENT_TYPE = "Content-Type"
        private const val VALUE_CONTENT_TYPE = "application/json"
    }
}