package com.tokopedia.loginHelper.presentation.searchAccount.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginHelper.data.mapper.toEnvString
import com.tokopedia.loginHelper.data.mapper.toRemoteUserHeaderUiModel
import com.tokopedia.loginHelper.data.mapper.toUserDataUiModel
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.users.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.loginHelper.domain.usecase.GetUserDetailsRestUseCase
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state.LoginHelperSearchAccountAction
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state.LoginHelperSearchAccountEvent
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state.LoginHelperSearchAccountUiState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.crypto.SecretKey
import javax.inject.Inject

class LoginHelperSearchAccountViewModel @Inject constructor(
    private val getUserDetailsRestUseCase: GetUserDetailsRestUseCase,
    private val aesEncryptorCBC: AESEncryptorCBC,
    private val secretKey: SecretKey,
    val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(LoginHelperSearchAccountUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiAction = MutableSharedFlow<LoginHelperSearchAccountAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    fun processEvent(event: LoginHelperSearchAccountEvent) {
        when (event) {
            is LoginHelperSearchAccountEvent.SetEnvType -> {
                setEnvType(event.envType)
            }
            is LoginHelperSearchAccountEvent.SaveUserDetailsFromAssets -> {
                getLoginData()
            }
            is LoginHelperSearchAccountEvent.QueryEmail -> {
                queryForGivenEmail(event.email)
            }
            is LoginHelperSearchAccountEvent.TapBackButton -> {
                handleBackButtonTap()
            }
        }
    }

    private fun setEnvType(envType: LoginHelperEnvType) {
        _uiState.update {
            it.copy(
                envType = envType
            )
        }
    }

    private fun getLoginData() {
        launchCatchError(
            dispatchers.io,
            block = {
                val userDetails = getUserDetailsRestUseCase.makeNetworkCall(_uiState.value.envType.toEnvString())
                val loginData = userDetails.body()

                val userList = LoginDataUiModel(
                    loginData?.count?.toRemoteUserHeaderUiModel(),
                    loginData?.users?.toUserDataUiModel()
                )

                updateUserDataList(Success(userList))
            },
            onError = {
                updateUserDataList(Fail(it))
            }
        )
    }
    private fun updateUserDataList(userDataList: Result<LoginDataUiModel>) {
        _uiState.update {
            it.copy(
                loginDataList = userDataList
            )
        }
    }

    private fun queryForGivenEmail(email: String) {
        _uiState.update {
            it.copy(
                searchText = email
            )
        }
        searchForFilteredUser()
    }

    private fun searchForFilteredUser() {
        val searchEmail = _uiState.value.searchText
        var list: List<UserDataUiModel>?
        var filteredUserList: Result<LoginDataUiModel>? = null
        _uiState.value.loginDataList.apply {
            when (this) {
                is Success -> {
                    list = this.data.users?.filter { userDataUiModel ->
                        (
                            userDataUiModel.email?.lowercase()?.contains(searchEmail.lowercase()) == true ||
                                userDataUiModel.tribe?.lowercase()?.contains(
                                searchEmail.lowercase()
                            ) == true
                            )
                    }

                    filteredUserList = Success(
                        LoginDataUiModel(HeaderUiModel(list?.size ?: 0), list)
                    )
                }
                is Fail -> Unit
            }
        }

        _uiState.update {
            it.copy(
                filteredUserList = filteredUserList
            )
        }
    }

    private fun decrypt(text: String): String {
        return aesEncryptorCBC.decrypt(text, secretKey)
    }

    private fun handleBackButtonTap() {
        _uiAction.tryEmit(LoginHelperSearchAccountAction.TapBackSearchAccountAction)
    }
}
