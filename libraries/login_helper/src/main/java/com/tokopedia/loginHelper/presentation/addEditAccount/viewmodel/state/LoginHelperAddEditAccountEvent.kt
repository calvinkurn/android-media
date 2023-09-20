package com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state

import com.tokopedia.loginHelper.domain.LoginHelperEnvType

sealed class LoginHelperAddEditAccountEvent {
    object TapBackButton : LoginHelperAddEditAccountEvent()

    data class ChangeEnvType(val envType: LoginHelperEnvType) : LoginHelperAddEditAccountEvent()
    data class AddUserToRemoteDB(val email: String, val password: String, val tribe: String = "") :
        LoginHelperAddEditAccountEvent()

    data class AddUserToLocalDB(val email: String, val password: String) :
        LoginHelperAddEditAccountEvent()

    data class EditUserDetailsFromRemote(
        val email: String = "",
        val password: String = "",
        val tribe: String = "",
        val id: Long = 0
    ): LoginHelperAddEditAccountEvent()
}
