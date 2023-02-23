package com.tokopedia.login_helper.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.login_helper.domain.LoginHelperEnvType
import com.tokopedia.login_helper.domain.usecase.GetUserDetailsRestUseCase
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperAction
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperEvent
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperUiState
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.mapper.LoginV2Mapper
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenV2UseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LoginHelperViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getUserDetailsRestUseCase: GetUserDetailsRestUseCase,
    private val loginTokenV2UseCase: LoginTokenV2UseCase,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    private val userSession: UserSessionInterface,
    private val getProfileUseCase: GetProfileUseCase,
    private val getAdminTypeUseCase: GetAdminTypeUseCase,
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

    private fun loginUser(email: String, password: String) {
        launchCatchError(coroutineContext, {
            val keyData = generatePublicKeyUseCase.executeOnBackground().keyData
            if (keyData.key.isNotEmpty()) {
                var finalPassword = RsaUtils.encrypt(password, keyData.key.decodeBase64(), true)
                loginTokenV2UseCase.setParams(email, finalPassword, keyData.hash)
                val tokenResult = loginTokenV2UseCase.executeOnBackground()
                LoginV2Mapper(userSession).map(tokenResult.loginToken,
                    onSuccessLoginToken = {
                        updateLoginToken(Success(it))
                    },
                    onErrorLoginToken = {
                        updateLoginToken(Fail(it))
                    },
                    onShowPopupError = {
                        Log.d("FATAL", "on show popup error: a")
                    },
                    onGoToActivationPage = {
                        Log.d("FATAL", "go to activation page : a")
                    },
                    onGoToSecurityQuestion = {
                        Log.d("FATAL", "loginUsergoto security question: ")
                    }
                )
            } else {
                updateLoginToken(Fail(MessageErrorException("Failed")))
            }
        }, {
            updateLoginToken(Fail(it))
        })
    }

//    fun getUserInfo() {
//        getProfileUseCase.execute(GetProfileSubscriber(userSession,
//            { mutableProfileResponse.value = Success(it) },
//            { mutableProfileResponse.value = Fail(it) },
//            getAdminTypeUseCase = getAdminTypeUseCase,
//            showLocationAdminPopUp = {
//                mutableShowLocationAdminPopUp.value = Success(true)
//            },
//            onLocationAdminRedirection = {
//                mutableAdminRedirection.value = Success(true)
//            },
//            showErrorGetAdminType = {
//                mutableShowLocationAdminPopUp.value = Fail(it)
//            }
//        ))
//    }

    private fun updateLoginToken(loginToken: com.tokopedia.usecase.coroutines.Result<LoginToken>) {
        _uiState.update {
            it.copy(
                loginToken = loginToken
            )
        }
    }

    private fun updateProfileResponse(profilePojo: com.tokopedia.usecase.coroutines.Result<ProfilePojo>) {

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
