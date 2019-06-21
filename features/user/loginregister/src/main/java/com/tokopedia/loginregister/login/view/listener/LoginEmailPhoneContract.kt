package com.tokopedia.loginregister.login.view.listener

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import java.util.ArrayList

/**
 * @author by nisie on 18/01/19.
 */
interface LoginEmailPhoneContract {

    interface View : CustomerView {
        fun resetError()

        fun showLoadingLogin()

        fun showErrorPassword(resId: Int)

        fun showErrorEmail(resId: Int)

        fun dismissLoadingLogin()

        fun onSuccessLogin()

        fun showLoadingDiscover()

        fun dismissLoadingDiscover()

        fun onErrorDiscoverLogin(errorMessage: String)

        fun onSuccessDiscoverLogin(providers: ArrayList<DiscoverItemViewModel>)

        fun getFacebookCredentialListener(): GetFacebookCredentialSubscriber.GetFacebookCredentialListener

        fun isFromRegister(): Boolean

        fun setSmartLock()

        fun stopTrace()

        fun onErrorLoginEmail(email: String): Function1<Throwable, Unit>

        fun onErrorReloginAfterSQ(validateToken: String): Function1<Throwable, Unit>

        fun onErrorLoginFacebook(email: String): Function1<Throwable, Unit>

        fun onErrorLoginGoogle(email: String?): Function1<Throwable, Unit>

        fun onSuccessGetUserInfo(): Function1<ProfilePojo, Unit>

        fun onErrorGetUserInfo(): Function1<Throwable, Unit>

        fun onGoToCreatePassword(): Function2<String, String, Unit>

        fun onGoToPhoneVerification(): Function0<Unit>

        fun onGoToActivationPage(email: String): Function1<MessageErrorException, Unit>

        fun onGoToSecurityQuestion(email: String): Function0<Unit>

        fun onGoToActivationPageAfterRelogin(): Function1<MessageErrorException, Unit>

        fun onGoToSecurityQuestionAfterRelogin(): Function0<Unit>

        fun onGoToForbiddenPage()

        fun onErrorValidateRegister(throwable: Throwable)

        fun onErrorEmptyEmailPhone()

        fun goToLoginPhoneVerifyPage(phoneNumber: String)

        fun goToRegisterPhoneVerifyPage(phoneNumber: String)

        fun onEmailExist(email: String)

        fun showNotRegisteredEmailDialog(email: String)

        fun onBackPressed()

        fun trackSuccessValidate()
    }

    interface Presenter : CustomerPresenter<View> {
        fun loginEmail(email: String, password: String)

        fun loginGoogle(accessToken: String, email: String)

        fun getFacebookCredential(fragment: Fragment, callbackManager: CallbackManager)

        fun getUserInfo()

        fun discoverLogin(context: Context)

        fun checkLoginEmailPhone(emailPhone: String)

        fun loginFacebook(context: Context, accessToken: AccessToken, email: String)

        fun reloginAfterSQ(validateToken: String)
    }
}