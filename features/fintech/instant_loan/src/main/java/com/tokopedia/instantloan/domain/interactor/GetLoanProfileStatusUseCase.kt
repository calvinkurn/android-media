package com.tokopedia.instantloan.domain.interactor

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.instantloan.data.model.response.ResponseUserProfileStatus
import com.tokopedia.instantloan.network.InstantLoanAuthInterceptor
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.PATH_USER_PROFILE_STATUS
import com.tokopedia.usecase.RequestParams

class GetLoanProfileStatusUseCase(interceptor: InstantLoanAuthInterceptor, context: Context) : RestRequestSupportInterceptorUseCase(interceptor, context) {

    override fun buildRequest(requestParams: RequestParams): List<RestRequest> {

        val restRequestList = ArrayList<RestRequest>()

        val typeOfT = object : TypeToken<ResponseUserProfileStatus>() {

        }.type

        val restCacheStrategy = RestCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val restRequest = RestRequest.Builder(PATH_USER_PROFILE_STATUS, typeOfT)
                .setRequestType(RequestType.GET)
                .setCacheStrategy(restCacheStrategy)
                .build()

        restRequestList.add(restRequest)
        return restRequestList
    }

}
