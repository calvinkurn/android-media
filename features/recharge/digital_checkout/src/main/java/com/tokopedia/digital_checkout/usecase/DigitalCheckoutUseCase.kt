package com.tokopedia.digital_checkout.usecase

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.digital_checkout.data.request.RequestBodyCheckout
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.digital_checkout.di.DigitalCartCheckoutQualifier
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type
import javax.inject.Inject

class DigitalCheckoutUseCase @Inject constructor(@DigitalCartCheckoutQualifier val repository: RestRepository)
    : RestRequestUseCase(repository) {

    private val url = DigitalUrl.CHECKOUT
    var requestParams = RequestParams()

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val token = object : TypeToken<DataResponse<ResponseCheckout>>() {}.type

        val requestBodyOtpSuccess = requestParams.getObject(PARAM_REQUEST_CHECKOUT) as RequestBodyCheckout
        val jsonElement = JsonParser().parse(Gson().toJson(requestBodyOtpSuccess))
        val requestBody = JsonObject()
        requestBody.add(PARAM_DATA, jsonElement)

        val mapHeader = mutableMapOf<String, String>()
        mapHeader[KEY_CONTENT_TYPE] = VALUE_CONTENT_TYPE

        val restRequest = RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody(requestBody)
                .setHeaders(mapHeader)
                .build()

        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    fun setRequestParams(requestBodyCheckout: RequestBodyCheckout) {
        requestParams = RequestParams.create()
        requestParams.putObject(PARAM_REQUEST_CHECKOUT, requestBodyCheckout)
        requestParams
    }

    companion object {
        const val PARAM_REQUEST_CHECKOUT = "PARAM_REQUEST_CHECKOUT"

        private const val KEY_CONTENT_TYPE = "Content-Type"
        private const val VALUE_CONTENT_TYPE = "application/json"
        private const val PARAM_DATA = "data"
    }
}