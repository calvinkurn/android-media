package com.tokopedia.updateinactivephone.viewmodel.presenter

import android.text.TextUtils

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.core.base.domain.RequestParams
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.ID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.PARAM_BANK_BOOK_IMAGE_PATH
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.PARAM_KTP_IMAGE_PATH
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.RESOLUTION
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.SERVER_ID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.TOKEN
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.USERID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.EMAIL
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.USER_ID
import com.tokopedia.updateinactivephone.viewmodel.presenter.ChangeInactivePhonePresenter.Companion.PHONE_MATCHER
import com.tokopedia.updateinactivephone.viewmodel.subscriber.UpdatePhoneNumberSubscriber
import com.tokopedia.updateinactivephone.viewmodel.subscriber.ValidateUserDataSubscriber
import com.tokopedia.updateinactivephone.usecase.UploadChangePhoneNumberRequestUseCase
import com.tokopedia.updateinactivephone.usecase.ValidateUserDataUseCase
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest

import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.inject.Inject

class ChangeInactiveFormRequestPresenter @Inject constructor(
        private val validateUserDataUseCase: ValidateUserDataUseCase,
        private val uploadChangePhoneNumberRequestUseCase: UploadChangePhoneNumberRequestUseCase)
    : BaseDaggerPresenter<ChangeInactiveFormRequest.View>(), ChangeInactiveFormRequest.Presenter {

    private var photoIdImagePath: String? = null
    private var accountImagePath: String? = null

    val isValidPhotoIdPath: Boolean
        get() = !TextUtils.isEmpty(photoIdImagePath)


    fun setPhotoIdImagePath(imagePath: String) {
        this.photoIdImagePath = imagePath
    }

    override fun uploadPhotoIdImage(email: String, phone: String, userId: String) {
        val updatePhoneNumberSubscriber = UpdatePhoneNumberSubscriber(view)
        if (!TextUtils.isEmpty(photoIdImagePath)) {
            uploadChangePhoneNumberRequestUseCase.execute(getUploadChangePhoneNumberRequestParam(email, phone, userId),
                    updatePhoneNumberSubscriber)
        }
    }

    fun setAccountPhotoImagePath(imagePath: String) {
        this.accountImagePath = imagePath
    }

    override fun validateUserData(email: String, phone: String, userId: String) {
        val validEmail = isValidEmail(email)
        val validPhone = isValidPhone(phone)

        if (validPhone && validEmail) {
            view.showLoading()
            validateUserDataUseCase.execute(phone, email, userId,
                    ValidateUserDataSubscriber(view))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        var isValid = true

        if (TextUtils.isEmpty(email)) {
            view.showErrorPhoneNumber(R.string.email_field_empty)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false
            view.showErrorEmail(R.string.invalid_email_error)
        }

        return isValid
    }

    private fun getUploadChangePhoneNumberRequestParam(email: String, phone: String, userId: String): RequestParams {
        val params = RequestParams.create()
        params.putString(USERID, userId)
        params.putString(ID, userId)
        params.putInt(SERVER_ID, 49)
        params.putInt(RESOLUTION, 215)
        params.putString(PHONE, phone)
        params.putString(EMAIL, email)
        params.putInt(USER_ID, if (userId.isEmpty()) 0 else Integer.valueOf(userId))
        params.putString(TOKEN, "tokopedia-lite-inactive-phone")

        setParamUploadIdImage(params)

        if (!TextUtils.isEmpty(accountImagePath)) {
            setParamUploadBankBookImage(params)
        }
        return params
    }

    private fun setParamUploadBankBookImage(params: RequestParams) {
        params.putString(PARAM_BANK_BOOK_IMAGE_PATH, accountImagePath)

    }

    private fun setParamUploadIdImage(params: RequestParams) {
        params.putString(PARAM_KTP_IMAGE_PATH, photoIdImagePath)
    }

    private fun isValidPhone(phoneNumber: String): Boolean {
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

    override fun detachView() {
        super.detachView()
        validateUserDataUseCase.unsubscribe()
        uploadChangePhoneNumberRequestUseCase.unsubscribe()
    }
}
