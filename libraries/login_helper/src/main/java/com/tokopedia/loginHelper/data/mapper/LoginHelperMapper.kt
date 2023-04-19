package com.tokopedia.loginHelper.data.mapper

import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.data.response.UserDataResponse
import com.tokopedia.loginHelper.domain.uiModel.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.LoginDataUiModel
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

fun ArrayList<UserDataUiModel>.toUserDataResponse(): List<UserDataResponse> {
    return this.map {
        UserDataResponse(
            it.email,
            it.password
        )
    }
}
