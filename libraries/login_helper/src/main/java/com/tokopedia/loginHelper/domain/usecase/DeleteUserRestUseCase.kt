package com.tokopedia.loginHelper.domain.usecase

import com.tokopedia.loginHelper.data.api.LoginHelperApiService
import com.tokopedia.loginHelper.data.mapper.toLoginHelperDeleteUserUiModel
import com.tokopedia.loginHelper.domain.uiModel.deleteUser.LoginHelperDeleteUserUiModel
import javax.inject.Inject

class DeleteUserRestUseCase @Inject constructor(private val api: LoginHelperApiService) {
    suspend fun makeApiCall(
        envType: String,
        id: Long
    ): LoginHelperDeleteUserUiModel? {
        return api.deleteUser(envType, id).body()?.toLoginHelperDeleteUserUiModel()
    }
}
