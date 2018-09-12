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
import okhttp3.Interceptor

class AddCreditCardUseCase(authInterceptor : List<Interceptor>, context: Context) : RestRequestSupportInterceptorUseCase(authInterceptor, context) {

    override fun buildRequest(): MutableList<RestRequest> {
        val tempRequest = ArrayList<RestRequest>()

        val restRequest1 = RestRequest.Builder(PAYMENT_SETTING_URL + GET_IFRAME_ADD_CC_URL, ResponseGetIFrameCreditCard::class.java)
                .setBody(HashMap<String, Any>())
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restRequest1)

        return tempRequest
    }
}