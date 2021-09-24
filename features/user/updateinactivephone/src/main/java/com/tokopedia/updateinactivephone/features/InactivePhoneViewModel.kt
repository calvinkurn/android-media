package com.tokopedia.updateinactivephone.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_INVALID_PHONE_NUMBER
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_PHONE_NUMBER_MAX
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_PHONE_NUMBER_MIN
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.domain.data.StatusInactivePhoneNumberDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetStatusInactivePhoneNumberUseCase
import com.tokopedia.updateinactivephone.domain.usecase.PhoneValidationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InactivePhoneViewModel @Inject constructor(
    private val phoneValidationUseCase: PhoneValidationUseCase,
    private val getStatusInactivePhoneNumberUseCase: GetStatusInactivePhoneNumberUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _phoneValidation = MutableLiveData<Result<PhoneValidationDataModel>>()
    val phoneValidation: LiveData<Result<PhoneValidationDataModel>>
        get() = _phoneValidation

    private val _getStatusPhoneNumber = MutableLiveData<Result<StatusInactivePhoneNumberDataModel>>()
    val getStatusPhoneNumber: LiveData<Result<StatusInactivePhoneNumberDataModel>>
        get() = _getStatusPhoneNumber

    fun userValidation(phone: String, email: String) {
        launchCatchError(coroutineContext, {
            if (email.isEmpty() && !isValidPhoneNumber(phone)) {
                return@launchCatchError
            }

            val response = phoneValidationUseCase(mapOf(
                PhoneValidationUseCase.PARAM_PHONE to phone,
                PhoneValidationUseCase.PARAM_EMAIL to email
            ))

            withContext(dispatcher.main) {
                _phoneValidation.postValue(Success(response))
            }
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

    fun getStatusPhoneNumber(email: String, phone: String, index: Int) {
        launchCatchError(coroutineContext, {
            val result = getStatusInactivePhoneNumberUseCase(mapOf(
                GetStatusInactivePhoneNumberUseCase.PARAM_EMAIL to email,
                GetStatusInactivePhoneNumberUseCase.PARAM_PHONE to phone,
                GetStatusInactivePhoneNumberUseCase.PARAM_USER_INDEX to index,
            ))

            withContext(dispatcher.main) {
                _getStatusPhoneNumber.postValue(Success(result))
            }
        }, {
            _getStatusPhoneNumber.postValue(Fail(Throwable(it)))
        })
    }

    companion object {
        private val PHONE_MATCHER = "^(\\+)?+[0-9]*$".toRegex()
    }
}