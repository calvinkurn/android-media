package com.tokopedia.loginregister.invalid_phone_number.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.R.string.*
import com.tokopedia.loginregister.invalid_phone_number.domain.InvalidPhoneNumberUseCaseContract
import com.tokopedia.loginregister.invalid_phone_number.view.model.PhoneFormState
import com.tokopedia.loginregister.invalid_phone_number.view.model.StatusPhoneNumberResult
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InvalidPhoneNumberViewModel @Inject constructor(
    private val invalidPhoneNumberUseCase: InvalidPhoneNumberUseCaseContract,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _formState = MutableLiveData<PhoneFormState>()
    val formState get() = _formState

    private val _statusPhoneNumber = MutableLiveData<Result<StatusPhoneNumberResult>>()
    val statusPhoneNumber get() = _statusPhoneNumber

    private fun validationNumber(number: String): Boolean {
        _formState.value =
            when {
                number.isBlank() -> PhoneFormState(numberError = ipn_required)
                number.length < 9 -> PhoneFormState(numberError = ipn_too_short)
                number.length > 15 -> PhoneFormState(numberError = ipn_too_long)
                else -> PhoneFormState(isDataValid = true)
            }

        return _formState.value?.isDataValid == true
    }

    fun submitNumber(number: String){
        if (validationNumber(number)) postNumber(number)
    }

    private fun postNumber(number: String) {
        launchCatchError(coroutineContext, {
            val response = invalidPhoneNumberUseCase.getData(number)

            if (response.data.isExist)
                _statusPhoneNumber.value = Success(StatusPhoneNumberResult.SuccessHaveMultipleAccount())
            else
                _formState.value = PhoneFormState(numberError = ipn_number_not_registered)
        }, {
            _statusPhoneNumber.value = Fail(it)
        })
    }

    companion object{
        const val TAG = "MainViewModel"
    }

}