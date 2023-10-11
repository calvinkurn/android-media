package com.tokopedia.loginHelper.domain.usecase

import com.tokopedia.loginHelper.data.api.LoginHelperApiService
import com.tokopedia.loginHelper.data.mapper.toAddUserBody
import com.tokopedia.loginHelper.data.mapper.toUiModel
import com.tokopedia.loginHelper.domain.pojo.LoginHelperAddUserPojo
import com.tokopedia.loginHelper.domain.uiModel.addedit.LoginHelperAddUserUiModel
import javax.inject.Inject

class EditUserRestUseCase @Inject constructor(private val api: LoginHelperApiService) {
    suspend fun makeApiCall(
        email: String,
        password: String,
        tribe: String,
        envType: String,
        id: Long
    ): LoginHelperAddUserUiModel? {
        val addUserBody = LoginHelperAddUserPojo(email, password, tribe).toAddUserBody()
        return api.editUser(addUserBody, envType, id).body()?.toUiModel()
    }
}
