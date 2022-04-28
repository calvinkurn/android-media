package com.tokopedia.loginregister.inactive_phone_number.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.R.string.*
import com.tokopedia.loginregister.inactive_phone_number.data.StatusPhoneNumber
import com.tokopedia.loginregister.inactive_phone_number.domain.InactivePhoneNumberUseCaseContract
import com.tokopedia.loginregister.inactive_phone_number.view.model.PhoneFormState
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InactivePhoneNumberViewModel @Inject constructor(
    private val inactivePhoneNumberUseCase: InactivePhoneNumberUseCaseContract,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _formState = MutableLiveData<PhoneFormState>()
    val formState get() = _formState

    private val _statusPhoneNumber = MutableLiveData<Result<String>>()
    val statusPhoneNumber get() = _statusPhoneNumber

    var currentNumber: String = ""

    private val  _isLoading = MutableLiveData(false)
    val isLoading get() = _isLoading

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
        if (_isLoading.value == false){
            _isLoading.value = true
            currentNumber = number
            if (validationNumber(number)) postNumber(number)
        }
    }

    private fun postNumber(number: String) {
        launchCatchError(coroutineContext, {
            val response = inactivePhoneNumberUseCase.getData(number)

            when {
                response.data.status == StatusPhoneNumber.USER_ACTIVE.value ->
                    _statusPhoneNumber.value = Success(currentNumber)
                response.data.errors.isNotEmpty() ->
                    _statusPhoneNumber.value = Fail(MessageErrorException(response.data.errors[0]))
                else ->
                    _formState.value = PhoneFormState(numberError = ipn_number_not_registered)
            }

            _isLoading.value = false
        }, {
            _statusPhoneNumber.value = Fail(it)
            _isLoading.value = false
        })
    }

}