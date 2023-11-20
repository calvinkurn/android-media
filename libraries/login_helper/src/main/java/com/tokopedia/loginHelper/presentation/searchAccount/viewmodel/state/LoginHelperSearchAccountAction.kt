package com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state

sealed class LoginHelperSearchAccountAction {
    object TapBackSearchAccountAction : LoginHelperSearchAccountAction()

    object OnSuccessDeleteAccountAction: LoginHelperSearchAccountAction()

    object OnFailureDeleteAccountAction: LoginHelperSearchAccountAction()
}
