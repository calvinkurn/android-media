package com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state

sealed class LoginHelperAccountSettingsAction {
    data class RouteToPage(val route: String) : LoginHelperAccountSettingsAction()
}
