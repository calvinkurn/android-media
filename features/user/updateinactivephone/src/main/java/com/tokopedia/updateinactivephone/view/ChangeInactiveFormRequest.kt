package com.tokopedia.updateinactivephone.view

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

class ChangeInactiveFormRequest {

    interface View : CustomerView {

        fun dismissLoading()

        fun showLoading()

        fun onForbidden()

        fun showErrorValidateData(errorMessage: String)

        fun onUserDataValidated(userId: String)

        fun onPhoneTooShort()

        fun onPhoneTooLong()

        fun onPhoneBlackListed()

        fun onWrongUserIDInput()

        fun onPhoneDuplicateRequest()

        fun onPhoneServerError()

        fun onSameMsisdn()

        fun onAlreadyRegisteredMsisdn()

        fun onEmptyMsisdn()

        fun onInvalidPhone()

        fun onMaxReachedPhone()

        fun showErrorPhoneNumber(error_field_required: Int)

        fun showErrorEmail(error_invalid_email: Int)

        fun onEmailError()

        fun onUserNotRegistered()

        fun onInvalidFileUploaded()

        fun onUpdateDataRequestFailed()

        fun onUpdateDataRequestSuccess()
    }

    interface Presenter : CustomerPresenter<View> {

        fun uploadPhotoIdImage(email: String, phone: String, userId: String)
        fun validateUserData(email: String, phone: String, userId: String)
    }

}
