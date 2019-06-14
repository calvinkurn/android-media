package com.tokopedia.loginregister.login.view.presenter

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.view.listener.LoginContract
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel
import com.tokopedia.loginregister.login.view.subscriber.LoginTokenSubscriber
import com.tokopedia.loginregister.loginthirdparty.domain.LoginWebviewUseCase
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.loginthirdparty.subscriber.LoginThirdPartySubscriber
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterValidationPojo
import com.tokopedia.loginregister.registerinitial.domain.usecase.RegisterValidationUseCase
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule.SESSION_MODULE
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

Ø
/**
 * @author by nisie on 18/01/19.
 */
class LoginEmailPhonePresenter @Inject constructor(private val discoverUseCase: DiscoverUseCase,
                                                   private val getFacebookCredentialUseCase:
                                                   GetFacebookCredentialUseCase,
                                                   private val registerValidationUseCase:
                                                   RegisterValidationUseCase,
                                                   private val loginWebviewUseCase:
                                                   LoginWebviewUseCase,
                                                   private val loginTokenUseCase:
                                                   LoginTokenUseCase,
                                                   private val getProfileUseCase: GetProfileUseCase,
                                                   @Named(SESSION_MODULE)
                                                   private val userSession: UserSessionInterface)
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
        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken.token, LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                LoginTokenSubscriber(userSession,
                        view.onSuccessLoginFacebook(email),
                        view.onErrorLoginFacebook(email)))
    }

    override fun loginGoogle(accessToken: String, email: String?) {
        view.showLoadingLogin()
        loginTokenUseCase.executeLoginSocialMedia(LoginTokenUseCase.generateParamSocialMedia(
                accessToken, LoginTokenUseCase.SOCIAL_TYPE_GOOGLE),
                LoginTokenSubscriber(userSession,
                        view.onSuccessLoginGoogle(email),
                        view.onErrorLoginGoogle(email)))
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

    /**
     * Login Email :
     * Step 1 : Login Token
     * Step 2 : If Success, proceed to step 3. If need SQ, go to SQ
     * Step 2.1 : After SQ, take validation_token and login_token again
     * Step 3 : Get profile data
     * Step 4 : Proceed to home
     */
    override fun loginEmail(email: String, password: String) {
        view.resetError()
        if (isValid(email, password)) {
            view.showLoadingLogin()
            view.disableArrow()
            loginTokenUseCase.executeLoginEmailWithPassword(LoginTokenUseCase.generateParamLoginEmail(
                    email, password), LoginTokenSubscriber(userSession,
                    view.onSuccessLoginToken(email),
                    view.onErrorLoginEmail(email)))
        } else {
            viewEmailPhone.stopTrace()
        }
    }

    override fun reloginAfterSQ(validateToken: String) {
        loginTokenUseCase.executeLoginAfterSQ(LoginTokenUseCase.generateParamLoginAfterSQ(
                userSession, validateToken), LoginTokenSubscriber(userSession,
                view.onSuccessReloginAfterSQ(),
                view.onErrorReloginAfterSQ(validateToken)))
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

    override fun getUserInfo() {
        getProfileUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onNext(response: GraphqlResponse) {
                val pojo: ProfilePojo = response.getData<ProfilePojo>(ProfilePojo::class.java)
                //TODO Validate Data
                setUserData(pojo)
                view.onSuccessGetUserInfo(pojo)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                view.onErrorGetUserInfo(e)
            }

        })
    }

    private fun setUserData(pojo: ProfilePojo) {
        userSession.setHasPassword(pojo.profileInfo.isCreatedPassword)
        userSession.profilePicture = pojo.profileInfo.profilePicture
        userSession.setIsMSISDNVerified(pojo.profileInfo.isPhoneVerified)

        //TODO
        userSession.setLoginSession(true,
                pojo.profileInfo.userId,
                pojo.profileInfo.fullName,
                "",
                pojo.profileInfo.isPhoneVerified,
                "",
                pojo.profileInfo.email,
                false,
                pojo.profileInfo.phone)
        userSession.shopAvatar = ""
    }

    override fun detachView() {
        super.detachView()
        discoverUseCase.unsubscribe()
        registerValidationUseCase.unsubscribe()
        loginWebviewUseCase.unsubscribe()
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()

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