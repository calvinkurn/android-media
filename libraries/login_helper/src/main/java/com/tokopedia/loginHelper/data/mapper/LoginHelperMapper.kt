package com.tokopedia.loginHelper.data.mapper

import com.tokopedia.loginHelper.data.body.LoginHelperAddUserBody
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.data.response.LoginHelperAddUserResponse
import com.tokopedia.loginHelper.data.response.UserDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.pojo.LoginHelperAddUserPojo
import com.tokopedia.loginHelper.domain.uiModel.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.LoginDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.LoginHelperAddUserUiModel
import com.tokopedia.loginHelper.domain.uiModel.UserDataUiModel

fun LoginDataResponse.toLoginUiModel(): LoginDataUiModel {
    return LoginDataUiModel(
        count = this.count?.toLocalUserHeaderUiModel(),
        users = this.users?.toUserDataUiModel()
    )
}

fun List<UserDataResponse>.toUserDataUiModel(): List<UserDataUiModel> {
    return this.map {
        UserDataUiModel(it.email, it.password, it.tribe)
    }
}

fun Int.toLocalUserHeaderUiModel(): HeaderUiModel {
    return HeaderUiModel(this, "Local Users")
}

fun Int.toRemoteUserHeaderUiModel(): HeaderUiModel {
    return HeaderUiModel(this, "Remote Users")
}

fun LoginHelperEnvType.toEnvString(): String {
    return if (this == LoginHelperEnvType.STAGING) {
        "staging"
    } else {
        "production"
    }
}

fun ArrayList<UserDataUiModel>.toUserDataResponse(): List<UserDataResponse> {
    return this.map {
        UserDataResponse(
            it.email,
            it.password
        )
    }
}

fun LoginHelperAddUserPojo.toAddUserBody(): LoginHelperAddUserBody {
    return LoginHelperAddUserBody(
        this.email,
        this.password,
        this.tribe
    )
}

fun LoginHelperAddUserResponse.toUiModel(): LoginHelperAddUserUiModel {
    return LoginHelperAddUserUiModel(
        this.message,
        this.code,
        this.addUserData.toUiModel()
    )
}

fun LoginHelperAddUserResponse.LoginHelperAddUserData.toUiModel(): LoginHelperAddUserUiModel.LoginHelperAddUserData {
    return LoginHelperAddUserUiModel.LoginHelperAddUserData(
        this.email,
        this.password,
        this.tribe,
        this.id
    )
}
