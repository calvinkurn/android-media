package com.tokopedia.loginHelper.domain.usecase

import com.tokopedia.loginHelper.data.api.LoginHelperApiService
import com.tokopedia.loginHelper.data.mapper.toAddUserBody
import com.tokopedia.loginHelper.data.mapper.toUiModel
import com.tokopedia.loginHelper.domain.pojo.LoginHelperAddUserPojo
import com.tokopedia.loginHelper.domain.uiModel.LoginHelperAddUserUiModel
import javax.inject.Inject

class AddUserRestUseCase @Inject constructor(private val api: LoginHelperApiService) {

    suspend fun makeApiCall(email: String, password: String, tribe: String, envType: String): LoginHelperAddUserUiModel? {
        val addUserBody = LoginHelperAddUserPojo(email, password, tribe).toAddUserBody()
        return api.addUser(addUserBody, envType).body()?.toUiModel()
    }
}
