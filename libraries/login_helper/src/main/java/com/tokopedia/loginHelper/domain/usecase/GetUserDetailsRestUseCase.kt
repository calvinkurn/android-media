package com.tokopedia.loginHelper.domain.usecase

import com.tokopedia.loginHelper.data.api.LoginHelperApiService
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import retrofit2.Response
import javax.inject.Inject

class GetUserDetailsRestUseCase @Inject constructor(private val api: LoginHelperApiService) {

    suspend fun makeNetworkCall(envType: String): Response<LoginDataResponse> {
        return api.getUserData(envType)
    }
}
