package com.tokopedia.loginHelper.domain.uiModel.users

data class LoginDataUiModel(
    val count: HeaderUiModel? = HeaderUiModel(0,""),
    val users: List<UserDataUiModel>? = listOf()
)
