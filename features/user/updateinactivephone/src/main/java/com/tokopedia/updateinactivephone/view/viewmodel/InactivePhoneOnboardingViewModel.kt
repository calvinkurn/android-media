package com.tokopedia.updateinactivephone.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_INVALID_PHONE_NUMBER
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_PHONE_NUMBER_MAX
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_PHONE_NUMBER_MIN
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.domain.usecase.PhoneValidationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class InactivePhoneOnboardingViewModel @Inject constructor(
        private val phoneValidationUseCase: PhoneValidationUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _phoneValidation = MutableLiveData<Result<PhoneValidationDataModel>>()
    val phoneValidation: LiveData<Result<PhoneValidationDataModel>>
        get() = _phoneValidation

    fun phoneValidation(phone: String, email: String) {
        launchCatchError(coroutineContext, {

            isValidPhoneNumber(phone)

            phoneValidationUseCase.setParam(phone, email)
            phoneValidationUseCase.execute(onSuccess = {
                if (it.validation.isSuccess) {
                    _phoneValidation.postValue(Success(it))
                } else {
                    _phoneValidation.postValue(Fail(Throwable(it.validation.error)))
                }
            }, onError = {
                _phoneValidation.postValue(Fail(it))
            })
        }, {
            _phoneValidation.postValue(Fail(it))
        })
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        if (phone.isEmpty() || phone.isBlank() || phone == "0") return true

        val isValid = phone.matches(PHONE_MATCHER)
        if (!isValid) {
            _phoneValidation.postValue(Fail(Throwable(ERROR_INVALID_PHONE_NUMBER)))
            return false
        } else if (isValid && phone.length < 9) {
            _phoneValidation.postValue(Fail(Throwable(ERROR_PHONE_NUMBER_MIN)))
            return false
        } else if (isValid && phone.length > 15) {
            _phoneValidation.postValue(Fail(Throwable(ERROR_PHONE_NUMBER_MAX)))
            return false
        }

        return true
    }

    public override fun onCleared() {
        super.onCleared()
        phoneValidationUseCase.cancelJob()
    }

    companion object {
        private val PHONE_MATCHER = "^(\\+)?+[0-9]*$".toRegex()
    }
}