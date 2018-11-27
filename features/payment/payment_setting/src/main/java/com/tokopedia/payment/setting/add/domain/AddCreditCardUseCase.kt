package com.tokopedia.payment.setting.add.domain

import android.content.Context
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.payment.setting.add.model.ResponseGetIFrameCreditCard
import com.tokopedia.payment.setting.detail.model.DataResponseDeleteCC
import com.tokopedia.payment.setting.util.GET_IFRAME_ADD_CC_URL
import com.tokopedia.payment.setting.util.PAYMENT_SETTING_URL
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import okhttp3.Interceptor

class AddCreditCardUseCase(authInterceptor : List<Interceptor>, context: Context, val userSession: UserSession) : RestRequestSupportInterceptorUseCase(authInterceptor, context) {

    override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val tempRequest = ArrayList<RestRequest>()

        val params = HashMap<String, Any>()
        params.put(USER_ID, userSession.userId)
        params.put(DEVICE, ANDROID)
        val restRequest1 = RestRequest.Builder(PAYMENT_SETTING_URL + GET_IFRAME_ADD_CC_URL, ResponseGetIFrameCreditCard::class.java)
                .setQueryParams(params)
                .setBody(HashMap<String, Any>())
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restRequest1)

        return tempRequest
    }

    companion object {
        val USER_ID = "user_id"
        val DEVICE = "device"
        val ANDROID = "android"
    }
}