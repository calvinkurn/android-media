package com.tokopedia.digital.newcart.presentation.usecase

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCartData
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import okhttp3.Interceptor

class DigitalGetCartUseCase(authInterceptor: List<Interceptor>, context: Context)
    : RestRequestSupportInterceptorUseCase(authInterceptor, context) {

    override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val networkRequest = mutableListOf<RestRequest>()
        val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type

        val url = DigitalUrl.CART

        val restRequest = RestRequest.Builder(url, token)
                .setRequestType(RequestType.GET)
                .setQueryParams(requestParams.parameters)
                .build()
        networkRequest.add(restRequest)
        return networkRequest
    }

    fun createRequestParams(categoryId: String, userId: String, deviceId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_CATEGORY_ID, categoryId)
        requestParams.putString(PARAM_USER_ID, userId)
        requestParams.putString(PARAM_DEVICE_ID, deviceId)
        requestParams.putString(PARAM_OS_TYPE, VALUE_OS_TYPE)
        return requestParams
    }

    companion object {
        private const val PARAM_CATEGORY_ID = "category_id"
        private const val PARAM_USER_ID = "user_id"
        private const val PARAM_DEVICE_ID = "device_id"
        private const val PARAM_OS_TYPE = "os_type"
        private const val VALUE_OS_TYPE = "1"
    }
}