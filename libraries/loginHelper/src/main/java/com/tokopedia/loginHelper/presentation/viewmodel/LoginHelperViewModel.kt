package com.tokopedia.loginHelper.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.LoginDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.UserDataUiModel
import com.tokopedia.loginHelper.domain.usecase.GetUserDetailsRestUseCase
import com.tokopedia.loginHelper.presentation.viewmodel.state.LoginHelperAction
import com.tokopedia.loginHelper.presentation.viewmodel.state.LoginHelperEvent
import com.tokopedia.loginHelper.presentation.viewmodel.state.LoginHelperUiState
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
import java.lang.reflect.Type
import javax.inject.Inject

// Currently data is hardcoded , Will get the data from DB once the server side is fixed
class LoginHelperViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getUserDetailsRestUseCase: GetUserDetailsRestUseCase,
    private val loginTokenV2UseCase: LoginTokenV2UseCase,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    private val userSession: UserSessionInterface,
    private val getProfileUseCase: GetProfileUseCase,
    private val getAdminTypeUseCase: GetAdminTypeUseCase
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
        }
    }

    private fun getLoginData() {
        launchCatchError(
            dispatchers.io,
            block = {
                //          val response = getUserDetailsRestUseCase.executeOnBackground()
                //         updateUserDataList(convertToUserListUiModel(response))
                val userDetails = listOfUsers(_uiState.value.envType)
                val sortedUserList = userDetails.users?.sortedBy {
                    it.email
                }
                val userList = LoginDataUiModel(userDetails.count, sortedUserList)
                updateUserDataList(Success(userList))
            },
            onError = {
                updateUserDataList(Fail(it))
            }
        )
    }

    // Currently It is hardcoded . Will get the data from server once the issue is fixed
    private fun listOfUsers(envType: LoginHelperEnvType): LoginDataUiModel {
        return when (envType) {
            LoginHelperEnvType.PRODUCTION -> {
                provideProdLoginData()
            }
            LoginHelperEnvType.STAGING -> {
                provideStagingLoginData()
            }
        }
    }

    private fun provideStagingLoginData(): LoginDataUiModel {
        return LoginDataUiModel(
            count = HeaderUiModel(6),
            users = listOf<UserDataUiModel>(
                UserDataUiModel("pbs-bagas.priyadi+01@tokopedia.com", "toped1234", "Cex"),
                UserDataUiModel("pbs-hanifah.puji+buystag1@tokopedia.com", "toped123", "Cex"),
                UserDataUiModel("dwi.widodo+06@tokopedia.com", "dodopass", "Cex"),
                UserDataUiModel("gaung.utama+01@tokopedia.com", "Q1w2e3r4", "Cex"),
                UserDataUiModel("pbs-farhan+paylater6@tokopedia.com", "tokopedia", "Fintech"),
                UserDataUiModel("yoshua.mandali+atomereject1@tokopedia.com", "tokopedia", "Fintech")
            )
        )
    }

    private fun provideProdLoginData(): LoginDataUiModel {
        return LoginDataUiModel(
            count = HeaderUiModel(5),
            users = listOf<UserDataUiModel>(
                UserDataUiModel("pbs-bagas.priyadi+01@tokopedia.com", "toped123", "Cex"),
                UserDataUiModel(
                    "android.automation.seller.h5+frontendtest@tokopedia.com",
                    "tokopedia789",
                    "Payment"
                ),
                UserDataUiModel(
                    "pbs-adam.izzulhaq+manualbuyer.gplcicil.trx.ovrdue@tokopedia.com",
                    "tokopedia",
                    "Fintech"
                ),
                UserDataUiModel(
                    "pbs-adam.izzulhaq+manualbuy.gplcicil+03@tokopedia.com",
                    "tokopedia",
                    "Fintech"
                ),
                UserDataUiModel(
                    "android.automation.seller.h5+frontendtest@tokopedia.com",
                    "tokopedia789",
                    "Fintech"
                )
            )
        )
    }

    private fun convertToUserListUiModel(typeRestResponseMap: Map<Type, RestResponse>): LoginDataResponse {
        return typeRestResponseMap[LoginDataResponse::class.java]?.getData() as LoginDataResponse
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
                loginDataList = userDataList
            )
        }
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
                        userDataUiModel.email?.contains(searchEmail) == true
                    }

                    filteredUserList = Success(LoginDataUiModel(HeaderUiModel(list?.size ?: 0), list))
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
        getUserDetailsRestUseCase.cancelJobs()
    }
}
