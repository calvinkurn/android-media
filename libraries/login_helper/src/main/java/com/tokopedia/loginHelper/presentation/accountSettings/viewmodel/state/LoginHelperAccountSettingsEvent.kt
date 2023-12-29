package com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state

sealed class LoginHelperAccountSettingsEvent {
    object GoToAddAccount : LoginHelperAccountSettingsEvent()
    object GoToEditAccount : LoginHelperAccountSettingsEvent()
    object GoToDeleteAccount : LoginHelperAccountSettingsEvent()
    object GoToLoginHelperHome : LoginHelperAccountSettingsEvent()
}
