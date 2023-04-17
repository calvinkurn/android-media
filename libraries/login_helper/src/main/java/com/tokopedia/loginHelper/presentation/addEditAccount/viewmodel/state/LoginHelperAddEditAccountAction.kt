package com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state

sealed class LoginHelperAddEditAccountAction {
    object TapBackAction : LoginHelperAddEditAccountAction()
    object GoToLoginHelperHome : LoginHelperAddEditAccountAction()
}
