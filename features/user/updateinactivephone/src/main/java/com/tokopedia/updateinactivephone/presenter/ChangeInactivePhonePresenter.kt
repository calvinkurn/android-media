package com.tokopedia.updateinactivephone.presenter

import android.text.TextUtils

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.subscriber.CheckPhoneNumberStatusSubscriber
import com.tokopedia.updateinactivephone.usecase.CheckPhoneNumberStatusUsecase
import com.tokopedia.updateinactivephone.view.ChangeInactivePhone

import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.inject.Inject

class ChangeInactivePhonePresenter @Inject constructor(private val checkPhoneNumberStatusUsecase: CheckPhoneNumberStatusUsecase) : BaseDaggerPresenter<ChangeInactivePhone.View>(), ChangeInactivePhone.Presenter {

    override fun attachView(view: ChangeInactivePhone.View) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
        checkPhoneNumberStatusUsecase.unsubscribe()
    }

    private fun isValid(phoneNumber: String): Boolean {
        var isValid = true
        val check: Boolean
        val p: Pattern = Pattern.compile(PHONE_MATCHER)
        val m: Matcher
        m = p.matcher(phoneNumber)
        check = m.matches()

        if (TextUtils.isEmpty(phoneNumber)) {
            view.showErrorPhoneNumber(R.string.phone_field_empty)
            isValid = false
        } else if (check && phoneNumber.length < 8) {
            view.showErrorPhoneNumber(R.string.phone_number_invalid_min_8)
            isValid = false
        } else if (check && phoneNumber.length > 15) {
            view.showErrorPhoneNumber(R.string.phone_number_invalid_max_15)
            isValid = false
        } else if (!check) {
            view.showErrorPhoneNumber(R.string.invalid_phone_number)
            isValid = false
        }
        return isValid
    }

    override fun checkPhoneNumberStatus(phone: String) {
        if (isValid(phone)) {
            view.showLoading()
            checkPhoneNumberStatusUsecase.execute(phone,
                    CheckPhoneNumberStatusSubscriber(view))
        }
    }

    companion object {
        const val PHONE_MATCHER = "^(\\+)?+[0-9]*$"
    }

}