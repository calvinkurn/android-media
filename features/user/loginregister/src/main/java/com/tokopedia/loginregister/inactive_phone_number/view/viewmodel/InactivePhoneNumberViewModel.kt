package com.tokopedia.loginregister.inactive_phone_number.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.R.string.*
import com.tokopedia.loginregister.inactive_phone_number.data.StatusPhoneNumber
import com.tokopedia.loginregister.inactive_phone_number.domain.InactivePhoneNumberUseCase
import com.tokopedia.loginregister.inactive_phone_number.view.model.PhoneFormState
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InactivePhoneNumberViewModel @Inject constructor(
    private val inactivePhoneNumberUseCase: InactivePhoneNumberUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _formState = MutableLiveData<PhoneFormState>()
    val formState get() = _formState

    private val _statusPhoneNumber = MutableLiveData<Result<String>>()
    val statusPhoneNumber get() = _statusPhoneNumber

    var currentNumber: String = ""

    private val _isLoading = MutableLiveData(false)
    val isLoading get() = _isLoading

    private fun validationNumber(number: String): Boolean {
        _formState.value =
            when {
                number.isEmpty() -> PhoneFormState(numberError = ipn_required)
                number.length < MINIMUM_LENGTH_NUMBER -> PhoneFormState(numberError = ipn_too_short)
                number.length > MAXIMUM_LENGTH_NUMBER -> PhoneFormState(numberError = ipn_too_long)
                else -> PhoneFormState(isDataValid = true)
            }

        return _formState.value?.isDataValid == true
    }

    fun submitNumber(number: String) {
        if (_isLoading.value == false) {
            if (validationNumber(number)) {
                _isLoading.value = true
                currentNumber = number
                postNumber(number)
            }
        }
    }

    private fun postNumber(number: String) {
        launchCatchError(coroutineContext, {
            val response = inactivePhoneNumberUseCase(number)

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

    companion object {
        private const val MINIMUM_LENGTH_NUMBER = 9
        private const val MAXIMUM_LENGTH_NUMBER = 15
    }

}