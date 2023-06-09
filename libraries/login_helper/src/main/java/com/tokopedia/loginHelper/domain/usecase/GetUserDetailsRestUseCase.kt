package com.tokopedia.loginHelper.domain.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.loginHelper.data.api.LoginHelperApiService
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.usecase.RequestParams
import retrofit2.Response
import javax.inject.Inject

class GetUserDetailsRestUseCase @Inject constructor(private val repository: RestRepository, private val api: LoginHelperApiService) {

    private val url = "https://172.21.56.230:3224/users?env=staging"
    var requestParams = RequestParams()


    suspend fun makeNetworkCall() : Response<LoginDataResponse>{
        return api.getUserData()
    }

}
