package com.tokopedia.changepassword.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.changepassword.domain.ChangePasswordUseCase
import com.tokopedia.changepassword.domain.model.ChangePasswordDomain
import com.tokopedia.changepassword.view.listener.ChangePasswordContract
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordPresenter(private val changePasswordUseCase: ChangePasswordUseCase,
                              val userSession: UserSessionInterface) :
        ChangePasswordContract.Presenter,
        BaseDaggerPresenter<ChangePasswordContract.View>() {

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
                val errorMessage = ErrorHandler.getErrorMessage(view.getContext(), e)

                when {
                    errorMessage.toLowerCase().contains("ulangi kata sandi") || errorMessage
                            .toLowerCase().contains("tidak cocok") -> view.onErrorConfirmPass(errorMessage)
                    errorMessage.toLowerCase().contains("kata sandi baru") -> view
                            .onErrorNewPass(errorMessage)
                    errorMessage.toLowerCase().contains("kata sandi lama") -> view
                            .onErrorOldPass(errorMessage)
                    else -> view.onErrorChangePassword(errorMessage)

                }
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
        return oldPassword.isNotBlank()
                && newPassword.isNotBlank()
                && confirmPassword.isNotBlank()
    }

    override fun detachView() {
        super.detachView()
        changePasswordUseCase.unsubscribe()
    }
}