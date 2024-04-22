package com.tokopedia.loginregister.registerinitial.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.TkpdIdlingResourceProvider
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.pojo.DiscoverData
import com.tokopedia.loginregister.common.domain.pojo.DynamicBannerDataModel
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.common.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.domain.usecase.DiscoverUseCase
import com.tokopedia.loginregister.common.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.domain.usecase.RegisterCheckParam
import com.tokopedia.loginregister.common.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.common.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.registerinitial.domain.RegisterRequestParam
import com.tokopedia.loginregister.registerinitial.domain.RegisterRequestV2UseCase
import com.tokopedia.loginregister.registerinitial.domain.data.ProfileInfoData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestData
import com.tokopedia.loginregister.registerinitial.view.bottomsheet.OtherMethodState
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-14.
 * ade.hadian@tokopedia.com
 */

class RegisterInitialViewModel @Inject constructor(
    private val registerCheckUseCase: RegisterCheckUseCase,
    private val registerRequestV2UseCase: RegisterRequestV2UseCase,
    private val activateUserUseCase: ActivateUserUseCase,
    private val discoverUseCase: DiscoverUseCase,
    private val loginTokenUseCase: LoginTokenUseCase,
    private val getProfileUseCase: GetUserInfoAndSaveSessionUseCase,
    private val tickerInfoUseCase: TickerInfoUseCase,
    private val dynamicBannerUseCase: DynamicBannerUseCase,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    private val userSession: UserSessionInterface,
    private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    private val mutableGetProviderResponse = MutableLiveData<Result<DiscoverData>>()
    val getProviderResponse: LiveData<Result<DiscoverData>>
        get() = mutableGetProviderResponse

    private val mutableLoginTokenGoogleResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenGoogleResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenGoogleResponse

    private val mutableLoginTokenAfterSQResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenAfterSQResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenAfterSQResponse

    private val mutableValidateToken = MutableLiveData<String>()
    val validateToken: LiveData<String>
        get() = mutableValidateToken

    private val mutableShowPopup = MutableLiveData<PopupError>()
    val showPopup: LiveData<PopupError>
        get() = mutableShowPopup

    private val mutableGoToActivationPage = MutableLiveData<MessageErrorException>()
    val goToActivationPage: LiveData<MessageErrorException>
        get() = mutableGoToActivationPage

    private val mutableGoToSecurityQuestion = MutableLiveData<String>()
    val goToSecurityQuestion: LiveData<String>
        get() = mutableGoToSecurityQuestion

    private val mutableGoToActivationPageAfterRelogin = MutableLiveData<MessageErrorException>()
    val goToActivationPageAfterRelogin: LiveData<MessageErrorException>
        get() = mutableGoToActivationPageAfterRelogin

    private val mutableGoToSecurityQuestionAfterRelogin = MutableLiveData<String>()
    val goToSecurityQuestionAfterRelogin: LiveData<String>
        get() = mutableGoToSecurityQuestionAfterRelogin

    private val mutableGetUserInfoResponse = MutableLiveData<Result<ProfileInfoData>>()
    val getUserInfoResponse: LiveData<Result<ProfileInfoData>>
        get() = mutableGetUserInfoResponse

    private val mutableGetUserInfoAfterAddPinResponse = MutableLiveData<Result<ProfileInfoData>>()
    val getUserInfoAfterAddPinResponse: LiveData<Result<ProfileInfoData>>
        get() = mutableGetUserInfoAfterAddPinResponse

    private val mutableGetTickerInfoResponse = MutableLiveData<Result<List<TickerInfoPojo>>>()
    val getTickerInfoResponse: LiveData<Result<List<TickerInfoPojo>>>
        get() = mutableGetTickerInfoResponse

    private val mutableRegisterCheckResponse = MutableLiveData<Result<RegisterCheckData>>()
    val registerCheckResponse: LiveData<Result<RegisterCheckData>>
        get() = mutableRegisterCheckResponse

    private val mutableRegisterRequestResponse = MutableLiveData<Result<RegisterRequestData>>()
    val registerRequestResponse: LiveData<Result<RegisterRequestData>>
        get() = mutableRegisterRequestResponse

    private val mutableActivateUserResponse = MutableLiveData<Result<ActivateUserData>>()
    val activateUserResponse: LiveData<Result<ActivateUserData>>
        get() = mutableActivateUserResponse

    private val _dynamicBannerResponse = MutableLiveData<Result<DynamicBannerDataModel>>()
    val dynamicBannerResponse: LiveData<Result<DynamicBannerDataModel>>
        get() = _dynamicBannerResponse

    var idlingResourceProvider =
        TkpdIdlingResourceProvider.provideIdlingResource("REGISTER_INITIAL")

    private var _otherMethodState: OtherMethodState<DiscoverData?> = OtherMethodState.Loading()
    val otherMethodState get() = _otherMethodState

    fun setOtherMethodState(state: OtherMethodState<DiscoverData?>) {
        _otherMethodState = state
    }

    fun getProvider() {
        launchCatchError(block = {
            val result = discoverUseCase(PARAM_DISCOVER_REGISTER)
            mutableGetProviderResponse.value = Success(result.data)
        }, onError = {
                mutableGetProviderResponse.value = Fail(it)
            })
    }

    fun registerGoogle(accessToken: String, email: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_GOOGLE
        idlingResourceProvider?.increment()
        loginTokenUseCase.executeLoginSocialMedia(
            LoginTokenUseCase.generateParamSocialMedia(
                accessToken,
                LoginTokenUseCase.SOCIAL_TYPE_GOOGLE
            ),
            LoginTokenSubscriber(
                userSession,
                onSuccessLoginTokenGoogle(),
                onFailedLoginTokenGoogle(),
                {
                    showPopup().invoke(it.loginToken.popupError)
                    idlingResourceProvider?.decrement()
                },
                onGoToActivationPage(),
                onGoToSecurityQuestion(email)
            )
        )
    }

    fun getUserInfo() {
        idlingResourceProvider?.increment()
        launch {
            try {
                val result = getProfileUseCase(Unit)
                when (result) {
                    is Success -> onSuccessGetUserInfo(result.data.profileInfo)
                    is Fail -> onFailedGetUserInfo(result.throwable)
                }
            } catch (e: Exception) {
                onFailedGetUserInfo(e)
            }
        }
    }

    fun getUserInfoAfterAddPin() {
        launch {
            when (val response = getProfileUseCase(Unit)) {
                is Success -> {
                    mutableGetUserInfoAfterAddPinResponse.value = Success(ProfileInfoData(response.data.profileInfo))
                }
                is Fail -> {
                    mutableGetUserInfoAfterAddPinResponse.value = Fail(response.throwable)
                }
            }
        }
    }

    fun getTickerInfo() {
        launch {
            runCatching {
                val ticker = tickerInfoUseCase(TickerInfoUseCase.REGISTER_PAGE)
                mutableGetTickerInfoResponse.value = Success(ticker)
            }.onFailure {
                mutableGetTickerInfoResponse.value = Fail(it)
            }
        }
    }

    fun registerCheck(id: String) {
        launchCatchError(block = {
            idlingResourceProvider?.increment()
            val params = RegisterCheckParam(id)
            val result = registerCheckUseCase(params)
            onSuccessRegisterCheck().invoke(result)
        }, onError = {
                onFailedRegisterCheck().invoke(it)
            })
    }

    fun registerRequestV2(
        email: String,
        password: String,
        fullName: String,
        validateToken: String
    ) {
        launchCatchError(block = {
            val keyData = generatePublicKeyUseCase().keyData
            if (keyData.key.isNotEmpty()) {
                val encryptedPassword = RsaUtils.encrypt(password, keyData.key.decodeBase64(), true)
                val registerRequestParam = RegisterRequestParam(
                    email,
                    encryptedPassword,
                    OS_TYPE_ANDROID,
                    REG_TYPE_EMAIL,
                    fullName,
                    validateToken,
                    keyData.hash
                )
                userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
                val result = registerRequestV2UseCase(registerRequestParam)
                onSuccessRegisterRequest(result.data)
            }
        }, onError = {
                onFailedRegisterRequest(it)
            })
    }

    fun activateUser(
        email: String,
        validateToken: String
    ) {
        launchCatchError(coroutineContext, {
            activateUserUseCase.setParams(email, validateToken)
            val result = activateUserUseCase.executeOnBackground().data
            when {
                result.isSuccess == 1 -> {
                    onSuccessActivateUser().invoke(result)
                }

                result.message.isNotEmpty() -> {
                    onFailedActivateUser().invoke(MessageErrorException(result.message))
                }

                else -> {
                    onFailedActivateUser().invoke(Throwable())
                }
            }
        }, {
            onFailedActivateUser().invoke(it)
        })
    }

    fun reloginAfterSQ(validateToken: String) {
        loginTokenUseCase.executeLoginAfterSQ(
            LoginTokenUseCase.generateParamLoginAfterSQ(
                userSession,
                validateToken
            ),
            LoginTokenSubscriber(
                userSession,
                onSuccessLoginTokenAfterSQ(),
                onFailedLoginTokenAfterSQ(validateToken),
                { showPopup().invoke(it.loginToken.popupError) },
                onGoToActivationPageAfterRelogin(validateToken),
                onGoToSecurityQuestionAfterRelogin("")
            )
        )
    }

    fun getDynamicBannerData() {
        launchCatchError(coroutineContext, {
            val model = dynamicBannerUseCase(DynamicBannerUseCase.Page.REGISTER)
            _dynamicBannerResponse.value = Success(model)
        }, {
            _dynamicBannerResponse.postValue(Fail(it))
        })
    }

    private fun onSuccessLoginTokenGoogle(): (LoginTokenPojo) -> Unit {
        return {
            mutableLoginTokenGoogleResponse.value = Success(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onFailedLoginTokenGoogle(): (Throwable) -> Unit {
        return {
            mutableLoginTokenGoogleResponse.value = Fail(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onSuccessLoginTokenAfterSQ(): (LoginTokenPojo) -> Unit {
        return {
            mutableLoginTokenAfterSQResponse.value = Success(it)
        }
    }

    private fun onFailedLoginTokenAfterSQ(validateToken: String): (Throwable) -> Unit {
        return {
            mutableLoginTokenAfterSQResponse.value = Fail(it)
            mutableValidateToken.value = validateToken
        }
    }

    private fun onSuccessGetUserInfo(profileInfo: ProfileInfo) {
        mutableGetUserInfoResponse.value = Success(ProfileInfoData(profileInfo))
        idlingResourceProvider?.decrement()
    }

    private fun onFailedGetUserInfo(throwable: Throwable) {
        mutableGetUserInfoResponse.value = Fail(throwable)
        idlingResourceProvider?.decrement()
    }

    private fun onSuccessRegisterCheck(): (RegisterCheckPojo) -> Unit {
        return {
            if (it.data.errors.isEmpty()) {
                mutableRegisterCheckResponse.value = Success(it.data)
            } else if (it.data.errors.isNotEmpty() && it.data.errors[0].isNotEmpty()) {
                mutableRegisterCheckResponse.value =
                    Fail(MessageErrorException(it.data.errors[0]))
            } else {
                mutableRegisterCheckResponse.value = Fail(RuntimeException())
            }
            idlingResourceProvider?.decrement()
        }
    }

    private fun onFailedRegisterCheck(): (Throwable) -> Unit {
        return {
            mutableRegisterCheckResponse.value = Fail(it)
            idlingResourceProvider?.decrement()
        }
    }

    private fun onSuccessRegisterRequest(result: RegisterRequestData) {
        userSession.clearToken()
        if (result.accessToken.isNotEmpty() &&
            result.refreshToken.isNotEmpty() &&
            result.tokenType.isNotEmpty()
        ) {
            mutableRegisterRequestResponse.value = Success(result)
        } else if (result.errors.isNotEmpty() && result.errors[0].message.isNotEmpty()) {
            mutableRegisterRequestResponse.value =
                Fail(MessageErrorException(result.errors[0].message))
        } else {
            mutableRegisterRequestResponse.value = Fail(RuntimeException())
        }
    }

    private fun onFailedRegisterRequest(throwable: Throwable) {
        userSession.clearToken()
        mutableRegisterRequestResponse.value = Fail(throwable)
    }

    private fun onSuccessActivateUser(): (ActivateUserData) -> Unit {
        return {
            userSession.clearToken()
            if (it.accessToken.isNotEmpty() &&
                it.refreshToken.isNotEmpty() &&
                it.tokenType.isNotEmpty()
            ) {
                mutableActivateUserResponse.value = Success(it)
            } else if (it.message.isNotEmpty()) {
                mutableActivateUserResponse.value = Fail(MessageErrorException(it.message))
            } else {
                mutableActivateUserResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onFailedActivateUser(): (Throwable) -> Unit {
        return {
            userSession.clearToken()
            mutableActivateUserResponse.value = Fail(it)
        }
    }

    private fun showPopup(): (PopupError) -> Unit {
        return {
            mutableShowPopup.value = it
        }
    }

    private fun onGoToActivationPage(): (MessageErrorException) -> Unit {
        return {
            mutableGoToActivationPage.value = it
            idlingResourceProvider?.decrement()
        }
    }

    private fun onGoToSecurityQuestion(email: String): () -> Unit {
        return {
            mutableGoToSecurityQuestion.value = email
            idlingResourceProvider?.decrement()
        }
    }

    private fun onGoToActivationPageAfterRelogin(validateToken: String): (MessageErrorException) -> Unit {
        return {
            mutableValidateToken.value = validateToken
            mutableGoToActivationPageAfterRelogin.value = it
        }
    }

    private fun onGoToSecurityQuestionAfterRelogin(email: String): () -> Unit {
        return {
            mutableGoToSecurityQuestionAfterRelogin.value = email
        }
    }

    fun clearBackgroundTask() {
        loginTokenUseCase.unsubscribe()
    }

    override fun onCleared() {
        super.onCleared()
        clearBackgroundTask()
    }

    companion object {
        const val OS_TYPE_ANDROID = "1"
        const val REG_TYPE_EMAIL = "email"
        private const val PARAM_DISCOVER_REGISTER = "register"
    }
}
