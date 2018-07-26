package com.tokopedia.changepassword.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.changepassword.domain.ChangePasswordUseCase
import com.tokopedia.changepassword.domain.model.ChangePasswordDomain
import com.tokopedia.changepassword.view.listener.ChangePasswordContract
import com.tokopedia.user.session.UserSession
import rx.Subscriber

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordPresenter(val changePasswordUseCase: ChangePasswordUseCase,
                              val userSession: UserSession) :
        ChangePasswordContract.Presenter,
        BaseDaggerPresenter<ChangePasswordContract.View>() {

    val PASSWORD_MINIMUM_LENGTH: Int = 6

    override fun submitChangePasswordForm(oldPassword: String,
                                          newPassword: String,
                                          confirmPassword: String) {
        changePasswordUseCase.execute(ChangePasswordUseCase.getParam(
                oldPassword,
                newPassword,
                confirmPassword
        ), object : Subscriber<ChangePasswordDomain>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.onErrorChangePassword(e.toString())
            }

            override fun onNext(changePasswordDomain: ChangePasswordDomain) {
                if (changePasswordDomain.is_success) {
                    view.onSuccessChangePassword()
                } else {
                    view.onErrorChangePassword("")
                }
            }
        })
    }

    override fun isValidForm(oldPassword: String,
                             newPassword: String,
                             confirmPassword: String): Boolean {
        var isValid = true

        if (oldPassword.isBlank()) {
            isValid = false
        }

        if (newPassword.isBlank() || newPassword.length < PASSWORD_MINIMUM_LENGTH) {
            isValid = false
        }

        if (confirmPassword.isBlank() || !confirmPassword.equals(newPassword)) {
            isValid = false
        }

        return isValid
    }

    override fun detachView() {
        super.detachView()
        changePasswordUseCase.unsubscribe()
    }

    fun logoutToHomePage() {

    }
}