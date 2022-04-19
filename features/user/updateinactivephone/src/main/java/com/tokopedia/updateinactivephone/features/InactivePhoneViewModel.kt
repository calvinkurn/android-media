package com.tokopedia.updateinactivephone.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_INVALID_PHONE_NUMBER
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_PHONE_NUMBER_MAX
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_PHONE_NUMBER_MIN
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.domain.data.StatusInactivePhoneNumberDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetStatusInactivePhoneNumberUseCase
import com.tokopedia.updateinactivephone.domain.usecase.PhoneValidationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InactivePhoneViewModel @Inject constructor(
    private val phoneValidationUseCase: PhoneValidationUseCase,
    private val getStatusInactivePhoneNumberUseCase: GetStatusInactivePhoneNumberUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _phoneValidation = MutableLiveData<Result<PhoneValidationDataModel>>()
    val phoneValidation: LiveData<Result<PhoneValidationDataModel>>
        get() = _phoneValidation

    private val _getStatusPhoneNumber = MutableLiveData<Result<StatusInactivePhoneNumberDataModel>>()
    val getStatusPhoneNumber: LiveData<Result<StatusInactivePhoneNumberDataModel>>
        get() = _getStatusPhoneNumber

    fun userValidation(inactivePhoneUserDataModel: InactivePhoneUserDataModel) {
        launchCatchError(coroutineContext, {
            if (inactivePhoneUserDataModel.email.isEmpty() && !isValidPhoneNumber(inactivePhoneUserDataModel.oldPhoneNumber)) {
                return@launchCatchError
            }

            val response = phoneValidationUseCase(inactivePhoneUserDataModel)
            _phoneValidation.value = Success(response)
        }, {
            _phoneValidation.value = Fail(it)
        })
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        if (phone.isEmpty() || phone.isBlank() || phone == "0") return true

        val isValid = phone.matches(PHONE_MATCHER)
        if (!isValid) {
            _phoneValidation.value = Fail(Throwable(ERROR_INVALID_PHONE_NUMBER))
            return false
        } else if (isValid && phone.length < 9) {
            _phoneValidation.value = Fail(Throwable(ERROR_PHONE_NUMBER_MIN))
            return false
        } else if (isValid && phone.length > 15) {
            _phoneValidation.value = Fail(Throwable(ERROR_PHONE_NUMBER_MAX))
            return false
        }

        return true
    }

    fun getStatusPhoneNumber(inactivePhoneUserDataModel: InactivePhoneUserDataModel) {
        launchCatchError(coroutineContext, {
            val result = getStatusInactivePhoneNumberUseCase(inactivePhoneUserDataModel)
            _getStatusPhoneNumber.value = Success(result)
        }, {
            _getStatusPhoneNumber.value = Fail(Throwable(it))
        })
    }

    companion object {
        private val PHONE_MATCHER = "^(\\+)?+[0-9]*$".toRegex()
    }
}