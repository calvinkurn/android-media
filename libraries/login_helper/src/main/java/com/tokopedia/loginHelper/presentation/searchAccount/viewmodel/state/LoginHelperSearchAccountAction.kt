package com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state

sealed class LoginHelperSearchAccountAction {
    data class GoToRoute(val route: String) : LoginHelperSearchAccountAction()

    object OnFailureDeleteAccountAction : LoginHelperSearchAccountAction()
}
