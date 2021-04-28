package com.tokopedia.logintest.login.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logintest.R
import com.tokopedia.logintest.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.logintest.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.logintest.common.view.ticker.subscriber.TickerInfoLoginSubscriber
import com.tokopedia.logintest.login.domain.RegisterCheckTestAppUseCase
import com.tokopedia.logintest.login.domain.StatusPinTestAppUseCase
import com.tokopedia.logintest.login.domain.pojo.RegisterCheckData
import com.tokopedia.logintest.login.domain.pojo.StatusPinData
import com.tokopedia.logintest.login.view.listener.LoginTestAppContract
import com.tokopedia.sessioncommon.di.SessionModule.SESSION_MODULE
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 18/01/19.
 */
class LoginTestAppPresenter @Inject constructor(private val registerCheckTestAppUseCase: RegisterCheckTestAppUseCase,
                                                private val loginTokenUseCase:
                                                   LoginTokenUseCase,
                                                private val getProfileUseCase: GetProfileUseCase,
                                                private val tickerInfoUseCase: TickerInfoUseCase,
                                                private val statusPinUseCase: StatusPinTestAppUseCase,
                                                private val dynamicBannerUseCase: DynamicBannerUseCase,
                                                @Named(SESSION_MODULE)
                                                   private val userSession: UserSessionInterface)
    : BaseDaggerPresenter<LoginTestAppContract.View>(),
        LoginTestAppContract.Presenter {

    private lateinit var viewEmailPhone: LoginTestAppContract.View


    fun attachView(view: LoginTestAppContract.View, viewEmailPhone: LoginTestAppContract.View) {
        super.attachView(view)
        this.viewEmailPhone = viewEmailPhone
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
                        {},
                        {},
                        {}))
            } else {
                viewEmailPhone.stopTrace()
            }
        }
    }

    override fun reloginAfterSQ(validateToken: String) {
        view?.let { view ->
            loginTokenUseCase.executeLoginAfterSQ(LoginTokenUseCase.generateParamLoginAfterSQ(
                    userSession, validateToken), LoginTokenSubscriber(userSession,
                    { getUserInfoAddPin() },
                    view.onErrorReloginAfterSQ(validateToken),
                    {},
                    {},
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

    override fun getUserInfoAddPin() {
        view?.let { view ->
            getProfileUseCase.execute(GetProfileSubscriber(userSession,
                    view.onSuccessGetUserInfoAddPin(),
                    view.onErrorGetUserInfo())
            )
        }
    }

    override fun getTickerInfo() {
        tickerInfoUseCase.execute(TickerInfoUseCase.createRequestParam(TickerInfoUseCase.LOGIN_PAGE),
                TickerInfoLoginSubscriber(viewEmailPhone))
    }

    override fun checkStatusPin(onSuccess: (StatusPinData) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit) {
        statusPinUseCase.executeCoroutines(onSuccess, onError)
    }

    override fun registerCheck(id: String, onSuccess: (RegisterCheckData) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit) {
        registerCheckTestAppUseCase.apply {
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
        registerCheckTestAppUseCase.cancelJobs()
        statusPinUseCase.cancelJobs()
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
        tickerInfoUseCase.unsubscribe()
    }
}