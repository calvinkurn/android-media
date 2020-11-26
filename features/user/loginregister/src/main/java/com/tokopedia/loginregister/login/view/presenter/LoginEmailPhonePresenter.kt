package com.tokopedia.loginregister.login.view.presenter

import android.content.Context
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.login.domain.StatusFingerprint
import com.tokopedia.loginregister.login.domain.StatusFingerprintUseCase
import com.tokopedia.loginregister.login.domain.StatusPinUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.StatusPinData
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.registerinitial.domain.usecase.RegisterValidationUseCase
import com.tokopedia.loginregister.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.ticker.subscriber.TickerInfoLoginSubscriber
import com.tokopedia.sessioncommon.di.SessionModule.SESSION_MODULE
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenFacebookSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 18/01/19.
 */
class LoginEmailPhonePresenter @Inject constructor(private val registerCheckUseCase: RegisterCheckUseCase,
                                                   private val discoverUseCase: DiscoverUseCase,
                                                   private val getFacebookCredentialUseCase:
                                                   GetFacebookCredentialUseCase,
                                                   private val registerValidationUseCase:
                                                   RegisterValidationUseCase,
                                                   private val loginTokenUseCase:
                                                   LoginTokenUseCase,
                                                   private val getProfileUseCase: GetProfileUseCase,
                                                   private val tickerInfoUseCase: TickerInfoUseCase,
                                                   private val statusPinUseCase: StatusPinUseCase,
                                                   private val dynamicBannerUseCase: DynamicBannerUseCase,
                                                   private val statusFingerprintUseCase: StatusFingerprintUseCase,
                                                   private val fingerprintPreferenceHelper: FingerprintSetting,
                                                   private var cryptographyUtils: Cryptography?,
                                                   @Named(SESSION_MODULE)
                                                   private val userSession: UserSessionInterface)
    : BaseDaggerPresenter<LoginEmailPhoneContract.View>(),
        LoginEmailPhoneContract.Presenter {

    private lateinit var viewEmailPhone: LoginEmailPhoneContract.View


    fun attachView(view: LoginEmailPhoneContract.View, viewEmailPhone: LoginEmailPhoneContract.View) {
        super.attachView(view)
        this.viewEmailPhone = viewEmailPhone
    }

    fun isLastUserRegistered(): Boolean = fingerprintPreferenceHelper.isFingerprintRegistered()

    override fun discoverLogin(context: Context) {
        view?.let { view ->
            discoverUseCase.execute(RequestParams.EMPTY, object : Subscriber<DiscoverViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.stopTrace()
                    view.dismissLoadingDiscover()
                    view.onErrorDiscoverLogin(e)
                }

                override fun onNext(discoverViewModel: DiscoverViewModel) {
                    view.stopTrace()
                    view.dismissLoadingDiscover()
                    view.onSuccessDiscoverLogin(discoverViewModel.providers)
                }
            })
        }
    }

    override fun getFacebookCredential(fragment: Fragment, callbackManager: CallbackManager) {
        view?.let { view ->
            userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK
            getFacebookCredentialUseCase.execute(GetFacebookCredentialUseCase.getParam(
                    fragment,
                    callbackManager),
                    GetFacebookCredentialSubscriber(view.getFacebookCredentialListener()))
        }
    }

    /**
     * Login Facebook :
     * Step 1 : Get token from facebook credential
     * Step 2 : Login Token
     * Step 3 : If Success, proceed to step 3. If need SQ, go to SQ
     * Step 3.1 : After SQ, take validation_token and login_token again
     * Step 4 : Get profile data
     * Step 5 : If name contains blocked word, go to add name
     * Step 6 : Proceed to home
     */
    override fun loginFacebook(context: Context, accessToken: AccessToken, email: String) {
        view?.let { view ->
            userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK
            view.showLoadingLogin()
            loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                    accessToken.token, LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                    LoginTokenSubscriber(userSession,
                            { getUserInfo() },
                            view.onErrorLoginFacebook(email),
                            view.onGoToActivationPage(email),
                            view.onGoToSecurityQuestion(email)))
        }
    }

    override fun loginFacebookPhone(context: Context, accessToken: AccessToken, phone: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK
        view.showLoadingLogin()
        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken.token, LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                LoginTokenFacebookSubscriber(userSession,
                        view.onSuccessLoginFacebookPhone(),
                        view.onErrorLoginFacebookPhone(),
                        view.onGoToSecurityQuestion(""))
        )
    }

    /**
     * Login Facebook :
     * Step 1 : Get token from google api
     * Step 2 : Login Token
     * Step 3 : If Success, proceed to step 3. If need SQ, go to SQ
     * Step 3.1 : After SQ, take validation_token and login_token again
     * Step 4 : Get profile data
     * Step 5 : If name contains blocked word, go to add name
     * Step 6 : Proceed to home
     */
    override fun loginGoogle(accessToken: String, email: String) {
        view?.let { view ->
            userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_GOOGLE

            view.showLoadingLogin()
            loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                    accessToken, LoginTokenUseCase.SOCIAL_TYPE_GOOGLE),
                    LoginTokenSubscriber(userSession,
                            { getUserInfo() },
                            view.onErrorLoginGoogle(email),
                            view.onGoToActivationPage(email),
                            view.onGoToSecurityQuestion(email)))
        }
    }

    /**
     * Login Email :
     * Step 1 : Login Token
     * Step 2 : If Success, proceed to step 3. If need SQ, go to SQ
     * Step 2.1 : After SQ, take validation_token and login_token again
     * Step 3 : Get profile data
     * Step 4 : If name contains blocked word, go to add name
     * Step 5 : Proceed to home
     */
    override fun loginEmail(email: String, password: String, isSmartLock: Boolean) {
        view?.let { view ->
            if (isSmartLock) {
                userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_EMAIL_SMART_LOCK
            } else {
                userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_EMAIL
            }

            view.resetError()
            if (isValid(email, password)) {
                view.showLoadingLogin()
                loginTokenUseCase.executeLoginEmailWithPassword(LoginTokenUseCase.generateParamLoginEmail(
                        email, password), LoginTokenSubscriber(userSession,
                        { view.onSuccessLoginEmail() },
                        view.onErrorLoginEmail(email),
                        view.onGoToActivationPage(email),
                        view.onGoToSecurityQuestion(email)))
            } else {
                viewEmailPhone.stopTrace()
            }
        }
    }

    override fun reloginAfterSQ(validateToken: String) {
        view?.let { view ->
            loginTokenUseCase.executeLoginAfterSQ(LoginTokenUseCase.generateParamLoginAfterSQ(
                    userSession, validateToken), LoginTokenSubscriber(userSession,
                    { getUserInfo() },
                    view.onErrorReloginAfterSQ(validateToken),
                    view.onGoToActivationPageAfterRelogin(),
                    view.onGoToSecurityQuestionAfterRelogin()))
        }
    }

    private fun isValid(email: String, password: String): Boolean {

        var isValid = true

        view?.let { view ->
            if (TextUtils.isEmpty(password)) {
                view.showErrorPassword(R.string.error_field_password_required)
                isValid = false
            } else if (password.length < 4) {
                view.showErrorPassword(R.string.error_incorrect_password)
                isValid = false
            }

            if (TextUtils.isEmpty(email)) {
                view.showErrorEmail(R.string.error_field_required)
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                view.showErrorEmail(R.string.error_invalid_email)
                isValid = false
            }
        }

        return isValid
    }

    override fun getUserInfo() {
        view?.let { view ->
            getProfileUseCase.execute(GetProfileSubscriber(userSession,
                    view.onSuccessGetUserInfo(),
                    view.onErrorGetUserInfo()))
        }
    }

    override fun getUserInfoFingerprint() {
        if(cryptographyUtils?.isInitialized() == true && view.getFingerprintConfig()) {
            view?.let { view ->
                getProfileUseCase.execute(GetProfileSubscriber(userSession,
                        { checkStatusFingerprint() },
                        view.onErrorGetUserInfo())
                )
            }
        }else {
            getUserInfo()
        }
    }

    override fun getTickerInfo() {
        tickerInfoUseCase.execute(TickerInfoUseCase.createRequestParam(TickerInfoUseCase.LOGIN_PAGE),
                TickerInfoLoginSubscriber(viewEmailPhone))
    }

    override fun checkStatusPin(onSuccess: (StatusPinData) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit) {
        statusPinUseCase.executeCoroutines(onSuccess, onError)
    }

    override fun checkStatusFingerprint() {
        if(cryptographyUtils?.isInitialized() == true) {
            val signature = cryptographyUtils?.generateFingerprintSignature(userId = userSession.userId, deviceId = userSession.deviceId)
            signature?.run {
                statusFingerprintUseCase.executeCoroutines({
                    onCheckStatusFingerprintSuccess(it)
                }, {
                    view.onSuccessLogin()
                }, this)
            }
        }else {
            view.onSuccessLogin()
        }
    }

    private fun saveFingerprintStatus(data: StatusFingerprint){
        if(data.isValid) {
            fingerprintPreferenceHelper.saveUserId(userSession.userId)
            fingerprintPreferenceHelper.registerFingerprint()
        }else {
            removeFingerprintData()
        }
    }

    override fun removeFingerprintData(){
        fingerprintPreferenceHelper.removeUserId()
        fingerprintPreferenceHelper.unregisterFingerprint()
    }

    private fun onCheckStatusFingerprintSuccess(data: StatusFingerprint){
        saveFingerprintStatus(data)
        view.onSuccessCheckStatusFingerprint(data)
    }

    override fun registerCheck(id: String, onSuccess: (RegisterCheckData) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit) {
        registerCheckUseCase.apply {
            setRequestParams(this.getRequestParams(id))
            execute({
                if (it.data.errors.isEmpty())
                    onSuccess(it.data)
                else if (it.data.errors.isNotEmpty() && it.data.errors[0].isNotEmpty())
                    onError(com.tokopedia.network.exception.MessageErrorException(it.data.errors[0]))
                else onError(RuntimeException())
            }, onError)
        }
    }

    override fun getDynamicBanner(page: String) {
        val params = DynamicBannerUseCase.createRequestParams(page)
        dynamicBannerUseCase.createParams(params)
        dynamicBannerUseCase.execute(onSuccess = {
            view.onGetDynamicBannerSuccess(it)
        }, onError = {
            view.onGetDynamicBannerError(it)
        })
    }

    override fun detachView() {
        super.detachView()
        registerCheckUseCase.cancelJobs()
        statusPinUseCase.cancelJobs()
        discoverUseCase.unsubscribe()
        registerValidationUseCase.unsubscribe()
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
        tickerInfoUseCase.unsubscribe()
    }
}