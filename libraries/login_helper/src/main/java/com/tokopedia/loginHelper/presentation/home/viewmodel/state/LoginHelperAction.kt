package com.tokopedia.loginHelper.presentation.home.viewmodel.state

sealed class LoginHelperAction {
    object TapBackAction : LoginHelperAction()
    object GoToLoginPage : LoginHelperAction()
    object GoToAccountSettings : LoginHelperAction()
}
