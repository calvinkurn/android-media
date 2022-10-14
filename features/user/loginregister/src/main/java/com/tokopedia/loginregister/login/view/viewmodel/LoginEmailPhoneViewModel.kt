package com.tokopedia.loginregister.login.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gojek.icp.identity.loginsso.data.models.Profile
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.discover.pojo.DiscoverData
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessHelper
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessPreference
import com.tokopedia.loginregister.goto_seamless.model.GetTemporaryKeyParam
import com.tokopedia.loginregister.goto_seamless.usecase.GetTemporaryKeyUseCase
import com.tokopedia.loginregister.goto_seamless.usecase.GetTemporaryKeyUseCase.Companion.MODULE_GOTO_SEAMLESS
import com.tokopedia.loginregister.login.domain.RegisterCheckFingerprintUseCase
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckFingerprint
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.mapper.LoginV2Mapper
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginEmailPhoneViewModel @Inject constructor(
    private val registerCheckUseCase: RegisterCheckUseCase,
    private val discoverUseCase: DiscoverUseCase,
    private val activateUserUseCase: ActivateUserUseCase,
    private val loginTokenUseCase: LoginTokenUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val tickerInfoUseCase: TickerInfoUseCase,
    private val getAdminTypeUseCase: GetAdminTypeUseCase,
    private val loginTokenV2UseCase: LoginTokenV2UseCase,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    private val dynamicBannerUseCase: DynamicBannerUseCase,
    private val registerCheckFingerprintUseCase: RegisterCheckFingerprintUseCase,
    private val loginFingerprintUseCase: LoginFingerprintUseCase,
    private val getTemporaryKeyUseCase: GetTemporaryKeyUseCase,
    private val gotoSeamlessHelper: GotoSeamlessHelper,
    private val gotoSeamlessPreference: GotoSeamlessPreference,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val mutableNavigateToGojekSeamless = SingleLiveEvent<Boolean>()
    val navigateToGojekSeamless: LiveData<Boolean>
        get() = mutableNavigateToGojekSeamless

    private val mutableRegisterCheckResponse = MutableLiveData<Result<RegisterCheckData>>()
    val registerCheckResponse: LiveData<Result<RegisterCheckData>>
        get() = mutableRegisterCheckResponse

    private val mutableDiscoverResponse = MutableLiveData<Result<DiscoverData>>()
    val discoverResponse: LiveData<Result<DiscoverData>>
        get() = mutableDiscoverResponse

    private val mutableActivateResponse = MutableLiveData<Result<ActivateUserData>>()
    val activateResponse: LiveData<Result<ActivateUserData>>
        get() = mutableActivateResponse

    private val mutableLoginTokenResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenResponse

    private val mutableLoginTokenV2Response = MutableLiveData<Result<LoginToken>>()
    val loginTokenV2Response: LiveData<Result<LoginToken>>
        get() = mutableLoginTokenV2Response

    private val mutableProfileResponse = MutableLiveData<Result<ProfilePojo>>()
    val profileResponse: LiveData<Result<ProfilePojo>>
        get() = mutableProfileResponse

    private val mutableShowPopup = MutableLiveData<PopupError>()
    val showPopup: LiveData<PopupError>
        get() = mutableShowPopup

    private val mutableGoToActivationPage = MutableLiveData<String>()
    val goToActivationPage: LiveData<String>
        get() = mutableGoToActivationPage

    private val mutableShowLocationAdminPopUp = MutableLiveData<Result<Boolean>>()
    val showLocationAdminPopUp: LiveData<Result<Boolean>>
        get() = mutableShowLocationAdminPopUp

    private val mutableAdminRedirection = MutableLiveData<Result<Boolean>>()
    val adminRedirection: LiveData<Result<Boolean>>
        get() = mutableAdminRedirection

    private val mutableGoToSecurityQuestion = MutableLiveData<String>()
    val goToSecurityQuestion: LiveData<String>
        get() = mutableGoToSecurityQuestion

    private val mutableLoginTokenGoogleResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenGoogleResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenGoogleResponse

    private val mutableGoToActivationPageAfterRelogin = MutableLiveData<MessageErrorException>()
    val goToActivationPageAfterRelogin: LiveData<MessageErrorException>
        get() = mutableGoToActivationPageAfterRelogin

    private val mutableGoToSecurityQuestionAfterRelogin = MutableLiveData<String>()
    val goToSecurityQuestionAfterRelogin: LiveData<String>
        get() = mutableGoToSecurityQuestionAfterRelogin

    private val mutableLoginTokenAfterSQResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenAfterSQResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenAfterSQResponse

    private val mutableGetTickerInfoResponse = MutableLiveData<Result<List<TickerInfoPojo>>>()
    val getTickerInfoResponse: LiveData<Result<List<TickerInfoPojo>>>
        get() = mutableGetTickerInfoResponse

    private val mutableDynamicBannerResponse = MutableLiveData<Result<DynamicBannerDataModel>>()
    val dynamicBannerResponse: LiveData<Result<DynamicBannerDataModel>>
        get() = mutableDynamicBannerResponse

    private val mutableRegisterCheckFingerprint =
        MutableLiveData<Result<RegisterCheckFingerprint>>()
    val registerCheckFingerprint: LiveData<Result<RegisterCheckFingerprint>>
        get() = mutableRegisterCheckFingerprint

    private val mutableLoginBiometricResponse = MutableLiveData<Result<LoginToken>>()
    val loginBiometricResponse: LiveData<Result<LoginToken>>
        get() = mutableLoginBiometricResponse

    private val mutableGetTemporaryKeyResponse = MutableLiveData<Boolean>()
    val getTemporaryKeyResponse: LiveData<Boolean>
        get() = mutableGetTemporaryKeyResponse

    fun registerCheck(id: String) {
        launchCatchError(coroutineContext, {
            registerCheckUseCase.setRequestParams(registerCheckUseCase.getRequestParams(id))
            val response = registerCheckUseCase.executeOnBackground()
            if (response.data.errors.isEmpty())
                mutableRegisterCheckResponse.value = Success(response.data)
            else if (response.data.errors.isNotEmpty() && response.data.errors[0].isNotEmpty()) {
                mutableRegisterCheckResponse.value =
                    Fail(MessageErrorException(response.data.errors[0]))
            } else mutableRegisterCheckResponse.value = Fail(RuntimeException())
        }, {
            mutableRegisterCheckResponse.value = Fail(it)
        })
    }

    fun registerCheckFingerprint() {
        registerCheckFingerprintUseCase.checkRegisteredFingerprint(onSuccess = {
            if (it.data.errorMessage.isEmpty()) {
                mutableRegisterCheckFingerprint.postValue(Success(it))
            } else {
                mutableRegisterCheckFingerprint.postValue(Fail(MessageErrorException(it.data.errorMessage)))
            }
        }, onError = {
            mutableRegisterCheckFingerprint.postValue(Fail(it))
        })
    }

    fun discoverLogin() {
        launchCatchError(block = {
            val result = discoverUseCase(PARAM_DISCOVER_LOGIN)

            withContext(dispatchers.main) {
                mutableDiscoverResponse.value = Success(result.data)
            }
        }, onError = {
            mutableDiscoverResponse.value = Fail(it)
        })
    }

    fun activateUser(email: String, validateToken: String) {
        launchCatchError(coroutineContext, {
            activateUserUseCase.setParams(email, validateToken)
            val result = activateUserUseCase.executeOnBackground()
            mutableActivateResponse.value = Success(result.data)
        }, {
            mutableActivateResponse.value = Fail(it)
        })
    }

    fun getUserInfo() {
        getProfileUseCase.execute(GetProfileSubscriber(userSession,
            { mutableProfileResponse.value = Success(it) },
            { mutableProfileResponse.value = Fail(it) },
            getAdminTypeUseCase = getAdminTypeUseCase,
            showLocationAdminPopUp = {
                mutableShowLocationAdminPopUp.value = Success(true)
            },
            onLocationAdminRedirection = {
                mutableAdminRedirection.value = Success(true)
            },
            showErrorGetAdminType = {
                mutableShowLocationAdminPopUp.value = Fail(it)
            }
        ))
    }

    fun loginGoogle(accessToken: String, email: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_GOOGLE
        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
            accessToken, LoginTokenUseCase.SOCIAL_TYPE_GOOGLE
        ),
            LoginTokenSubscriber(userSession,
                {
                    mutableLoginTokenGoogleResponse.value = Success(it)
                }, {
                    mutableLoginTokenGoogleResponse.value = Fail(it)
                },
                { showPopup(it.loginToken.popupError) },
                { onGoToActivationPage(email) },
                { onGoToSecurityQuestion(email) }
            ))
    }

    fun loginEmail(email: String, password: String) {
        loginTokenUseCase.executeLoginEmailWithPassword(LoginTokenUseCase.generateParamLoginEmail(
            email, password
        ), LoginTokenSubscriber(userSession,
            {
                mutableLoginTokenResponse.value = Success(it)
            },
            {
                mutableLoginTokenResponse.value = Fail(it)
            },
            { showPopup(it.loginToken.popupError) },
            { onGoToActivationPage(email) },
            { onGoToSecurityQuestion(email) }
        ))
    }

    fun loginEmailV2(email: String, password: String, useHash: Boolean) {
        launchCatchError(coroutineContext, {
            val keyData = generatePublicKeyUseCase.executeOnBackground().keyData
            if (keyData.key.isNotEmpty()) {
                var finalPassword = password
                if (useHash) {
                    finalPassword = RsaUtils.encrypt(password, keyData.key.decodeBase64(), useHash)
                }
                loginTokenV2UseCase.setParams(email, finalPassword, keyData.hash)
                val tokenResult = loginTokenV2UseCase.executeOnBackground()
                LoginV2Mapper(userSession).map(tokenResult.loginToken,
                    onSuccessLoginToken = {
                        mutableLoginTokenV2Response.value = Success(it)
                    },
                    onErrorLoginToken = {
                        mutableLoginTokenV2Response.value = Fail(it)
                    },
                    onShowPopupError = { showPopup(it.popupError) },
                    onGoToActivationPage = { onGoToActivationPage(email) },
                    onGoToSecurityQuestion = { onGoToSecurityQuestion(email) }
                )
            } else {
                mutableLoginTokenV2Response.value = Fail(MessageErrorException("Failed"))
            }
        }, {
            mutableLoginTokenV2Response.value = Fail(it)
        })
    }

    fun getTemporaryKeyForSDK(tkpdProfile: ProfilePojo) {
        launchCatchError(block = {
            val params = GetTemporaryKeyParam(
                module = MODULE_GOTO_SEAMLESS,
                currentToken = gotoSeamlessPreference.getTemporaryToken()
            )
            val result = getTemporaryKeyUseCase(params)
            gotoSeamlessPreference.storeTemporaryToken(result.data.key)
            val profile = Profile(
                accessToken = result.data.key,
                name = tkpdProfile.profileInfo.fullName,
                customerId = tkpdProfile.profileInfo.userId,
                countryCode = "",
                phone = tkpdProfile.profileInfo.phone,
                email = tkpdProfile.profileInfo.email,
                profileImageUrl = tkpdProfile.profileInfo.profilePicture
            )
            gotoSeamlessHelper.saveUserProfileToSDK(profile)
            mutableGetTemporaryKeyResponse.value = true
        }, onError = {
            mutableGetTemporaryKeyResponse.value = false
        })
    }

    fun loginTokenBiometric(email: String, validateToken: String) {
        loginFingerprintUseCase.loginBiometric(email, validateToken,
            onSuccessLoginBiometric(),
            onFailedLoginBiometric(),
            { showPopup(it.popupError) },
            { onGoToActivationPage(email) },
            { onGoToSecurityQuestion(email) }
        )
    }

    private fun onSuccessLoginBiometric(): (LoginToken) -> Unit {
        return {
            if (it.accessToken.isNotEmpty() &&
                it.refreshToken.isNotEmpty() &&
                it.tokenType.isNotEmpty()
            ) {
                mutableLoginBiometricResponse.value = Success(it)
            } else if (it.errors.isNotEmpty() &&
                it.errors[0].message.isNotEmpty()
            ) {
                mutableLoginBiometricResponse.value =
                    Fail(MessageErrorException(it.errors[0].message))
            } else {
                mutableLoginBiometricResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onFailedLoginBiometric(): (Throwable) -> Unit {
        return {
            userSession.clearToken()
            mutableLoginBiometricResponse.value = Fail(it)
        }
    }

    fun reloginAfterSQ(validateToken: String) {
        loginTokenUseCase.executeLoginAfterSQ(
            LoginTokenUseCase.generateParamLoginAfterSQ(
                userSession, validateToken
            ), LoginTokenSubscriber(userSession,
                { mutableLoginTokenAfterSQResponse.value = Success(it) },
                { mutableLoginTokenAfterSQResponse.value = Fail(it) },
                { showPopup(it.loginToken.popupError) },
                { onGoToActivationPageAfterRelogin(it) },
                { onGoToSecurityQuestionAfterRelogin("") })
        )
    }

    fun getTickerInfo() {
        launchCatchError(coroutineContext, {
            val params = TickerInfoUseCase.createRequestParam(TickerInfoUseCase.LOGIN_PAGE)
            val ticker = withContext(dispatchers.io) {
                tickerInfoUseCase.createObservable(params).toBlocking().single()
            }
            mutableGetTickerInfoResponse.value = Success(ticker)
        }, {
            mutableGetTickerInfoResponse.value = Fail(it)
        })
    }

    fun getDynamicBannerData(page: String) {
        launchCatchError(coroutineContext, {
            val params = DynamicBannerUseCase.createRequestParams(page)
            dynamicBannerUseCase.createParams(params)
            dynamicBannerUseCase.executeOnBackground().run {
                mutableDynamicBannerResponse.postValue(Success(this))
            }
        }, {
            mutableDynamicBannerResponse.postValue(Fail(it))
        })
    }

    private fun showPopup(popupError: PopupError) {
        mutableShowPopup.value = popupError
    }

    private fun onGoToActivationPage(email: String) {
        mutableGoToActivationPage.value = email
    }

    private fun onGoToSecurityQuestion(email: String) {
        mutableGoToSecurityQuestion.value = email
    }

    private fun onGoToActivationPageAfterRelogin(messageErrorException: MessageErrorException) {
        mutableGoToActivationPageAfterRelogin.value = messageErrorException
    }

    private fun onGoToSecurityQuestionAfterRelogin(email: String) {
        mutableGoToSecurityQuestionAfterRelogin.value = email
    }

    fun clearBackgroundTask() {
        tickerInfoUseCase.unsubscribe()
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
    }

    fun checkSeamlessEligiblity() {
        launch {
            try {
                val result = gotoSeamlessHelper.getGojekProfile()
                mutableNavigateToGojekSeamless.value = result.authCode.isNotEmpty()
            } catch (e: Exception) {
                mutableNavigateToGojekSeamless.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearBackgroundTask()
    }

    companion object {
        private const val PARAM_DISCOVER_LOGIN = "login"
    }
}
