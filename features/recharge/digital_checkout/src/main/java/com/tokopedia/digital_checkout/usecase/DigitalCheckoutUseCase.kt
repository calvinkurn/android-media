package com.tokopedia.digital_checkout.usecase

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.digital_checkout.data.request.RequestBodyCheckout
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import okhttp3.Interceptor
import javax.inject.Inject

class DigitalCheckoutUseCase @Inject constructor(authInterceptor: ArrayList<Interceptor>,
                                                 @ApplicationContext context: Context)
    : RestRequestSupportInterceptorUseCase(authInterceptor, context) {

    override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val networkRequest = mutableListOf<RestRequest>()
        val token = object : TypeToken<DataResponse<ResponseCheckout>>() {}.type

        val url = DigitalUrl.CHECKOUT

        val requestBodyOtpSuccess = requestParams.getObject(PARAM_REQUEST_CHECKOUT) as RequestBodyCheckout
        val jsonElement = JsonParser().parse(Gson().toJson(requestBodyOtpSuccess))
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
        requestParams.putObject(PARAM_REQUEST_CHECKOUT, requestBodyCheckout)
        return requestParams
    }

    companion object {
        const val PARAM_REQUEST_CHECKOUT = "PARAM_REQUEST_CHECKOUT"

        private const val KEY_CONTENT_TYPE = "Content-Type"
        private const val VALUE_CONTENT_TYPE = "application/json"
    }
}