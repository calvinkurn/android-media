package com.tokopedia.loginregister.registerinitial.view.presenter

import android.content.Intent
import android.support.v4.app.Fragment
import android.text.TextUtils

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel
import com.tokopedia.loginregister.loginthirdparty.domain.LoginWebviewUseCase
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.registerinitial.domain.usecase.RegisterValidationUseCase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterValidationPojo
import com.tokopedia.loginregister.registerinitial.view.listener.RegisterInitialContract
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.user.session.UserSessionInterface

import javax.inject.Inject

import rx.Subscriber
import javax.inject.Named

/**
 * @author by nisie on 10/24/18.
 */
class RegisterInitialPresenter @Inject constructor(
        private val discoverUseCase: DiscoverUseCase,
        private val getFacebookCredentialUseCase: GetFacebookCredentialUseCase,
        private val registerValidationUseCase: RegisterValidationUseCase,
        private val loginTokenUseCase: LoginTokenUseCase,
        private val getProfileUseCase: GetProfileUseCase,
        @Named(SessionModule.SESSION_MODULE)
        private val userSession: UserSessionInterface) : BaseDaggerPresenter<RegisterInitialContract.View>(), RegisterInitialContract.Presenter {

    override fun getProvider() {
        view.showLoadingDiscover()
        discoverUseCase.execute(DiscoverUseCase.getParamRegister(), object : Subscriber<DiscoverViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.dismissLoadingDiscover()
                view.onErrorDiscoverRegister(e)
            }

            override fun onNext(discoverViewModel: DiscoverViewModel) {
                view.dismissLoadingDiscover()
                if (!discoverViewModel.providers.isEmpty()) {
                    view.onSuccessDiscoverRegister(discoverViewModel.providers)
                } else {
                    view.onErrorDiscoverRegister(Throwable())
                }
            }
        })
    }

    override fun detachView() {
        super.detachView()
        discoverUseCase.unsubscribe()
        registerValidationUseCase.unsubscribe()
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
    }

    override fun validateRegister(id: String) {
        registerValidationUseCase.execute(RegisterValidationUseCase.createValidateRegisterParam(id),
                object : Subscriber<RegisterValidationPojo>() {

                    override fun onCompleted() {

                    }

                    override fun onError(throwable: Throwable) {
                        view.onErrorValidateRegister(throwable)

                    }

                    override fun onNext(registerValidationViewModel: RegisterValidationPojo) {
                        onSuccessValidate(registerValidationViewModel)
                    }
                })
    }

    private fun onSuccessValidate(model: RegisterValidationPojo) {
        if (TextUtils.equals(model.type, PHONE_TYPE)) {
            view.setTempPhoneNumber(model.view)
            if (model.exist!!) {
                view.showRegisteredPhoneDialog(model.view)
            } else {
                view.showProceedWithPhoneDialog(model.view)
            }
        }

        if (TextUtils.equals(model.type, EMAIL_TYPE)) {
            if (model.exist!!) {
                view.showRegisteredEmailDialog(model.view)
            } else {
                view.goToRegisterEmailPageWithEmail(model.view)
            }
        }
    }

    override fun getFacebookCredential(fragment: Fragment, callbackManager: CallbackManager) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK
        getFacebookCredentialUseCase.execute(
                GetFacebookCredentialUseCase.getParam(
                        fragment,
                        callbackManager),
                GetFacebookCredentialSubscriber(view.facebookCredentialListener))
    }

    override fun registerFacebook(accessToken: AccessToken, email: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_FACEBOOK
        view.showProgressBar()

        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken.token, LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                LoginTokenSubscriber(userSession,
                        { getUserInfo() },
                        view.onErrorLoginFacebook(email),
                        view.onGoToActivationPage(email),
                        view.onGoToSecurityQuestion(email)))
    }

    override fun registerGoogle(accessToken: String, email: String) {
        userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_GOOGLE

        view.showProgressBar()
        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken, LoginTokenUseCase.SOCIAL_TYPE_GOOGLE),
                LoginTokenSubscriber(userSession,
                        { getUserInfo() },
                        view.onErrorLoginGoogle(email),
                        view.onGoToActivationPage(email),
                        view.onGoToSecurityQuestion(email)))
    }

    override fun getUserInfo() {
        getProfileUseCase.execute(GetProfileSubscriber(userSession,
                view.onSuccessGetUserInfo(),
                view.onErrorGetUserInfo(),
                view.onGoToCreatePassword(),
                view.onGoToPhoneVerification()))
    }

    companion object {

        private val BUNDLE_WEBVIEW = "bundle"
        private val ARGS_PATH = "path"
        private val ARGS_ERROR = "error"
        private val ARGS_MESSAGE = "message"
        private val ARGS_CODE = "code"
        private val ARGS_SERVER = "server"
        private val HTTPS = "https://"
        private val PHONE_TYPE = "phone"
        private val EMAIL_TYPE = "email"
    }
}
