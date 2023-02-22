package com.tokopedia.login_helper.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.login_helper.domain.LoginHelperEnvType
import com.tokopedia.login_helper.domain.usecase.GetUserDetailsRestUseCase
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperAction
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperEvent
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LoginHelperViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getUserDetailsRestUseCase: GetUserDetailsRestUseCase,
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(LoginHelperUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiAction = MutableSharedFlow<LoginHelperAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    fun processEvent(event: LoginHelperEvent) {
        when (event) {
            is LoginHelperEvent.ChangeEnvType -> {
                changeEnvType(event.envType)
            }
            is LoginHelperEvent.TapBackButton -> {
                handleBackButtonTap()
            }
            is LoginHelperEvent.GetLoginData -> {
                getLoginData()
            }
        }
    }


    private fun getLoginData() {
        Log.d("FATAL", "callTheAPi: starting ")
        launchCatchError(
            dispatchers.io,
            block = {
                val response =  getUserDetailsRestUseCase.executeOnBackground()
                Log.d("FATAL", "callTheAPi: ${response}")
            },
            onError = {
                Log.d("FATAL", "callTheAPi: ${it.message}")
            }
        )
    }


    private fun changeEnvType(envType: LoginHelperEnvType) {
        _uiState.update {
            it.copy(
                envType = envType
            )
        }
        getLoginData()
    }

    private fun handleBackButtonTap() {
        _uiAction.tryEmit(LoginHelperAction.TapBackAction)
    }
}
