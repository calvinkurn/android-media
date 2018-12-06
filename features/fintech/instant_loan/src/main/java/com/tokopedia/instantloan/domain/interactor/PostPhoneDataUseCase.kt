package com.tokopedia.instantloan.domain.interactor

import android.content.Context

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.instantloan.data.model.response.PhoneDataEntity
import com.tokopedia.instantloan.data.model.response.ResponsePhoneData
import com.tokopedia.instantloan.network.InstantLoanUrl
import com.tokopedia.usecase.RequestParams

import java.lang.reflect.Type
import java.util.ArrayList

import okhttp3.Interceptor

class PostPhoneDataUseCase(interceptor: Interceptor, context: Context) : RestRequestSupportInterceptorUseCase(interceptor, context) {
    private var body: JsonObject? = null

    override fun buildRequest(requestParams: RequestParams): List<RestRequest> {

        val restRequestList = ArrayList<RestRequest>()
        val typeOfT = object : TypeToken<ResponsePhoneData>() {

        }.type

        val restCacheStrategy = RestCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val restRequest = RestRequest.Builder(InstantLoanUrl.PATH_POST_PHONEDATA, typeOfT)
                .setRequestType(RequestType.POST)
                .setCacheStrategy(restCacheStrategy)
                .setBody(this.body)
                .build()

        restRequestList.add(restRequest)
        return restRequestList

    }

    fun setBody(body: JsonObject) {
        this.body = body
    }
}
