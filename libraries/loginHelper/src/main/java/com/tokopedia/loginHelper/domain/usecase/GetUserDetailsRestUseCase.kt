package com.tokopedia.loginHelper.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type
import javax.inject.Inject

class GetUserDetailsRestUseCase @Inject constructor(private val repository: RestRepository)
    :RestRequestUseCase(repository){

    private val url = "http://172.21.56.230:3123/users?env=staging"
    var requestParams = RequestParams()

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {

        val token = object: TypeToken<LoginDataResponse>() {}.type

        val restRequest = RestRequest.Builder(url, token)
            .setRequestType(RequestType.GET)
            .setHeaders(mapOf(Pair("Content-Type", "application/x-www-form-urlencoded")))
            .build()

        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

}
