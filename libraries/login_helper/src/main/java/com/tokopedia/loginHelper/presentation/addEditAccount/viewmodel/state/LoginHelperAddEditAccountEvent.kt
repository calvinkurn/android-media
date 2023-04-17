package com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state

sealed class LoginHelperAddEditAccountEvent {
    object TapBackButton : LoginHelperAddEditAccountEvent()
    data class AddUserToRemoteDB(val email: String, val password: String, val tribe: String = "") :
        LoginHelperAddEditAccountEvent()

    data class AddUserToLocalDB(val email: String, val password: String, val tribe: String = "") :
        LoginHelperAddEditAccountEvent()
}
