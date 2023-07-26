package com.tokopedia.loginHelper.presentation.accountSettings.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state.LoginHelperAccountSettingsAction
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state.LoginHelperAccountSettingsEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginHelperAccountSettingsViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _uiActionChannel = Channel<LoginHelperAccountSettingsAction>()
    val uiAction = _uiActionChannel.receiveAsFlow()

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
        viewModelScope.launch {
            _uiActionChannel.send(
                LoginHelperAccountSettingsAction.RouteToPage(ApplinkConstInternalGlobal.LOGIN_HELPER)
            )
        }
    }

    private fun handleGoToAddAccount() {
        viewModelScope.launch {
            _uiActionChannel.send(
                LoginHelperAccountSettingsAction.RouteToPage(
                    ApplinkConstInternalGlobal.LOGIN_HELPER_ADD_EDIT_ACCOUNT
                )
            )
        }
    }

    private fun handleGoToEditAccount() {
        viewModelScope.launch {
            _uiActionChannel.send(
                LoginHelperAccountSettingsAction.RouteToPage(
                    ApplinkConstInternalGlobal.LOGIN_HELPER_SEARCH_ACCOUNT
                )
            )
        }
    }

    private fun handleGoToDeleteAccount() {
        viewModelScope.launch {
            _uiActionChannel.send(
                LoginHelperAccountSettingsAction.RouteToPage(
                    ApplinkConstInternalGlobal.LOGIN_HELPER_SEARCH_ACCOUNT
                )
            )
        }
    }
}
