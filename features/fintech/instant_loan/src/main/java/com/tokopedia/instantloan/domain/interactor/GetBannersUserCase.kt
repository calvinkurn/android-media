package com.tokopedia.instantloan.domain.interactor

import android.content.Context

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.instantloan.data.model.response.ResponseBannerOffer
import com.tokopedia.instantloan.network.InstantLoanUrl
import com.tokopedia.usecase.RequestParams

import java.lang.reflect.Type
import java.util.ArrayList

import okhttp3.Interceptor

class GetBannersUserCase(interceptor: Interceptor, context: Context) : RestRequestSupportInterceptorUseCase(interceptor, context) {

    override fun buildRequest(requestParams: RequestParams): List<RestRequest> {

        val restRequestList = ArrayList<RestRequest>()

        val typeOfT = object : TypeToken<ResponseBannerOffer>() {

        }.type

        val restCacheStrategy = RestCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
        val restRequest = RestRequest.Builder(InstantLoanUrl.COMMON_URL.PATH_BANNER_OFFER, typeOfT)
                .setCacheStrategy(restCacheStrategy)
                .build()

        restRequestList.add(restRequest)
        return restRequestList

    }
}
