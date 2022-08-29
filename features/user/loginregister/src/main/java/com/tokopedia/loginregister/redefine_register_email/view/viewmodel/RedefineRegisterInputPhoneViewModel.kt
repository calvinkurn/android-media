package com.tokopedia.loginregister.redefine_register_email.view.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.RegisterUtil
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class RedefineRegisterInputPhoneViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private var phoneError = RESOURCE_NOT_CHANGED
    private val _formState = SingleLiveEvent<Int>()
    val formState: LiveData<Int> get() = _formState
    private val _isRegisteredPhone = SingleLiveEvent<Boolean>()
    val isRegisteredPhone: LiveData<Boolean> get() = _isRegisteredPhone

    fun validatePhone(phone: String) {
        phoneError = when {
            phone.isEmpty() -> {
                R.string.error_field_required
            }
            RegisterUtil.isPhoneTooShortLength(phone) -> {
                R.string.register_email_input_phone_min_length_error
            }
            RegisterUtil.isPhoneExceedMaximumLength(phone) -> {
                R.string.register_email_input_phone_max_length_error
            }
            else -> {
                NOTHING_RESOURCE
            }
        }

        _formState.value = phoneError
    }

    private fun isPhoneNumberValid(): Boolean {
        return phoneError == NOTHING_RESOURCE
    }

    fun submitForm(phone: String) {
        if (isPhoneNumberValid()) {
            _isRegisteredPhone.value = true
        } else {
            validatePhone(phone)
        }
    }

    companion object {
        const val NOTHING_RESOURCE = 0
        const val RESOURCE_NOT_CHANGED = -1
    }

}