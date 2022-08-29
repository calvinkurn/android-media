package com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.RegisterUtil
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.RegisterCheckUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class RedefineRegisterInputPhoneViewModel @Inject constructor(
    private val registerCheckUseCase: RegisterCheckUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private var phoneError = RESOURCE_NOT_CHANGED
    private val _formState = SingleLiveEvent<Int>()
    val formState: LiveData<Int> get() = _formState
    private val _isRegisteredPhone = SingleLiveEvent<RegisteredPhoneState>()
    val isRegisteredPhone: LiveData<RegisteredPhoneState> get() = _isRegisteredPhone

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
            registerCheck(phone)
        } else {
            validatePhone(phone)
        }
    }

    private fun registerCheck(phone: String) {
        _isRegisteredPhone.value = RegisteredPhoneState.Loading()
        launchCatchError(coroutineContext, {
            val response = registerCheckUseCase(phone)

            _isRegisteredPhone.value = when {
                response.data.errors.isNotEmpty() -> {
                    RegisteredPhoneState.Ineligible(message = response.data.errors[0])
                }
                response.data.isExist -> {
                    RegisteredPhoneState.Registered()
                }
                else -> {
                    RegisteredPhoneState.Unregistered(phoneNumber = response.data.view)
                }
            }

        }, {
            _isRegisteredPhone.value = RegisteredPhoneState.Failed(throwable = it)
        })
    }

    companion object {
        const val NOTHING_RESOURCE = 0
        const val RESOURCE_NOT_CHANGED = -1
        private const val STATUS_USER_ACTIVE = 1
    }

}