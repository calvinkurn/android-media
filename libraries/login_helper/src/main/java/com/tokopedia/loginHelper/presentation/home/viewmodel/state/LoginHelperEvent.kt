package com.tokopedia.loginHelper.presentation.home.viewmodel.state

import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperDataSourceType
import com.tokopedia.loginHelper.domain.LoginHelperEnvType

sealed class LoginHelperEvent {
    data class ChangeEnvType(val envType: LoginHelperEnvType) : LoginHelperEvent()
    object TapBackButton : LoginHelperEvent()
    object GetRemoteLoginData : LoginHelperEvent()
    data class GetLocalLoginData(val userDetails: LoginDataResponse) : LoginHelperEvent()
    data class LoginUser(val email: String, val password: String, val useHash: Boolean = true) : LoginHelperEvent()
    data class QueryEmail(val email: String) : LoginHelperEvent()
    object GetUserInfo : LoginHelperEvent()
    object GoToLoginPage : LoginHelperEvent()
    object LogOutUser : LoginHelperEvent()
    object GoToAccountsSetting : LoginHelperEvent()
    data class ChangeDataSourceType(val dataSourceType: LoginHelperDataSourceType) : LoginHelperEvent()

    data class HandleLoader(val state: Boolean): LoginHelperEvent()
}
