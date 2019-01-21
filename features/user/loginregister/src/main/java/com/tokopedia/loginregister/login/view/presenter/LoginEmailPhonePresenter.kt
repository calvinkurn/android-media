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
                                                   private val loginEmailUseCase: LoginEmailUseCase)
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
                            onSuccessValidate(registerValidationViewModel)
                        }
                    })
        }
    }

    private fun onSuccessValidate(model: RegisterValidationPojo) {
        //TODO
        if (TextUtils.equals(model.type, PHONE_TYPE)) {
//            view.setTempPhoneNumber(model.view)
            if (model.exist) {
                viewEmailPhone.goToLoginPhoneVerifyPage(model.view)
            } else {
//                view.showNotRegisteredPhoneDialog(model.view)
                viewEmailPhone.goToLoginPhoneVerifyPage(model.view)
            }
        }

        if (TextUtils.equals(model.type, EMAIL_TYPE)) {
            if (model.exist) {
                viewEmailPhone.onEmailExist(model.view)
            } else {
//                viewEmailPhone.showNotRegisteredEmailDialog(model.view)
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
                        LoginRegisterAnalytics.FACEBOOK, view, email))
    }

    override fun loginGoogle(accessToken: String?, email: String?) {
        view.showLoadingLogin()
        loginWithSosmedUseCase.execute(LoginWithSosmedUseCase.getParamGoogle(accessToken),
                LoginThirdPartySubscriber(view.context, view.loginRouter,
                        email, view, LoginRegisterAnalytics.GOOGLE))
    }

    override fun login(email: String, password: String) {
        view.resetError()
        if (isValid(email, password)) {
            view.showLoadingLogin()
            view.disableArrow()
            loginEmailUseCase.execute(LoginEmailUseCase.getParam(email, password),
                    LoginSubscriber(view.context, view.loginRouter,
                            email, view))
        }
    }

    private fun isValid(email: String, password: String): Boolean {
        var isValid = true

        if (TextUtils.isEmpty(password)) {
            view.showErrorPassword(R.string.error_field_required)
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
    }

    //IGNORE FOR NOW

    override fun discoverLogin() {
        //use the other method
    }

    override fun loginFacebook(accessToken: AccessToken?, email: String?) {
        //use the other method
    }



    override fun saveLoginEmail(email: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLoginIdList(): ArrayList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loginWebview(data: Intent?) {
        //NOT USED
    }

}