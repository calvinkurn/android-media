package com.tokopedia.loginHelper.data.mapper

import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.data.response.UserDataResponse
import com.tokopedia.loginHelper.domain.uiModel.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.LoginDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.UserDataUiModel

fun LoginDataResponse.toLoginUiModel(): LoginDataUiModel {
    return LoginDataUiModel(
        count = this.count?.toHeaderUiModel(),
        users = this.users?.toUserDataUiModel()
    )
}

fun List<UserDataResponse>.toUserDataUiModel(): List<UserDataUiModel> {
    return this.map {
        UserDataUiModel(it.email, it.password, it.tribe)
    }
}

fun Int.toHeaderUiModel(): HeaderUiModel {
    return HeaderUiModel(this)
}
