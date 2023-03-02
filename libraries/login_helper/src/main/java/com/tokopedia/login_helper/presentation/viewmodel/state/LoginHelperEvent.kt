package com.tokopedia.login_helper.presentation.viewmodel.state

import com.tokopedia.login_helper.domain.LoginHelperEnvType

sealed class LoginHelperEvent {
    data class ChangeEnvType(val envType: LoginHelperEnvType) : LoginHelperEvent()
    object TapBackButton : LoginHelperEvent()
    object GetLoginData : LoginHelperEvent()
    data class LoginUser(val email: String, val password: String, val useHash: Boolean = true) : LoginHelperEvent()
    data class QueryEmail(val email: String) : LoginHelperEvent()
    object GetUserInfo : LoginHelperEvent()
    object GoToLoginPage : LoginHelperEvent()
}
