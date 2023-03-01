package com.tokopedia.login_helper.presentation.viewmodel

import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.login_helper.data.response.LoginDataResponse
import com.tokopedia.login_helper.domain.LoginHelperEnvType
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel
import com.tokopedia.login_helper.domain.uiModel.UserDataUiModel
import com.tokopedia.login_helper.domain.usecase.GetUserDetailsRestUseCase
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperAction
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperEvent
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperUiState
import com.tokopedia.login_helper.util.exception.ErrorGetAdminTypeException
import com.tokopedia.login_helper.util.exception.GoToActivationPageException
import com.tokopedia.login_helper.util.exception.GoToSecurityQuestionException
import com.tokopedia.login_helper.util.exception.LocationAdminRedirectionException
import com.tokopedia.login_helper.util.exception.ShowLocationAdminPopupException
import com.tokopedia.login_helper.util.exception.ShowPopupErrorException
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
                loginUser(event.email, event.password)
            }
            is LoginHelperEvent.QueryEmail -> {
                queryForGivenEmail(event.email)
            }
        }
    }

    private fun getLoginData() {
        Log.d("FATAL", "callTheAPi: starting ")
        launchCatchError(
            dispatchers.io,
            block = {
                //       val response =  getUserDetailsRestUseCase.executeOnBackground()
                //      Log.d("FATAL", "callTheAPi: ${response}")
                //         updateUserDataList(convertToUserListUiModel(response))
                updateUserDataList(Success(listOfUsers()))
            },
            onError = {
                updateUserDataList(Fail(it))
                Log.d("FATAL", "callTheAPi: ${it.message}")
            }
        )
    }

    private fun listOfUsers(): LoginDataUiModel {
        return LoginDataUiModel(
            count = 5,
            users = listOf<UserDataUiModel>(
                UserDataUiModel("pbs-bagas.priyadi+01@tokopedia.com", "toped1234", "Cex"),
                UserDataUiModel("pbs-abc.yui@tokopedia.com", "asd", "iuasdjhas"),
                UserDataUiModel("sourav.saikia+01@tokopedia.com", "asd", "asedas"),
                UserDataUiModel("eren.yeager+01@tokopedia.com", "asd", "qwert"),
                UserDataUiModel("pbs-bagas.priyadi+03@tokopedia.com", "asd", "as"),
                UserDataUiModel("sourav.pbs-saikia+01@tokopedia.com", "asd", "asedas")
            )
        )
    }

    private fun convertToUserListUiModel(typeRestResponseMap: Map<Type, RestResponse>): LoginDataResponse {
        return typeRestResponseMap[LoginDataResponse::class.java]?.getData() as LoginDataResponse
    }

    private fun loginUser(email: String, password: String) {
        launchCatchError(coroutineContext, {
            val keyData = generatePublicKeyUseCase.executeOnBackground().keyData
            if (keyData.key.isNotEmpty()) {
                var finalPassword = RsaUtils.encrypt(password, keyData.key.decodeBase64(), true)
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

    fun getUserInfo() {
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

                    filteredUserList = Success(LoginDataUiModel(list?.size, list))
                }
                is Fail -> {
                    Fail(Exception("Failed to get data"))
                }
            }
        }

        _uiState.update {
            it.copy(
                filteredUserList = filteredUserList
            )
        }
    }
}
