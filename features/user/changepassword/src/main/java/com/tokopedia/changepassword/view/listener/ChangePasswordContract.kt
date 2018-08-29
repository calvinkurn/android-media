package com.tokopedia.changepassword.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by nisie on 7/25/18.
 */

interface ChangePasswordContract {

    interface View : CustomerView {
        fun getContext(): Context?

        fun showLoading()

        fun hideLoading()

        fun onSuccessChangePassword()

        fun onErrorChangePassword(errorMessage: String)

        fun onErrorLogout(errorMessage: String)

        fun onErrorOldPass(errorMessage: String?)

        fun onErrorNewPass(errorMessage: String?)

        fun onErrorConfirmPass(errorMessage: String?)


    }

    interface Presenter : CustomerPresenter<View> {

        fun submitChangePasswordForm(oldPassword: String,
                                     newPassword: String,
                                     confirmPassword: String)

        fun isValidForm(oldPassword: String, newPassword: String, confirmPassword: String): Boolean

    }
}