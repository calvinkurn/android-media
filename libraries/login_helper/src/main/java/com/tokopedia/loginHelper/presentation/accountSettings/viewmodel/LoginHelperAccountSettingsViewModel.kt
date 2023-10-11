package com.tokopedia.loginHelper.presentation.accountSettings.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state.LoginHelperAccountSettingsAction
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state.LoginHelperAccountSettingsEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class LoginHelperAccountSettingsViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _uiAction = MutableSharedFlow<LoginHelperAccountSettingsAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    fun processEvent(event: LoginHelperAccountSettingsEvent) {
        when (event) {
            is LoginHelperAccountSettingsEvent.GoToLoginHelperHome -> {
                handleBackButtonTap()
            }
            is LoginHelperAccountSettingsEvent.GoToAddAccount -> {
                handleGoToAddAccount()
            }
            is LoginHelperAccountSettingsEvent.GoToEditAccount -> {
                handleGoToEditAccount()
            }
            is LoginHelperAccountSettingsEvent.GoToDeleteAccount -> {
                handleGoToDeleteAccount()
            }
        }
    }

    private fun handleBackButtonTap() {
        _uiAction.tryEmit(
            LoginHelperAccountSettingsAction.RouteToPage(ApplinkConstInternalGlobal.LOGIN_HELPER)
        )
    }

    private fun handleGoToAddAccount() {
        _uiAction.tryEmit(
            LoginHelperAccountSettingsAction.RouteToPage(
                ApplinkConstInternalGlobal.LOGIN_HELPER_ADD_EDIT_ACCOUNT
            )
        )
    }

    private fun handleGoToEditAccount() {
        _uiAction.tryEmit(
            LoginHelperAccountSettingsAction.RouteToPage(
                ApplinkConstInternalGlobal.LOGIN_HELPER_SEARCH_ACCOUNT
            )
        )
    }

    private fun handleGoToDeleteAccount() {
        _uiAction.tryEmit(
            LoginHelperAccountSettingsAction.RouteToPage(
                ApplinkConstInternalGlobal.LOGIN_HELPER_SEARCH_ACCOUNT
            )
        )
    }
}
