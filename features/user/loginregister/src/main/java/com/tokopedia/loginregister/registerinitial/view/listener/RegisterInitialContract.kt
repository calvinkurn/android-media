package com.tokopedia.loginregister.registerinitial.view.listener

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
import com.tokopedia.sessioncommon.view.LoginSuccessRouter

import java.util.ArrayList

/**
 * @author by nisie on 10/24/18.
 */
interface RegisterInitialContract {

    interface View : CustomerView {

        val facebookCredentialListener: GetFacebookCredentialSubscriber.GetFacebookCredentialListener

        val loginRouter: LoginSuccessRouter

        fun showLoadingDiscover()

        fun onErrorDiscoverRegister(errorMessage: Throwable)

        fun onSuccessDiscoverRegister(discoverViewModel: ArrayList<DiscoverItemViewModel>)

        fun dismissLoadingDiscover()

        fun showProgressBar()

        fun dismissProgressBar()

        fun showRegisteredEmailDialog(email: String)

        fun showRegisteredPhoneDialog(phone: String)

        fun showProceedWithPhoneDialog(phone: String)

        fun goToLoginPage()

        fun goToRegisterEmailPageWithEmail(email: String)

        fun onErrorValidateRegister(message: Throwable)

        fun setTempPhoneNumber(maskedPhoneNumber: String)

        fun onGoToAddName()

        fun onBackPressed()

        fun onErrorLoginFacebook(email: String): (e: Throwable) -> Unit

        fun onErrorLoginGoogle(email: String): (e: Throwable) -> Unit

        fun onGoToActivationPage(email: String): (errorMessage: MessageErrorException) -> Unit

        fun onGoToSecurityQuestion(email: String): () -> Unit

        fun onSuccessGetUserInfo(): (pojo: ProfilePojo) -> Unit

        fun onErrorGetUserInfo(): (e: Throwable) -> Unit

        fun onGoToCreatePassword(): (fullName: String, userId: String) -> Unit

        fun onGoToPhoneVerification(): () -> Unit

        fun onGoToChangeName()

        fun onGoToForbiddenPage()

    }

    interface Presenter : CustomerPresenter<View> {

        fun getProvider()

        fun getFacebookCredential(fragment: Fragment, callbackManager: CallbackManager)

        fun registerFacebook(accessToken: AccessToken, email: String)

        fun registerGoogle(accessToken: String, email: String)

        fun validateRegister(id: String)

        fun getUserInfo()
    }

}