package com.tokopedia.loginHelper.presentation.home.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.data.mapper.toEnvString
import com.tokopedia.loginHelper.data.mapper.toLocalUserHeaderUiModel
import com.tokopedia.loginHelper.data.mapper.toRemoteUserHeaderUiModel
import com.tokopedia.loginHelper.data.mapper.toUserDataUiModel
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.data.response.UserDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperDataSourceType
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.users.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.LocalUsersDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.loginHelper.domain.usecase.GetUserDetailsRestUseCase
import com.tokopedia.loginHelper.presentation.home.viewmodel.state.LoginHelperAction
import com.tokopedia.loginHelper.presentation.home.viewmodel.state.LoginHelperEvent
import com.tokopedia.loginHelper.presentation.home.viewmodel.state.LoginHelperUiState
import com.tokopedia.loginHelper.util.CacheConstants
import com.tokopedia.loginHelper.util.exception.ErrorGetAdminTypeException
import com.tokopedia.loginHelper.util.exception.GoToActivationPageException
import com.tokopedia.loginHelper.util.exception.GoToSecurityQuestionException
import com.tokopedia.loginHelper.util.exception.LocationAdminRedirectionException
import com.tokopedia.loginHelper.util.exception.ShowLocationAdminPopupException
import com.tokopedia.loginHelper.util.exception.ShowPopupErrorException
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.mapper.LoginV2Mapper
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenV2UseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.crypto.SecretKey
import javax.inject.Inject

