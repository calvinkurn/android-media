package com.tokopedia.loginHelper.presentation.viewmodel.state

sealed class LoginHelperAction {
    object TapBackAction : LoginHelperAction()
    object GoToLoginPage : LoginHelperAction()
}
