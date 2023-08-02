package com.tokopedia.loginregister.login.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gojek.icp.identity.loginsso.data.models.Profile
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.pojo.DiscoverData
import com.tokopedia.loginregister.common.domain.pojo.DynamicBannerDataModel
import com.tokopedia.loginregister.common.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.domain.usecase.DiscoverUseCase
import com.tokopedia.loginregister.common.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessHelper
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessPreference
import com.tokopedia.loginregister.goto_seamless.model.GetTemporaryKeyParam
import com.tokopedia.loginregister.goto_seamless.usecase.GetTemporaryKeyUseCase
import com.tokopedia.loginregister.goto_seamless.usecase.GetTemporaryKeyUseCase.Companion.MODULE_GOTO_SEAMLESS
import com.tokopedia.loginregister.login.domain.RegisterCheckFingerprintUseCase
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.login.domain.model.LoginOption
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.mapper.LoginV2Mapper
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginFingerprintUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenV2UseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
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

    private val mutableLoginBiometricResponse = MutableLiveData<Result<LoginToken>>()
    val loginBiometricResponse: LiveData<Result<LoginToken>>
        get() = mutableLoginBiometricResponse

    private val mutableGetTemporaryKeyResponse = MutableLiveData<Boolean>()
    val getTemporaryKeyResponse: LiveData<Boolean>
        get() = mutableGetTemporaryKeyResponse

    private val mutableLoginOption = MutableLiveData<LoginOption>()
    val getLoginOption: LiveData<LoginOption>
        get() = mutableLoginOption

    fun registerCheck(id: String) {
        launchCatchError(coroutineContext, {
            registerCheckUseCase.setRequestParams(registerCheckUseCase.getRequestParams(id))
            val response = registerCheckUseCase.executeOnBackground()
            if (response.data.errors.isEmpty()) {
                mutableRegisterCheckResponse.value = Success(response.data)
            } else {
                mutableRegisterCheckResponse.value = Fail(MessageErrorException(response.data.errors.first()))
            }
        }, {
            mutableRegisterCheckResponse.value = Fail(it)
        })
    }

    fun discoverLogin() {
        launchCatchError(block = {
            val result = discoverUseCase(PARAM_DISCOVER_LOGIN)
            mutableDiscoverResponse.value = Success(result.data)
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
        getProfileUseCase.execute(
            GetProfileSubscriber(
                userSession,
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
            )
        )
    }

    fun loginGoogle(accessToken: String, email: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_GOOGLE
        loginTokenUseCase.executeLoginSocialMedia(
            LoginTokenUseCase.generateParamSocialMedia(
                accessToken,
                LoginTokenUseCase.SOCIAL_TYPE_GOOGLE
            ),
            LoginTokenSubscriber(
                userSession,
                {
                    mutableLoginTokenGoogleResponse.value = Success(it)
                },
                {
                    mutableLoginTokenGoogleResponse.value = Fail(it)
                },
                { showPopup(it.loginToken.popupError) },
                { onGoToActivationPage(email) },
                { onGoToSecurityQuestion(email) }
            )
        )
    }

    fun loginEmail(email: String, password: String) {
        loginTokenUseCase.executeLoginEmailWithPassword(
            LoginTokenUseCase.generateParamLoginEmail(
                email,
                password
            ),
            LoginTokenSubscriber(
                userSession,
                {
                    mutableLoginTokenResponse.value = Success(it)
                },
                {
                    mutableLoginTokenResponse.value = Fail(it)
                },
                { showPopup(it.loginToken.popupError) },
                { onGoToActivationPage(email) },
                { onGoToSecurityQuestion(email) }
            )
        )
    }

    fun loginEmailV2(email: String, password: String, useHash: Boolean) {
        launchCatchError(coroutineContext, {
            val keyData = generatePublicKeyUseCase().keyData
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
        loginFingerprintUseCase.loginBiometric(
            email,
            validateToken,
            {
                mutableLoginBiometricResponse.value = Success(it)
            },
            onFailedLoginBiometric(),
            { showPopup(it.popupError) },
            { onGoToActivationPage(email) },
            { onGoToSecurityQuestion(email) }
        )
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
                userSession,
                validateToken
            ),
            LoginTokenSubscriber(
                userSession,
                { mutableLoginTokenAfterSQResponse.value = Success(it) },
                { mutableLoginTokenAfterSQResponse.value = Fail(it) },
                { showPopup(it.loginToken.popupError) },
                { onGoToActivationPageAfterRelogin(it) },
                { onGoToSecurityQuestionAfterRelogin("") }
            )
        )
    }

    fun getTickerInfo() {
        launchCatchError(coroutineContext, {
            val ticker = tickerInfoUseCase(TickerInfoUseCase.LOGIN_PAGE)
            mutableGetTickerInfoResponse.value = Success(ticker)
        }, {
            mutableGetTickerInfoResponse.value = Fail(it)
        })
    }

    fun getDynamicBannerData() {
        launchCatchError(coroutineContext, {
            val model = dynamicBannerUseCase(DynamicBannerUseCase.Page.LOGIN)
            mutableDynamicBannerResponse.value = Success(model)
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
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
    }

    suspend fun isGojekProfileExist(): Boolean {
        return try {
            gotoSeamlessHelper.getGojekProfile().authCode.isNotEmpty()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            false
        }
    }

    suspend fun isFingerprintRegistered(): Boolean {
        if (GlobalConfig.isSellerApp()) return false
        return try {
            registerCheckFingerprintUseCase(Unit)
        } catch (ignored: Exception) {
            false
        }
    }

    fun checkLoginOption(isEnableSeamless: Boolean, isEnableFingerprint: Boolean, isEnableDirectBiometric: Boolean, isEnableOcl: Boolean) {
        launch {
            var enableSeamless = isEnableSeamless
            // Access the SDK if seamless login config is enabled.
            if (isEnableSeamless) {
                enableSeamless = isGojekProfileExist()
            }
            var isBiometricRegistered = false
            // If one of the biometrics config is enabled then we hit the api to reduce network usage.
            if (isEnableFingerprint || isEnableDirectBiometric) {
                isBiometricRegistered = isFingerprintRegistered()
            }

            mutableLoginOption.value = LoginOption(
                isEnableSeamless = enableSeamless,
                isEnableBiometrics = (isBiometricRegistered && isEnableFingerprint),
                isEnableOcl = isEnableOcl,
                isEnableDirectBiometric = (isBiometricRegistered && isEnableDirectBiometric)
            )
        }
    }
    public override fun onCleared() {
        super.onCleared()
        clearBackgroundTask()
    }

    companion object {
        private const val PARAM_DISCOVER_LOGIN = "login"
    }
}
