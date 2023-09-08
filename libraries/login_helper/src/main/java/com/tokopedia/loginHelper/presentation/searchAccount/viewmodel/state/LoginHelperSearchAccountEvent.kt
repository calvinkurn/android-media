package com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state

import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperEnvType

sealed class LoginHelperSearchAccountEvent {
    data class SetEnvType(val envType: LoginHelperEnvType) : LoginHelperSearchAccountEvent()
    object TapBackButton : LoginHelperSearchAccountEvent()
    data class QueryEmail(val email: String) : LoginHelperSearchAccountEvent()
    data class GetUserData(val userDetails: LoginDataResponse) : LoginHelperSearchAccountEvent()

    data class DeleteUserDetailsFromRemote(val id: Long) : LoginHelperSearchAccountEvent()
}
