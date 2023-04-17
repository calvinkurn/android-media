package com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state

sealed class LoginHelperAccountSettingsAction {
    object GoToAddAccount : LoginHelperAccountSettingsAction()
    object GoToEditAccount : LoginHelperAccountSettingsAction()
    object GoToDeleteAccount : LoginHelperAccountSettingsAction()
    object GoToLoginHelperHome : LoginHelperAccountSettingsAction()
}
