package com.tokopedia.updateinactivephone.revamp.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.data.model.response.UploadImageData
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.ERROR_EMPTY_PHONE
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.ERROR_FAILED_UPLOAD_IMAGE
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.ERROR_INVALID_PHONE_NUMBER
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.ERROR_PHONE_NUMBER_MAX
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.ERROR_PHONE_NUMBER_MIN
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.ERROR_VALIDATE_PHONE_NUMBER
import com.tokopedia.updateinactivephone.revamp.domain.data.ImageUploadDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.revamp.domain.usecase.ImageUploadUseCase
import com.tokopedia.updateinactivephone.revamp.domain.usecase.PhoneValidationUseCase
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

    fun phoneValidation(phone: String) {
        if (!isValidPhoneNumber(phone)) {
            return
        }

        launchCatchError(coroutineContext, {
            phoneValidationUseCase.setParam(phone)
            phoneValidationUseCase.execute(onSuccess = {
                _phoneValidation.postValue(Success(it))
            }, onError = {
                _phoneValidation.postValue(Fail(it))
            })
        }, {
            _phoneValidation.postValue(Fail(it))
        })
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val isValid = phone.matches(PHONE_MATCHER)
        if (phone.isEmpty()) {
            _phoneValidation.postValue(Fail(Throwable(ERROR_EMPTY_PHONE)))
            return false
        } else if (!isValid) {
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

    override fun onCleared() {
        super.onCleared()
        phoneValidationUseCase.cancelJob()
    }

    companion object {
        private val PHONE_MATCHER = "^(\\+)?+[0-9]*$".toRegex()
    }
}