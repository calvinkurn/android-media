package com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountAction
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class LoginHelperAddEditAccountViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _uiAction = MutableSharedFlow<LoginHelperAddEditAccountAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    fun processEvent(event: LoginHelperAddEditAccountEvent) {
        when (event) {
            is LoginHelperAddEditAccountEvent.TapBackButton -> {
                handleBackButtonTap()
            }
            is LoginHelperAddEditAccountEvent.AddUserToLocalDB -> {
            }
            is LoginHelperAddEditAccountEvent.AddUserToRemoteDB -> {
            }
        }
    }

    private fun handleBackButtonTap() {
        _uiAction.tryEmit(LoginHelperAddEditAccountAction.TapBackAction)
    }
}
