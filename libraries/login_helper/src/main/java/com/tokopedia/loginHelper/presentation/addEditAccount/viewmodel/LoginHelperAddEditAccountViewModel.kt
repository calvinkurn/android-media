package com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginHelper.data.mapper.toEnvString
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.users.LocalUsersDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.loginHelper.domain.usecase.AddUserRestUseCase
import com.tokopedia.loginHelper.domain.usecase.EditUserRestUseCase
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountAction
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountEvent
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountUiState
import com.tokopedia.loginHelper.util.CacheConstants.LOGIN_HELPER_LOCAL_USER_DATA_PROD
import com.tokopedia.loginHelper.util.CacheConstants.LOGIN_HELPER_LOCAL_USER_DATA_STAGING
import com.tokopedia.loginHelper.util.ENCRYPTION_KEY
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginHelperAddEditAccountViewModel @Inject constructor(
    private val addUserRestUseCase: AddUserRestUseCase,
    private val editUserRestCaseUse: EditUserRestUseCase,
    private val aesEncryptorCBC: AESEncryptorCBC,
    private val gson: Gson,
    val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _uiActionChannel = Channel<LoginHelperAddEditAccountAction>()
    val uiAction = _uiActionChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(LoginHelperAddEditAccountUiState())
    val uiState = _uiState.asStateFlow()

    fun processEvent(event: LoginHelperAddEditAccountEvent) {
        when (event) {
            is LoginHelperAddEditAccountEvent.TapBackButton -> {
                handleBackButtonTap()
            }
            is LoginHelperAddEditAccountEvent.AddUserToLocalDB -> {
                handleAddUserToLocalDB(event.email, event.password)
            }
            is LoginHelperAddEditAccountEvent.AddUserToRemoteDB -> {
                handleAddUserToRemoteDB(event.email, event.password, event.tribe)
            }
            is LoginHelperAddEditAccountEvent.ChangeEnvType -> {
                changeEnvType(event.envType)
            }
            is LoginHelperAddEditAccountEvent.EditUserDetailsFromRemote -> {
                editUserFromRemote(event.email, event.password, event.tribe, event.id)
            }
        }
    }

    private fun changeEnvType(envType: LoginHelperEnvType) {
        _uiState.update {
            it.copy(
                envType = envType
            )
        }
    }

    private fun handleAddUserToLocalDB(email: String, password: String) {
        val encryptedEmail = encrypt(email)
        val encryptedPassword = encrypt(password)
        val cacheManager = PersistentCacheManager.instance
        val savedData = getLocalData(cacheManager)
        savedData?.userDataUiModel?.add(UserDataUiModel(encryptedEmail, encryptedPassword, ""))
        saveData(cacheManager, savedData)
    }

    private fun handleAddUserToRemoteDB(email: String, password: String, tribe: String) {
        viewModelScope.launchCatchError(
            dispatchers.io,
            {
                val encryptedEmail = encrypt(email)
                val encryptedPassword = encrypt(password)
                val addUserResponse = addUserRestUseCase.makeApiCall(
                    encryptedEmail,
                    encryptedPassword,
                    tribe,
                    _uiState.value.envType.toEnvString()
                )
                if (addUserResponse?.code == SUCCESS_STATUS) {
                    _uiActionChannel.send(LoginHelperAddEditAccountAction.OnSuccessAddDataToRest)
                } else {
                    _uiActionChannel.send(LoginHelperAddEditAccountAction.OnFailureAddDataToRest)
                }
            },
            onError = {
                _uiActionChannel.send(LoginHelperAddEditAccountAction.OnFailureAddDataToRest)
            }
        )
    }

    private fun editUserFromRemote(email: String, password: String, tribe: String, id: Long) {
        viewModelScope.launchCatchError(dispatchers.io, {
            val encryptedEmail = encrypt(email)
            val encryptedPassword = encrypt(password)
            val editResponse = editUserRestCaseUse.makeApiCall(
                encryptedEmail,
                encryptedPassword,
                tribe,
                _uiState.value.envType.toEnvString(),
                id
            )
            if (editResponse?.code == OK_STATUS) {
                _uiActionChannel.send(LoginHelperAddEditAccountAction.OnSuccessEditUserData)
            } else {
                _uiActionChannel.send(LoginHelperAddEditAccountAction.OnFailureEditUserData)
            }
        }, {
            _uiActionChannel.send(LoginHelperAddEditAccountAction.OnFailureEditUserData)
        })
    }

    private fun getLocalData(cacheManager: PersistentCacheManager): LocalUsersDataUiModel? {
        return if (_uiState.value.envType == LoginHelperEnvType.STAGING) {
            cacheManager.get(
                LOGIN_HELPER_LOCAL_USER_DATA_STAGING,
                LocalUsersDataUiModel::class.java,
                LocalUsersDataUiModel()
            )
        } else {
            cacheManager.get(
                LOGIN_HELPER_LOCAL_USER_DATA_PROD,
                LocalUsersDataUiModel::class.java,
                LocalUsersDataUiModel()
            )
        }
    }

    private fun saveData(cacheManager: PersistentCacheManager, savedData: LocalUsersDataUiModel?) {
        if (_uiState.value.envType == LoginHelperEnvType.STAGING) {
            cacheManager.put(
                LOGIN_HELPER_LOCAL_USER_DATA_STAGING,
                gson.toJson(savedData)
            )
        } else {
            cacheManager.put(
                LOGIN_HELPER_LOCAL_USER_DATA_PROD,
                gson.toJson(savedData)
            )
        }
        handleGoToLoginHelperHome()
    }

    private fun encrypt(text: String): String {
        val secretKey = aesEncryptorCBC.generateKey(ENCRYPTION_KEY)
        return aesEncryptorCBC.encrypt(text, secretKey)
    }

    private fun handleBackButtonTap() {
        viewModelScope.launch {
            _uiActionChannel.send(
                LoginHelperAddEditAccountAction.GoToRoute(
                    ApplinkConstInternalGlobal.LOGIN_HELPER_ACCOUNTS_SETTINGS
                )
            )
        }
    }

    private fun handleGoToLoginHelperHome() {
        viewModelScope.launch {
            _uiActionChannel.send(
                LoginHelperAddEditAccountAction.GoToRoute(
                    ApplinkConstInternalGlobal.LOGIN_HELPER
                )
            )
        }
    }

    companion object {
        const val SUCCESS_STATUS = 201L
        const val OK_STATUS = 200L
    }
}