class LoginHelperViewModel @Inject constructor(
    val dispatchers: CoroutineDispatchers,
    private val getUserDetailsRestUseCase: GetUserDetailsRestUseCase,
    private val loginTokenV2UseCase: LoginTokenV2UseCase,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    private val userSession: UserSessionInterface,
    private val getProfileUseCase: GetProfileUseCase,
    private val getAdminTypeUseCase: GetAdminTypeUseCase,
    private val aesEncryptorCBC: AESEncryptorCBC,
    private val secretKey: SecretKey
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
            is LoginHelperEvent.GetRemoteLoginData -> {
                // Removed now, Can be directly used when moved to REST endpoint
                getLoginData()
            }
            is LoginHelperEvent.LoginUser -> {
                loginUser(event.email, event.password, event.useHash)
            }
            is LoginHelperEvent.QueryEmail -> {
                queryForGivenEmail(event.email)
            }
            is LoginHelperEvent.GetUserInfo -> {
                getUserInfo()
            }
            is LoginHelperEvent.GoToLoginPage -> {
                goToLoginPage()
            }
            is LoginHelperEvent.LogOutUser -> {
                logOutUser()
            }
            is LoginHelperEvent.SaveUserDetailsFromAssets -> {
                storeUserDetailsInState()
            }
            is LoginHelperEvent.GoToAccountsSetting -> {
                handleGoToAccountSettings()
            }
            is LoginHelperEvent.ChangeDataSourceType -> {
                changeDataSourceType(event.dataSourceType)
            }
            is LoginHelperEvent.GetLocalLoginData -> {
                getLocalUserLoginData(event.userDetails)
            }
        }
    }

    // Can be directly used when moved to REST endpoints
    private fun getLoginData() {
        handleLoading(true)
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

    // From Persistent Cache
    private fun storeUserDetailsInState() {
        val cacheManager = PersistentCacheManager.instance
        val savedData = getLocalData(cacheManager)

        val decryptedLocalUserDetails = mutableListOf<UserDataResponse>()

        savedData?.userDataUiModel?.forEach {
            decryptedLocalUserDetails.add(
                UserDataResponse(
                    decrypt(it.email.toBlankOrString()),
                    decrypt(it.password.toBlankOrString())
                )
            )
        }

        val userList =
            LoginDataUiModel(
                decryptedLocalUserDetails.size.toLocalUserHeaderUiModel(),
                decryptedLocalUserDetails.toUserDataUiModel()
            )

        updateUserDataListLocal(Success(userList))
    }

    // From the File
    private fun getLocalUserLoginData(loginData: LoginDataResponse) {
        val decryptedUserDetails = mutableListOf<UserDataResponse>()
        loginData.users?.forEach {
            decryptedUserDetails.add(
                UserDataResponse(
                    decrypt(it.email.toBlankOrString()),
                    decrypt(it.password.toBlankOrString()),
                    it.tribe
                )
            )
        }
        val sortedUserList = decryptedUserDetails.sortedBy {
            it.email
        }

        val userList =
            LoginDataUiModel(
                sortedUserList.size.toLocalUserHeaderUiModel(),
                sortedUserList.toUserDataUiModel()
            )
        updateLocalUserDataList(Success(userList))
    }

    private fun encrypt(text: String): String {
        return aesEncryptorCBC.encrypt(text, secretKey)
    }

    private fun decrypt(text: String): String {
        return aesEncryptorCBC.decrypt(text, secretKey)
    }

    private fun getLocalData(cacheManager: PersistentCacheManager): LocalUsersDataUiModel? {
        return if (_uiState.value.envType == LoginHelperEnvType.STAGING) {
            cacheManager.get(
                CacheConstants.LOGIN_HELPER_LOCAL_USER_DATA_STAGING,
                LocalUsersDataUiModel::class.java,
                LocalUsersDataUiModel()
            )
        } else {
            cacheManager.get(
                CacheConstants.LOGIN_HELPER_LOCAL_USER_DATA_PROD,
                LocalUsersDataUiModel::class.java,
                LocalUsersDataUiModel()
            )
        }
    }

    private fun loginUser(email: String, password: String, useHash: Boolean = true) {
        launchCatchError(coroutineContext, {
            val keyData = generatePublicKeyUseCase.executeOnBackground().keyData
            if (keyData.key.isNotEmpty()) {
                var finalPassword = password
                if (useHash) {
                    finalPassword = RsaUtils.encrypt(password, keyData.key.decodeBase64(), useHash)
                }
                loginTokenV2UseCase.setParams(email, finalPassword, keyData.hash)
                val tokenResult = loginTokenV2UseCase.executeOnBackground()
                LoginV2Mapper(userSession).map(
                    tokenResult.loginToken,
                    onSuccessLoginToken = {
                        updateLoginToken(Success(it))
                    },
                    onErrorLoginToken = {
                        updateLoginToken(Fail(it))
                    },
                    onShowPopupError = {
                        updateLoginToken(Fail(ShowPopupErrorException()))
                    },
                    onGoToActivationPage = {
                        updateLoginToken(Fail(GoToActivationPageException()))
                    },
                    onGoToSecurityQuestion = {
                        updateLoginToken(Fail(GoToSecurityQuestionException()))
                    }
                )
            } else {
                updateLoginToken(Fail(MessageErrorException("Failed")))
            }
        }, {
            updateLoginToken(Fail(it))
        })
    }

    private fun getUserInfo() {
        getProfileUseCase.execute(
            GetProfileSubscriber(
                userSession,
                {
                    updateProfileResponse(Success(it))
                },
                {
                    updateProfileResponse(Fail(it))
                },
                getAdminTypeUseCase = getAdminTypeUseCase,
                showLocationAdminPopUp = {
                    updateProfileResponse(Fail(ShowLocationAdminPopupException()))
                },
                onLocationAdminRedirection = {
                    updateProfileResponse(Fail(LocationAdminRedirectionException()))
                },
                showErrorGetAdminType = {
                    updateProfileResponse(Fail(ErrorGetAdminTypeException()))
                }
            )
        )
    }

    private fun updateLoginToken(loginToken: Result<LoginToken>) {
        _uiState.update {
            it.copy(
                loginToken = loginToken
            )
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

    private fun updateProfileResponse(profilePojo: Result<ProfilePojo>) {
        _uiState.update {
            it.copy(
                profilePojo = profilePojo
            )
        }
    }

    private fun updateUserDataList(userDataList: Result<LoginDataUiModel>) {
        _uiState.update {
            it.copy(
                loginDataList = userDataList,
                isLoading = false
            )
        }
    }

    private fun updateUserDataListLocal(userDataList: Result<LoginDataUiModel>) {
        _uiState.update {
            it.copy(
                cachedLoginData = userDataList,
                isLoading = false
            )
        }
    }

    private fun updateLocalUserDataList(userDataList: Result<LoginDataUiModel>) {
        _uiState.update {
            it.copy(
                localLoginDataList = userDataList
            )
        }
    }

    private fun changeEnvType(envType: LoginHelperEnvType) {
        _uiState.update {
            it.copy(
                envType = envType
            )
        }
    }

    private fun changeDataSourceType(dataSourceType: LoginHelperDataSourceType) {
        _uiState.update {
            it.copy(
                dataSourceType = dataSourceType
            )
        }
    }

    private fun handleBackButtonTap() {
        _uiAction.tryEmit(LoginHelperAction.TapBackAction)
    }

    private fun handleGoToAccountSettings() {
        _uiAction.tryEmit(LoginHelperAction.GoToAccountSettings)
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

        if (_uiState.value.dataSourceType == LoginHelperDataSourceType.REMOTE) {
            filteredUserList = getFilteredUserList(searchEmail, _uiState.value.loginDataList)
            _uiState.update {
                it.copy(
                    filteredUserList = filteredUserList
                )
            }
        } else {
            filteredUserList = getFilteredUserList(searchEmail, _uiState.value.localLoginDataList)
            _uiState.update {
                it.copy(
                    localFilteredLoginDataList = filteredUserList
                )
            }
        }
    }

    private fun getFilteredUserList(searchEmail: String, userList: Result<LoginDataUiModel>?): Result<LoginDataUiModel>? {
        var list: List<UserDataUiModel>?
        var filteredUserList: Result<LoginDataUiModel>? = null
        userList.apply {
            when (this) {
                is Success -> {
                    list = this.data.users?.filter { userDataUiModel ->
                        userDataUiModel.email?.contains(searchEmail) == true
                    }

                    filteredUserList = Success(
                        LoginDataUiModel(HeaderUiModel(list?.size ?: 0), list)
                    )
                }
                is Fail -> Unit
            }
        }
        return filteredUserList
    }

    private fun goToLoginPage() {
        _uiAction.tryEmit(LoginHelperAction.GoToLoginPage)
    }

    private fun logOutUser() {
        userSession.logoutSession()
    }

    override fun onCleared() {
        super.onCleared()
        getProfileUseCase.unsubscribe()
        loginTokenV2UseCase.cancelJobs()
        generatePublicKeyUseCase.cancelJobs()
    }
}
