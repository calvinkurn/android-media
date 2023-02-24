package com.tokopedia.login_helper.presentation.viewmodel.state

sealed class LoginHelperAction {
    object TapBackAction: LoginHelperAction()
    object GoToLoginPage: LoginHelperAction()
}
