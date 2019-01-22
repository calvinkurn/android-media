package com.tokopedia.loginregister.login.view.listener

import android.content.Context
import com.facebook.AccessToken

/**
 * @author by nisie on 18/01/19.
 */
interface LoginEmailPhoneContract : LoginContract {

    interface View : LoginContract.View {
        fun onErrorValidateRegister(throwable: Throwable)
        fun onErrorEmptyEmailPhone()
        fun goToLoginPhoneVerifyPage(phoneNumber: String)
        fun onEmailExist(email: String)
        fun showNotRegisteredEmailDialog(email: String)
    }

    interface Presenter : LoginContract.Presenter {
        fun discoverLogin(context: Context)

        fun checkLoginEmailPhone(emailPhone: String)

        fun loginFacebook(context: Context, accessToken: AccessToken, email: String)
    }
}