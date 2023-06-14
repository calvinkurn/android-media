package com.tokopedia.loginHelper.domain.uiModel.addedit

data class LoginHelperAddUserUiModel(
    val message: String? = null,
    val code: Long? = null,
    val addUserData: LoginHelperAddUserData? = null
) {
    data class LoginHelperAddUserData(
        val email: String? = null,
        val password: String? = null,
        val tribe: String? = null,
        val id: String? = null
    )
}
