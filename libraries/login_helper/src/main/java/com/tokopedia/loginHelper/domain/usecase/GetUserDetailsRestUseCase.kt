package com.tokopedia.loginHelper.domain.usecase

import com.tokopedia.loginHelper.data.repository.LoginHelperRepositoryImpl
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.UnifiedLoginHelperData
import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class GetUserDetailsRestUseCase @Inject constructor(private val repository: LoginHelperRepositoryImpl) {

    suspend fun makeNetworkCall(envType: LoginHelperEnvType): UnifiedLoginHelperData {
        return repository.getUnifiedLoginData(envType)
    }

    fun getUserDataFromLocalAssets(loginData: LoginDataResponse): Result<UnifiedLoginHelperData> {
        return repository.getLoginDataFromAssets(loginData)
    }

    suspend fun getRemoteOnlyLoginData(envType: LoginHelperEnvType): LoginDataUiModel? {
        return repository.getRemoteOnlyData(envType)
    }
}
