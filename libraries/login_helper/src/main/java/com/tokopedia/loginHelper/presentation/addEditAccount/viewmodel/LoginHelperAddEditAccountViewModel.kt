package com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginHelper.domain.model.User
import com.tokopedia.loginHelper.domain.usecase.AddUserToLocalUseCase
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountAction
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountEvent
import com.tokopedia.loginHelper.util.ENCRYPTION_KEY
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class LoginHelperAddEditAccountViewModel @Inject constructor(
    private val aesEncryptorCBC: AESEncryptorCBC,
    private val addUserToLocalUseCase: AddUserToLocalUseCase,
    val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _uiAction = MutableSharedFlow<LoginHelperAddEditAccountAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    fun processEvent(event: LoginHelperAddEditAccountEvent) {
        when (event) {
            is LoginHelperAddEditAccountEvent.TapBackButton -> {
                handleBackButtonTap()
            }
            is LoginHelperAddEditAccountEvent.AddUserToLocalDB -> {
                handleAddUserToLocalDB(event.email, event.password)
            }
            is LoginHelperAddEditAccountEvent.AddUserToRemoteDB -> {
            }
        }
    }
    private fun handleAddUserToLocalDB(email: String, password: String) {
        val encryptedEmail = encrypt(email)
        val encryptedPassword = encrypt(password)
        val user = User(encryptedEmail, encryptedPassword)

        viewModelScope.launchCatchError(dispatchers.io, {
            addUserToLocalUseCase.invoke(user)
        }) {}

        handleOnSuccessAddUserToLocalDB()
    }

    private fun encrypt(text: String): String {
        val secretKey = aesEncryptorCBC.generateKey(ENCRYPTION_KEY)
        return aesEncryptorCBC.encrypt(text, secretKey)
    }

    private fun handleBackButtonTap() {
        _uiAction.tryEmit(LoginHelperAddEditAccountAction.TapBackAction)
    }

    private fun handleOnSuccessAddUserToLocalDB() {
        _uiAction.tryEmit(LoginHelperAddEditAccountAction.GoToLoginHelperHome)
    }
}
