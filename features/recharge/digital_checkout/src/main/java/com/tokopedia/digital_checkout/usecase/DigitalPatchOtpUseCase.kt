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
import com.tokopedia.digital_checkout.data.request.RequestBodyOtpSuccess
import com.tokopedia.digital_checkout.data.response.ResponsePatchOtpSuccess
import com.tokopedia.digital_checkout.di.DigitalCartCheckoutQualifier
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type
import javax.inject.Inject

class DigitalPatchOtpUseCase @Inject constructor(@DigitalCartCheckoutQualifier val repository: RestRepository)
    : RestRequestUseCase(repository) {

    private val url = DigitalUrl.OTP
    var requestParams = RequestParams()

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val token = object : TypeToken<DataResponse<ResponsePatchOtpSuccess>>() {}.type

        val requestBodyOtpSuccess = requestParams.getObject(PARAM_REQUEST_OTP_SUCCESS) as RequestBodyOtpSuccess
        val jsonElement = JsonParser().parse(Gson().toJson(requestBodyOtpSuccess))
        val requestBody = JsonObject()
        requestBody.add("data", jsonElement)

        val mapHeader = mutableMapOf<String, String>()
        mapHeader[KEY_CONTENT_TYPE] = VALUE_CONTENT_TYPE

        val restRequest = RestRequest.Builder(url, token)
                .setRequestType(RequestType.PATCH)
                .setBody(requestBody)
                .setHeaders(mapHeader)
                .build()

        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    fun setRequestParams(requestBodyOtpSuccess: RequestBodyOtpSuccess) {
        requestParams = RequestParams.create()
        requestParams.putObject(PARAM_REQUEST_OTP_SUCCESS, requestBodyOtpSuccess)
    }

    companion object {
        const val PARAM_REQUEST_OTP_SUCCESS = "PARAM_REQUEST_OTP_SUCCESS"

        private const val KEY_CONTENT_TYPE = "Content-Type"
        private const val VALUE_CONTENT_TYPE = "application/json"
    }
}