package com.tokopedia.buyerorder.detail.domain

import android.content.Context
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.buyerorder.detail.data.SendEventEmail
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.RequestParams
import okhttp3.Interceptor

class SendEventNotificationUseCase (interceptor : List<Interceptor>, context: Context) : RestRequestSupportInterceptorUseCase(interceptor, context) {

    var path : String = ""
    var body : HashMap<String, String> = hashMapOf()

    override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val tempRequest = arrayListOf<RestRequest>()

        val restRequest = RestRequest.Builder(path, SendEventEmail::class.java)
                .setBody(body)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restRequest)

        return tempRequest
    }

}