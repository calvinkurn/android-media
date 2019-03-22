package com.tokopedia.loginregister.login.view.presenter

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.view.listener.LoginContract
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel
import com.tokopedia.loginregister.login.view.subscriber.LoginSubscriber
import com.tokopedia.loginregister.loginthirdparty.domain.LoginWebviewUseCase
import com.tokopedia.loginregister.loginthirdparty.domain.LoginWithSosmedUseCase
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.loginthirdparty.subscriber.LoginThirdPartySubscriber
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterValidationPojo
import com.tokopedia.loginregister.registerinitial.domain.usecase.RegisterValidationUseCase
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.domain.usecase.LoginEmailUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 18/01/19.
 */
class LoginEmailPhonePresenter @Inject constructor(private val discoverUseCase: DiscoverUseCase,
                                                   private val getFacebookCredentialUseCase:
                                                   GetFacebookCredentialUseCase,
                                                   private val loginWithSosmedUseCase:
                                                   LoginWithSosmedUseCase,
                                                   private val registerValidationUseCase:
                                                   RegisterValidationUseCase,
                                                   private val loginEmailUseCase:
                                                   LoginEmailUseCase,
                                                   private val loginWebviewUseCase: LoginWebviewUseCase
)
    : BaseDaggerPresenter<LoginContract.View>(),
        LoginEmailPhoneContract.Presenter {

    private lateinit var viewEmailPhone: LoginEmailPhoneContract.View
    private val PHONE_TYPE = "phone"
    private val EMAIL_TYPE = "email"

    fun attachView(view: LoginContract.View, viewEmailPhone: LoginEmailPhoneContract.View) {
        super.attachView(view)
        this.viewEmailPhone = viewEmailPhone
    }

    override fun discoverLogin(context: Context) {
        discoverUseCase.execute(RequestParams.EMPTY, object : Subscriber<DiscoverViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.stopTrace()
                view.dismissLoadingDiscover()
                ErrorHandlerSession.getErrorMessage(object : ErrorHandlerSession.ErrorForbiddenListener {
                    override fun onForbidden() {
                        view.getLoginRouter().onForbidden()
                    }

                    override fun onError(errorMessage: String) {
                        view.onErrorDiscoverLogin(errorMessage)
                    }
                }, e, context)
            }

            override fun onNext(discoverViewModel: DiscoverViewModel) {
                view.stopTrace()
                view.dismissLoadingDiscover()
                if (!discoverViewModel.providers.isEmpty()) {
                    view.onSuccessDiscoverLogin(discoverViewModel.providers)
                } else {
                    view.onErrorDiscoverLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(
                            ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW,
                            context))
                }
            }
        })
    }

    override fun checkLoginEmailPhone(emailPhone: String) {
        if (emailPhone.isBlank()) {
            viewEmailPhone.onErrorEmptyEmailPhone()
        } else {
            registerValidationUseCase.execute(RegisterValidationUseCase.createValidateRegisterParam(emailPhone),
                    object : Subscriber<RegisterValidationPojo>() {

                        override fun onCompleted() {

                        }

                        override fun onError(throwable: Throwable) {
                            viewEmailPhone.onErrorValidateRegister(throwable)
                        }

                        override fun onNext(registerValidationViewModel: RegisterValidationPojo) {
                            viewEmailPhone.trackSuccessValidate()
                            onSuccessValidate(registerValidationViewModel)
                        }
                    })
        }
    }

    private fun onSuccessValidate(model: RegisterValidationPojo) {
        if (TextUtils.equals(model.type, PHONE_TYPE)) {
            if (model.exist) {
                viewEmailPhone.goToLoginPhoneVerifyPage(model.view.replace("-", ""))
            } else {
                viewEmailPhone.goToRegisterPhoneVerifyPage(model.view.replace("-", ""))
            }

        }

        if (TextUtils.equals(model.type, EMAIL_TYPE)) {
            if (model.exist) {
                viewEmailPhone.onEmailExist(model.view)
            } else {
                viewEmailPhone.showNotRegisteredEmailDialog(model.view)
            }
        }
    }

    override fun getFacebookCredential(fragment: Fragment, callbackManager: CallbackManager) {
        getFacebookCredentialUseCase.execute(GetFacebookCredentialUseCase.getParam(
                fragment,
                callbackManager),
                GetFacebookCredentialSubscriber(view.facebookCredentialListener))
    }

    override fun loginFacebook(context: Context, accessToken: AccessToken, email: String) {
        view.showLoadingLogin()
        loginWithSosmedUseCase.execute(LoginWithSosmedUseCase.getParamFacebook(accessToken),
                LoginThirdPartySubscriber(context, view.loginRouter,
                        email, view,  LoginRegisterAnalytics.FACEBOOK))
    }

    override fun loginGoogle(accessToken: String?, email: String?) {
        view.showLoadingLogin()
        loginWithSosmedUseCase.execute(LoginWithSosmedUseCase.getParamGoogle(accessToken),
                LoginThirdPartySubscriber(view.context, view.loginRouter,
                        email, view, LoginRegisterAnalytics.GOOGLE))
    }


    override fun loginWebview(data: Intent?) {
        val BUNDLE = "bundle"
        val ERROR = "error"
        val CODE = "code"
        val MESSAGE = "message"
        val SERVER = "server"
        val PATH = "path"
        val HTTPS = "https://"
        val ACTIVATION_SOCIAL = "activation-social"

        if (!(data?.getBundleExtra(BUNDLE) == null
                        || data.getBundleExtra(BUNDLE).getString(PATH) == null)) {
            val bundle = data.getBundleExtra(BUNDLE)
            if (bundle.getString(PATH, "").contains(ERROR)) {
                view.onErrorLoginSosmed(LoginRegisterAnalytics.WEBVIEW,
                        bundle.getString(MESSAGE, "")
                                + view.context.getString(R.string.code_error) + " " +
                                ErrorHandlerSession.ErrorCode.WS_ERROR)
            } else if (bundle.getString(PATH, "").contains(CODE)) {
                view.showLoadingLogin()
                loginWebviewUseCase.execute(LoginWebviewUseCase.getParamWebview(bundle.getString(CODE, ""), (HTTPS + bundle.getString(SERVER) + bundle.getString(PATH))),
                        LoginThirdPartySubscriber(view.context,
                                view.loginRouter,
                                "", view, LoginRegisterAnalytics.WEBVIEW))
            } else if (bundle.getString(PATH, "").contains(ACTIVATION_SOCIAL)) {
                view.onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(
                        ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW,
                        view.context))
            }
        } else {
            view.onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(
                    ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW,
                    view.context))
        }
    }


    override fun login(email: String, password: String) {
        view.resetError()
        if (isValid(email, password)) {
            view.showLoadingLogin()
            view.disableArrow()
            loginEmailUseCase.execute(LoginEmailUseCase.getParam(email, password),
                    LoginSubscriber(view.context, view.loginRouter,
                            email, view))
        } else {
            viewEmailPhone.stopTrace()
        }
    }

    private fun isValid(email: String, password: String): Boolean {
        var isValid = true

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

        return isValid
    }

    override fun detachView() {
        super.detachView()
        discoverUseCase.unsubscribe()
        loginWithSosmedUseCase.unsubscribe()
        registerValidationUseCase.unsubscribe()
        loginEmailUseCase.unsubscribe()
        loginWebviewUseCase.unsubscribe()

    }

    override fun discoverLogin() {
        //use the other method
    }

    override fun loginFacebook(accessToken: AccessToken?, email: String?) {
        //use the other method
    }

    override fun saveLoginEmail(email: String?) {
        //NOT USED
    }

    override fun getLoginIdList(): ArrayList<String> {
        //NOT USED
        return ArrayList()
    }

}