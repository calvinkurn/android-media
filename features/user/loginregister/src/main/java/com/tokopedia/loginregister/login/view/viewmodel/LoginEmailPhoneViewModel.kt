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
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.common.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.domain.usecase.DiscoverUseCase
import com.tokopedia.loginregister.common.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.domain.usecase.RegisterCheckParam
import com.tokopedia.loginregister.common.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.common.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessHelper
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessPreference
import com.tokopedia.loginregister.goto_seamless.model.GetTemporaryKeyParam
import com.tokopedia.loginregister.goto_seamless.usecase.GetTemporaryKeyUseCase
import com.tokopedia.loginregister.goto_seamless.usecase.GetTemporaryKeyUseCase.Companion.MODULE_GOTO_SEAMLESS
import com.tokopedia.loginregister.login.domain.RegisterCheckFingerprintUseCase
import com.tokopedia.loginregister.login.domain.model.LoginOption
import com.tokopedia.loginregister.login_sdk.data.AuthorizeData
import com.tokopedia.loginregister.login_sdk.data.SdkAuthorizeParam
import com.tokopedia.loginregister.login_sdk.data.SdkConsentData
import com.tokopedia.loginregister.login_sdk.data.SdkConsentParam
import com.tokopedia.loginregister.login_sdk.data.ValidateClientData
import com.tokopedia.loginregister.login_sdk.data.ValidateClientParam
import com.tokopedia.loginregister.login_sdk.usecase.AuthorizeSdkUseCase
import com.tokopedia.loginregister.login_sdk.usecase.LoginSdkConsentUseCase
import com.tokopedia.loginregister.login_sdk.usecase.ValidateClientUseCase
import com.tokopedia.loginregister.shopcreation.data.ShopStatus
import com.tokopedia.loginregister.shopcreation.domain.GetShopStatusUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.admin.AdminResult
import com.tokopedia.sessioncommon.data.fingerprintpreference.FingerprintPreference
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.mapper.LoginV2Mapper
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.FingerPrintGqlParam
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndAdminUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginFingerprintUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenV2GqlParam
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenV2UseCase
import com.tokopedia.sessioncommon.util.TokenGenerator
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
    private val tickerInfoUseCase: TickerInfoUseCase,
    private val getProfileAndAdmin: GetUserInfoAndAdminUseCase,
    private val userProfileAndSaveSessionUseCase: GetUserInfoAndSaveSessionUseCase,
    private val loginTokenV2UseCase: LoginTokenV2UseCase,
    private val loginSdkConsentUseCase: LoginSdkConsentUseCase,
    private val validateClientUseCase: ValidateClientUseCase,
    private val authorizeSdkUseCase: AuthorizeSdkUseCase,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    private val dynamicBannerUseCase: DynamicBannerUseCase,
    private val registerCheckFingerprintUseCase: RegisterCheckFingerprintUseCase,
    private val loginFingerprintUseCase: LoginFingerprintUseCase,
    private val getTemporaryKeyUseCase: GetTemporaryKeyUseCase,
    private val getShopStatusUseCase: GetShopStatusUseCase,
    private val gotoSeamlessHelper: GotoSeamlessHelper,
    private val gotoSeamlessPreference: GotoSeamlessPreference,
    private val userSession: UserSessionInterface,
    private val fingerprintPreferenceManager: FingerprintPreference,
    dispatchers: CoroutineDispatchers
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

    private val mutableConsent = MutableLiveData<Result<SdkConsentData>>()
    val sdkConsent: LiveData<Result<SdkConsentData>>
        get() = mutableConsent

    private val mutableValidateClient = MutableLiveData<Result<ValidateClientData>>()
    val validateClient: LiveData<Result<ValidateClientData>>
        get() = mutableValidateClient

    private val mutableAuthorizeResponse = MutableLiveData<Result<AuthorizeData>>()
    val authorizeResponse: LiveData<Result<AuthorizeData>>
        get() = mutableAuthorizeResponse

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

    private val _shopStatus = MutableLiveData<ShopStatus>()
    val shopStatus: LiveData<ShopStatus>
        get() = _shopStatus

    private var isLoginSdkFlow: Boolean = false

    fun setAsLoginSdkFlow() {
        isLoginSdkFlow = true
    }

    fun registerCheck(id: String) {
        launchCatchError(block = {
            val params = RegisterCheckParam(id)
            val result = registerCheckUseCase(params)
            if (result.data.errors.isEmpty()) {
                mutableRegisterCheckResponse.value = Success(result.data)
            } else {
                mutableRegisterCheckResponse.value = Fail(MessageErrorException(result.data.errors.first()))
            }
        }, onError = {
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

    fun getUserInfoOnly() {
        launch {
            try {
                val userInfo = userProfileAndSaveSessionUseCase(Unit)
                mutableProfileResponse.value = userInfo
            } catch (e: Exception) {
                mutableProfileResponse.value = Fail(e)
            }
        }
    }

    fun getUserInfo() {
        if (isLoginSdkFlow) {
            getUserInfoOnly()
        } else {
            launch {
                try {
                    if (GlobalConfig.isSellerApp()) {
                        getShopStatus()
                    }
                    when (val admin = getProfileAndAdmin(Unit)) {
                        is AdminResult.AdminResultOnSuccessGetProfile -> {
                            mutableProfileResponse.value = Success(admin.profile)
                        }

                        is AdminResult.AdminResultOnLocationAdminRedirection -> {
                            mutableAdminRedirection.value = Success(true)
                        }

                        is AdminResult.AdminResultShowLocationPopup -> {
                            mutableShowLocationAdminPopUp.value = Success(true)
                        }

                        is AdminResult.AdminResultOnErrorGetProfile -> {
                            mutableProfileResponse.value = Fail(admin.error)
                        }

                        is AdminResult.AdminResultOnErrorGetAdmin -> {
                            mutableShowLocationAdminPopUp.value = Fail(admin.error)
                        }
                    }
                } catch (e: Exception) {
                    mutableProfileResponse.value = Fail(e)
                }
            }
        }
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
                val tokenResult = loginTokenV2UseCase(
                    LoginTokenV2GqlParam(
                        username = email,
                        password = finalPassword,
                        grantType = TYPE_PASSWORD,
                        hash = keyData.hash
                    )
                )
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
        launch {
            try {
                val data = loginFingerprintUseCase(createRequestParams(email, validateToken))
                LoginV2Mapper(userSession).map(
                    loginToken = data.loginToken,
                    onSuccessLoginToken = {
                        mutableLoginBiometricResponse.value = Success(it)
                    },
                    onErrorLoginToken = {
                        onFailedLoginBiometric(it)
                    },
                    onShowPopupError = {
                        showPopup(it.popupError)
                    },
                    onGoToActivationPage = { onGoToActivationPage(email) },
                    onGoToSecurityQuestion = { onGoToSecurityQuestion(email) }

                )
            } catch (e: Exception) {
                onFailedLoginBiometric(e)
            }
        }
    }

    private fun createRequestParams(email: String, validateToken: String): FingerPrintGqlParam {
        return FingerPrintGqlParam(
            grantType = TokenGenerator().encode(LoginFingerprintUseCase.TYPE_EXTENSION),
            socialType = LoginFingerprintUseCase.SOCIAL_TYPE_BIOMETRIC,
            username = TokenGenerator().encode(email),
            validateToken = validateToken,
            deviceBiometrics = fingerprintPreferenceManager.getUniqueId()
        )
    }

    private fun onFailedLoginBiometric(error: Throwable) {
        userSession.clearToken()
        mutableLoginBiometricResponse.value = Fail(error)
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

    private suspend fun getShopStatus() {
        try {
            val resp = getShopStatusUseCase("")
            _shopStatus.value = resp
        } catch (e: Exception) {
            _shopStatus.value = ShopStatus.Error(e)
        }
    }


    fun getConsent(clientId: String, scopes: String, lang: String = "") {
        launch {
            try {
                val result = loginSdkConsentUseCase(
                    SdkConsentParam(clientId, scopes, lang)
                )
                mutableConsent.value = Success(result.data)
            } catch (e: Exception) {
                mutableConsent.value = Fail(e)
            }
        }
    }

    fun validateClient(clientId: String, signature: String, packageName: String, redirectUri: String) {
        launch {
            try {
                val param = ValidateClientParam (
                    clientId = clientId,
                    signature = signature,
                    packageName = packageName,
                    redirectUri = redirectUri
                )
                mutableValidateClient.value = Success(validateClientUseCase(param).data)
            } catch (e: Exception) {
                mutableValidateClient.value = Fail(e)
            }
        }
    }

    fun authorizeSdk(clientId: String, redirectUri: String, codeChallenge: String) {
        launch {
            try {
                val result = authorizeSdkUseCase(
                    SdkAuthorizeParam(
                        clientId = clientId,
                        redirectUri = redirectUri,
                        responseType = "code",
                        codeChallenge = codeChallenge
                    )
                )
                if (result.data.isSuccess) {
                    mutableAuthorizeResponse.value = Success(result.data)
                } else {
                    mutableAuthorizeResponse.value = Fail(MessageErrorException(result.data.error))
                }
            } catch (e: Exception) {
                mutableAuthorizeResponse.value = Fail(e)
            }
        }
    }

    public override fun onCleared() {
        super.onCleared()
        clearBackgroundTask()
    }

    companion object {
        private const val PARAM_DISCOVER_LOGIN = "login"
        private val TYPE_PASSWORD: String = "password"
    }
}
