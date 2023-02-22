package com.tokopedia.login_helper.data.mapper

import com.tokopedia.login_helper.data.response.LoginDataResponse
import com.tokopedia.login_helper.data.response.UserDataResponse
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel
import com.tokopedia.login_helper.domain.uiModel.UserDataUiModel

fun LoginDataResponse.toLoginUiModel(): LoginDataUiModel {
    return LoginDataUiModel(
        count = this.count,
        users = this.users?.toUserDataUiModel()
    )
}

fun List<UserDataResponse>.toUserDataUiModel() : List<UserDataUiModel> {
    return this.map {
        UserDataUiModel(it.email, it.password, it.tribe)
    }
}
