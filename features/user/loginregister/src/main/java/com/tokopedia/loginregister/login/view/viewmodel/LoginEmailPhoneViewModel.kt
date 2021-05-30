package com.tokopedia.loginregister.login.view.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.common.data.ResponseConverter
import com.tokopedia.loginregister.common.data.ResponseConverter.resultUsecaseCoroutineToSubscriber
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.login.domain.StatusPinUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.StatusPinData
import com.tokopedia.loginregister.login.view.model.DiscoverDataModel
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.mapper.LoginV2Mapper
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenFacebookSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class LoginEmailPhoneViewModel @Inject constructor(
        private val registerCheckUseCase: RegisterCheckUseCase,
        private val discoverUseCase: DiscoverUseCase,
        private val activateUserUseCase: ActivateUserUseCase,
        private val getFacebookCredentialUseCase: GetFacebookCredentialUseCase,
        private val loginTokenUseCase: LoginTokenUseCase,
        private val getProfileUseCase: GetProfileUseCase,
        private val tickerInfoUseCase: TickerInfoUseCase,
        private val statusPinUseCase: StatusPinUseCase,
        private val getAdminTypeUseCase: GetAdminTypeUseCase,
        private val loginTokenV2UseCase: LoginTokenV2UseCase,
        private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
        private val dynamicBannerUseCase: DynamicBannerUseCase,
        @Named(SessionModule.SESSION_MODULE)
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val mutableRegisterCheckResponse = MutableLiveData<Result<RegisterCheckData>>()
    val registerCheckResponse: LiveData<Result<RegisterCheckData>>
        get() = mutableRegisterCheckResponse

    private val mutableDiscoverResponse = MutableLiveData<Result<DiscoverDataModel>>()
    val discoverResponse: LiveData<Result<DiscoverDataModel>>
        get() = mutableDiscoverResponse

    private val mutableActivateResponse = MutableLiveData<Result<ActivateUserData>>()
    val activateResponse: LiveData<Result<ActivateUserData>>
        get() = mutableActivateResponse

    private val mutableLoginTokenResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenResponse

    private val mutableLoginTokenV2Response = MutableLiveData<Result<LoginTokenPojoV2>>()
    val loginTokenV2Response: LiveData<Result<LoginTokenPojoV2>>
        get() = mutableLoginTokenV2Response

    private val mutableProfileResponse = MutableLiveData<Result<ProfilePojo>>()
    val profileResponse: LiveData<Result<ProfilePojo>>
        get() = mutableProfileResponse

    private val mutableGetFacebookCredentialResponse = MutableLiveData<Result<FacebookCredentialData>>()
    val getFacebookCredentialResponse: LiveData<Result<FacebookCredentialData>>
        get() = mutableGetFacebookCredentialResponse

    private val mutableLoginTokenFacebookResponse = MutableLiveData<Result<LoginToken>>()
    val loginTokenFacebookResponse: LiveData<Result<LoginToken>>
        get() = mutableLoginTokenFacebookResponse

    private val mutableLoginTokenFacebookPhoneResponse = MutableLiveData<Result<LoginTokenPojo>>()
    val loginTokenFacebookPhoneResponse: LiveData<Result<LoginTokenPojo>>
        get() = mutableLoginTokenFacebookPhoneResponse

    private val mutableShowPopup = MutableLiveData<PopupError>()
    val showPopup: LiveData<PopupError>
        get() = mutableShowPopup

    private val mutableGoToActivationPage = MutableLiveData<String>()
    val goToActivationPage: LiveData<String>
        get() = mutableGoToActivationPage

    private val mutableShowLocationAdminPopUp = MutableLiveData<Result<Boolean>>()
    val showLocationAdminPopUp: LiveData<Result<Boolean>>
        get() = mutableShowLocationAdminPopUp

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

    private val mutableGetStatusPinResponse = MutableLiveData<Result<StatusPinData>>()
    val getStatusPinResponse: LiveData<Result<StatusPinData>>
        get() = mutableGetStatusPinResponse

    private val mutableDynamicBannerResponse = MutableLiveData<Result<DynamicBannerDataModel>>()
    val dynamicBannerResponse: LiveData<Result<DynamicBannerDataModel>>
        get() = mutableDynamicBannerResponse

    fun registerCheck(id: String) {
        registerCheckUseCase.apply {
            setRequestParams(this.getRequestParams(id))
            execute({
                if (it.data.errors.isEmpty())
                    mutableRegisterCheckResponse.value = Success(it.data)
                else if (it.data.errors.isNotEmpty() && it.data.errors[0].isNotEmpty()) {
                    mutableRegisterCheckResponse.value = Fail(MessageErrorException(it.data.errors[0]))
                } else mutableRegisterCheckResponse.value = Fail(RuntimeException())
            }, {
                mutableRegisterCheckResponse.value = Fail(it)
            })
        }
    }

    fun discoverLogin() {
        launchCatchError(coroutineContext, {
            discoverUseCase.execute(RequestParams.EMPTY, resultUsecaseCoroutineToSubscriber(
                    onSuccessResult = { mutableDiscoverResponse.value = Success(it) },
                    onErrorResult = { mutableDiscoverResponse.value = Fail(it) }
            ))
        }, {
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
                showErrorGetAdminType = {
                    mutableShowLocationAdminPopUp.value = Fail(it)
                }
        ))
    }

    fun loginFacebook(accessToken: AccessToken, email: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK
        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken.token, LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                LoginTokenSubscriber(userSession,
                        {
                            onSuccessLoginTokenFacebook(it)
                        },
                        {
                            onFailedLoginTokenFacebook(it)
                        },
                        { showPopup(it.loginToken.popupError) },
                        { onGoToActivationPage(email) },
                        { onGoToSecurityQuestion(email) }
                ))
    }

    fun loginFacebookPhone(accessToken: AccessToken, phone: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK
        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken.token, LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                LoginTokenFacebookSubscriber(userSession,
                        {
                            mutableLoginTokenFacebookPhoneResponse.value = Success(it)
                        }, {
                            mutableLoginTokenFacebookPhoneResponse.value = Fail(it)
                        },
                        { showPopup(it.loginToken.popupError) },
                        { onGoToSecurityQuestion("") }
                )
        )
    }

    fun loginGoogle(accessToken: String, email: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_GOOGLE
        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken, LoginTokenUseCase.SOCIAL_TYPE_GOOGLE),
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
                email, password), LoginTokenSubscriber(userSession,
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
            if(keyData.key.isNotEmpty()) {
                var finalPassword = password
                if(useHash) {
                    finalPassword = RsaUtils.encrypt(password, keyData.key.decodeBase64(), useHash)
                }
                loginTokenV2UseCase.setParams(email, finalPassword, keyData.hash)
                val tokenResult = loginTokenV2UseCase.executeOnBackground()
                LoginV2Mapper(userSession).map(tokenResult,
                        onSuccessLoginToken = {
                            mutableLoginTokenV2Response.value = Success(it)
                        },
                        onErrorLoginToken = {
                            mutableLoginTokenV2Response.value = Fail(it)
                        },
                        onShowPopupError = { showPopup(it.loginToken.popupError) },
                        onGoToActivationPage = { onGoToActivationPage(email) },
                        onGoToSecurityQuestion = { onGoToSecurityQuestion(email) }
                )
            }
            else {
                mutableLoginTokenV2Response.value = Fail(MessageErrorException("Failed"))
            }
        }, {
            mutableLoginTokenV2Response.value = Fail(it)
        })
    }

    fun reloginAfterSQ(validateToken: String) {
        loginTokenUseCase.executeLoginAfterSQ(LoginTokenUseCase.generateParamLoginAfterSQ(
                userSession, validateToken), LoginTokenSubscriber(userSession,
                {
                    mutableLoginTokenAfterSQResponse.value = Success(it)
                },
                {
                    mutableLoginTokenAfterSQResponse.value = Fail(it)
                },
                { showPopup(it.loginToken.popupError) },
                { onGoToActivationPageAfterRelogin(it) },
                { onGoToSecurityQuestionAfterRelogin("") })
        )
    }

    fun getTickerInfo() {
        launchCatchError(coroutineContext, {
            val params = TickerInfoUseCase.createRequestParam(TickerInfoUseCase.LOGIN_PAGE)
            val ticker = tickerInfoUseCase.createObservable(params).toBlocking().single()
            mutableGetTickerInfoResponse.value = Success(ticker)
        }, {
            mutableGetTickerInfoResponse.value = Fail(it)
        })
    }

    fun checkStatusPin() {
        launchCatchError(coroutineContext, {
            val statusPin = statusPinUseCase.executeOnBackground()
            mutableGetStatusPinResponse.value = Success(statusPin.data)
        }, {
            mutableGetStatusPinResponse.value = Fail(it)
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

    fun getFacebookCredential(fragment: Fragment, callbackManager: CallbackManager) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK
        getFacebookCredentialUseCase.execute(
                GetFacebookCredentialUseCase.getParam(
                        fragment,
                        callbackManager),
                GetFacebookCredentialSubscriber(
                        ResponseConverter.resultUsecaseCoroutineToFacebookCredentialListener(
                                {
                                    onSuccessGetFacebookEmailCredential().invoke(it)
                                },
                                {
                                    onSuccessGetFacebookPhoneCredential(it)
                                },
                                {
                                    onFailedGetFacebookCredential(it)
                                }
                        )
                )
        )
    }

    private fun onSuccessGetFacebookEmailCredential(): (FacebookCredentialData) -> Unit {
        return {
            mutableGetFacebookCredentialResponse.value = Success(it)
        }
    }

    private fun onSuccessGetFacebookPhoneCredential(facebookCredentialData: FacebookCredentialData) {
        mutableGetFacebookCredentialResponse.value = Success(facebookCredentialData)
    }

    private fun onFailedGetFacebookCredential(throwable: Throwable) {
        mutableGetFacebookCredentialResponse.value = Fail(throwable)
    }

    private fun onFailedLoginTokenFacebook(throwable: Throwable) {
        mutableLoginTokenFacebookResponse.value = Fail(throwable)
    }

    private fun onSuccessLoginTokenFacebook(loginToken: LoginTokenPojo) {
        mutableLoginTokenFacebookResponse.value = Success(loginToken.loginToken)
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
        discoverUseCase.unsubscribe()
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
    }

    override fun onCleared() {
        super.onCleared()
        clearBackgroundTask()
    }
}