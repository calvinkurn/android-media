package com.tokopedia.updateinactivephone.view

import com.tokopedia.abstraction.base.view.listener.CustomerView

class ChangeInactivePhone {

    interface View : CustomerView {

        fun showErrorPhoneNumber(resId: Int)

        fun showErrorPhoneNumber(errorMessage: String)

        fun dismissLoading()

        fun showLoading()

        fun onForbidden()

        fun onPhoneStatusSuccess(userId: String)

        fun onPhoneRegisteredWithEmail()

        fun onPhoneDuplicateRequest()

        fun onPhoneServerError()

        fun onPhoneBlackListed()

        fun onPhoneInvalid()

        fun onPhoneNotRegistered()

        fun onPhoneTooShort()

        fun onPhoneTooLong()

    }
}
