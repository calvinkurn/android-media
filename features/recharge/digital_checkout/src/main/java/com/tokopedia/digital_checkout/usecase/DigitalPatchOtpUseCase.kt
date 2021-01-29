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
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse
import com.tokopedia.digital_checkout.data.request.RequestBodyOtpSuccess
import com.tokopedia.digital_checkout.data.response.ResponsePatchOtpSuccess
import com.tokopedia.digital_checkout.di.DigitalCartCheckoutQualifier
import com.tokopedia.digital_checkout.di.DigitalCartQualifier
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import okhttp3.Interceptor
import javax.inject.Inject

class DigitalPatchOtpUseCase @Inject constructor(@DigitalCartCheckoutQualifier val authInterceptor: ArrayList<Interceptor>,
                                                 @ApplicationContext val context: Context)
    : RestRequestSupportInterceptorUseCase(authInterceptor, context) {

    override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val networkRequest = mutableListOf<RestRequest>()
        val token = object : TypeToken<DataResponse<ResponsePatchOtpSuccess>>() {}.type

        val url = DigitalUrl.OTP

        val requestBodyOtpSuccess = requestParams.getObject(PARAM_REQUEST_OTP_SUCCESS) as RequestBodyOtpSuccess
        val jsonElement = JsonParser().parse(Gson().toJson(requestBodyOtpSuccess))
        val requestBody = JsonObject()
        requestBody.add("data", jsonElement)

        val restRequest = RestRequest.Builder(url, token)
                .setRequestType(RequestType.PATCH)
                .setBody(requestBody)
                .build()
        networkRequest.add(restRequest)
        return networkRequest
    }

    fun createRequestParams(requestBodyOtpSuccess: RequestBodyOtpSuccess): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_REQUEST_OTP_SUCCESS, requestBodyOtpSuccess)
        return requestParams
    }

    companion object {
        const val PARAM_REQUEST_OTP_SUCCESS = "PARAM_REQUEST_OTP_SUCCESS"
    }
}