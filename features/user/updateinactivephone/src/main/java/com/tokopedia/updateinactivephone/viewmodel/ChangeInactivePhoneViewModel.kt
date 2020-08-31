package com.tokopedia.updateinactivephone.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.data.model.response.GqlCheckPhoneStatusResponse
import com.tokopedia.updateinactivephone.data.model.response.ValidateInactivePhone
import com.tokopedia.updateinactivephone.usecase.GetPhoneNumberStatusUsecase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class ChangeInactivePhoneViewModel @Inject constructor(
        private val getPhoneNumberStatusUsecase: GetPhoneNumberStatusUsecase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val mutableChangeInactivePhoneResponse = MutableLiveData<Result<ValidateInactivePhone>>()
    val changeInactiveFormRequestResponse: LiveData<Result<ValidateInactivePhone>>
        get() = mutableChangeInactivePhoneResponse

    fun checkPhoneNumberStatus(phone: String) {
        getPhoneNumberStatusUsecase.getPhoneNumberStatus(onSuccessGetStatus(), onErrorGetStatus(), phone)
    }

    fun isValidPhoneNumber(phoneNumber: String): Int {
        var isValid = 0
        val check: Boolean
        val pattern: Pattern = Pattern.compile(PHONE_MATCHER)
        val matcher: Matcher = pattern.matcher(phoneNumber)
        check = matcher.matches()

        if (TextUtils.isEmpty(phoneNumber)) {
            isValid = R.string.phone_field_empty
        } else if (check && phoneNumber.length < 8) {
            isValid = R.string.phone_number_invalid_min_8
        } else if (check && phoneNumber.length > 15) {
            isValid = R.string.phone_number_invalid_max_15
        } else if (!check) {
            isValid = R.string.invalid_phone_number
        }
        return isValid
    }

    private fun onErrorGetStatus(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableChangeInactivePhoneResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetStatus(): (GqlCheckPhoneStatusResponse) -> Unit {
        return { mutableChangeInactivePhoneResponse.value = Success(it.validateInactivePhone) }
    }

    override fun onCleared() {
        super.onCleared()
        getPhoneNumberStatusUsecase.cancelJobs()
    }

    companion object {
        const val PHONE_MATCHER = "^(\\+)?+[0-9]*$"
    }
}