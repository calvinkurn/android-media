package com.tokopedia.digital.newcart.presentation.usecase

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.digital.newcart.data.entity.requestbody.atc.RequestBodyAtcDigital
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCartData
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import okhttp3.Interceptor

class DigitalAddToCartUseCase(authInterceptor: List<Interceptor>, context: Context)
    : RestRequestSupportInterceptorUseCase(authInterceptor, context) {

    override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val networkRequest = mutableListOf<RestRequest>()
        val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type

        val url = DigitalUrl.CART

        val requestBodyAtcDigital = requestParams.getObject(PARAM_REQUEST_BODY_ATC_DIGITAL) as RequestBodyAtcDigital
        val jsonElement = JsonParser().parse(Gson().toJson(requestBodyAtcDigital))
        val requestBody = JsonObject()
        requestBody.add("data", jsonElement)

        val idemPotencyKeyHeader = requestParams.getString(PARAM_IDEM_POTENCY_KEY, "")
        val mapHeader = mutableMapOf<String, String>()
        mapHeader[KEY_IDEM_POTENCY_KEY] = idemPotencyKeyHeader
        mapHeader[KEY_CONTENT_TYPE] = VALUE_CONTENT_TYPE

        val restRequest = RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody(requestBody)
                .setHeaders(mapHeader)
                .build()
        networkRequest.add(restRequest)
        return networkRequest
    }

    fun createRequestParams(requestBodyAtcDigital: RequestBodyAtcDigital?,
                            idemPotencyKeyHeader: String?): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_REQUEST_BODY_ATC_DIGITAL, requestBodyAtcDigital)
        requestParams.putString(PARAM_IDEM_POTENCY_KEY, idemPotencyKeyHeader)
        return requestParams
    }

    companion object {
        private const val PARAM_REQUEST_BODY_ATC_DIGITAL = "PARAM_REQUEST_BODY_ATC_DIGITAL"
        private const val PARAM_IDEM_POTENCY_KEY = "PARAM_IDEM_POTENCY_KEY"
        private const val KEY_IDEM_POTENCY_KEY = "Idempotency-Key"
        private const val KEY_CONTENT_TYPE = "Content-Type"
        private const val VALUE_CONTENT_TYPE = "application/json"
    }
}