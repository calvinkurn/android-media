package com.tokopedia.promocheckout.common.domain

import android.content.Context
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.usecase.RequestParams
import okhttp3.Interceptor

class CancelPromoUseCase(authInterceptor : List<Interceptor>, context: Context) : RestRequestSupportInterceptorUseCase(authInterceptor, context) {

    val ANDROID = "android"
    val PATH_CANCEL_AUTO_APPLY_COUPON = "cart/v2/auto_applied_kupon/clear"

    override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val tempRequest = arrayListOf<RestRequest>()

        val params = HashMap<String, Any>()
        params.put(AuthUtil.HEADER_DEVICE, ANDROID)
        val restRequest1 = RestRequest.Builder(TkpdBaseURL.BASE_API_DOMAIN + PATH_CANCEL_AUTO_APPLY_COUPON, String::class.java)
                .setHeaders(params)
                .setBody(HashMap<String, Any>())
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restRequest1)

        return tempRequest
    }

}