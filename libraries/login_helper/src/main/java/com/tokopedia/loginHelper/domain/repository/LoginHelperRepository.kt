package com.tokopedia.loginHelper.domain.repository

import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.UnifiedLoginHelperData
import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel

interface LoginHelperRepository {

    suspend fun getUnifiedLoginData(envType: LoginHelperEnvType): UnifiedLoginHelperData

    suspend fun getRemoteLoginData(envType: LoginHelperEnvType): LoginDataUiModel?

    fun getLocalLoginData(envType: LoginHelperEnvType): LoginDataUiModel

    fun getLoginDataFromAssets(loginData: LoginDataResponse): com.tokopedia.usecase.coroutines.Result<UnifiedLoginHelperData>

    suspend fun getRemoteOnlyData(envType: LoginHelperEnvType): LoginDataUiModel?
}
