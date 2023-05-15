package com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel

import android.util.Log
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.LocalUsersDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.UserDataUiModel
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountAction
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountEvent
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountUiState
import com.tokopedia.loginHelper.util.CacheConstants.LOGIN_HELPER_LOCAL_USER_DATA_PROD
import com.tokopedia.loginHelper.util.CacheConstants.LOGIN_HELPER_LOCAL_USER_DATA_STAGING
import com.tokopedia.loginHelper.util.ENCRYPTION_KEY
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LoginHelperAddEditAccountViewModel @Inject constructor(
    private val aesEncryptorCBC: AESEncryptorCBC,
    private val gson: Gson,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _uiAction = MutableSharedFlow<LoginHelperAddEditAccountAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

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

        Log.d("FATAL", "handleAddUserToLocalDB: $savedData")
        savedData?.userDataUiModel?.add(UserDataUiModel(encryptedEmail, encryptedPassword, ""))
        saveData(cacheManager, savedData)
    }

    private fun handleAddUserToRemoteDB(email: String, password: String, tribe: String) {
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
        _uiAction.tryEmit(LoginHelperAddEditAccountAction.TapBackAction)
    }

    private fun handleGoToLoginHelperHome() {
        _uiAction.tryEmit(LoginHelperAddEditAccountAction.GoToLoginHelperHome)
    }
}
