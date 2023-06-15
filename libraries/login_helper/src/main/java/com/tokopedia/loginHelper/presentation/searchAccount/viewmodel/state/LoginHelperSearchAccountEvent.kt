package com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state

import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperEnvType

sealed class LoginHelperSearchAccountEvent {
    data class SetEnvType(val envType: LoginHelperEnvType) : LoginHelperSearchAccountEvent()
    object TapBackButton : LoginHelperSearchAccountEvent()
    object GetSearchAccountLoginData : LoginHelperSearchAccountEvent()
    data class QueryEmail(val email: String) : LoginHelperSearchAccountEvent()
    object GetUserInfo : LoginHelperSearchAccountEvent()
    object GoToLoginPageSearchAccount : LoginHelperSearchAccountEvent()
    data class SaveUserDetailsFromAssets(val userDetails: LoginDataResponse) : LoginHelperSearchAccountEvent()

    data class DeleteUserDetailsFromRemote(val id: Long): LoginHelperSearchAccountEvent()
}
