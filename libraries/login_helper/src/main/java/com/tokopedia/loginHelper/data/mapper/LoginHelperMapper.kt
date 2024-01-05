package com.tokopedia.loginHelper.data.mapper

import com.tokopedia.loginHelper.data.body.LoginHelperAddUserBody
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.data.response.LoginHelperAddUserResponse
import com.tokopedia.loginHelper.data.response.LoginHelperDeleteUserResponse
import com.tokopedia.loginHelper.data.response.UserDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.pojo.LoginHelperAddUserPojo
import com.tokopedia.loginHelper.domain.uiModel.addedit.LoginHelperAddUserUiModel
import com.tokopedia.loginHelper.domain.uiModel.deleteUser.LoginHelperDeleteUserUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel

fun LoginDataResponse.toLoginUiModel(): LoginDataUiModel {
    return LoginDataUiModel(
        count = this.count?.toRemoteUserHeaderUiModel(),
        users = this.users?.toUserDataUiModel()
    )
}

fun List<UserDataResponse>.toUserDataUiModel(): List<UserDataUiModel> {
    return this.map {
        UserDataUiModel(it.email, it.password, it.tribe, it.id)
    }
}

fun Int.toLocalUserHeaderUiModel(): HeaderUiModel {
    return HeaderUiModel(this, "Local Users")
}

fun Int.toRemoteUserHeaderUiModel(): HeaderUiModel {
    return HeaderUiModel(this, "Remote Users")
}

fun Int.toSearchResultsUserHeaderUiModel(): HeaderUiModel {
    return HeaderUiModel(this, "Search Results")
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
        this.addUserData?.toUiModel()
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

fun LoginHelperDeleteUserResponse.toLoginHelperDeleteUserUiModel(): LoginHelperDeleteUserUiModel {
    return LoginHelperDeleteUserUiModel(
        this.message,
        this.code
    )
}
