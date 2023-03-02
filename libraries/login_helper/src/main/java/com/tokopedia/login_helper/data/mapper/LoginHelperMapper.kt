package com.tokopedia.login_helper.data.mapper

import com.tokopedia.login_helper.data.response.LoginDataResponse
import com.tokopedia.login_helper.data.response.UserDataResponse
import com.tokopedia.login_helper.domain.uiModel.HeaderUiModel
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel
import com.tokopedia.login_helper.domain.uiModel.UserDataUiModel

fun LoginDataResponse.toLoginUiModel(): LoginDataUiModel {
    return LoginDataUiModel(
        count = this.count?.toHeaderUiModel(),
        users = this.users?.toUserDataUiModel()
    )
}

fun List<UserDataResponse>.toUserDataUiModel() : List<UserDataUiModel> {
    return this.map {
        UserDataUiModel(it.email, it.password, it.tribe)
    }
}

fun Int.toHeaderUiModel() : HeaderUiModel {
    return HeaderUiModel(this)
}
