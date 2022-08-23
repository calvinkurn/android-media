package com.tokopedia.updateinactivephone.features.inputoldphonenumber.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.R.string.*
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.model.PhoneFormState
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.updateinactivephone.domain.usecase.InputOldPhoneNumberUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InputOldPhoneNumberViewModel @Inject constructor(
    private val inputOldPhoneNumberUseCase: InputOldPhoneNumberUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _formState = MutableLiveData<PhoneFormState>()
    val formState: LiveData<PhoneFormState> get() = _formState
    private val phoneFormState = PhoneFormState()

    private val _statusPhoneNumber = MutableLiveData<Result<String>>()
    val statusPhoneNumber: LiveData<Result<String>> get() = _statusPhoneNumber

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private fun validationNumber(number: String): Boolean {
        when {
            number.isEmpty() -> {
                phoneFormState.apply {
                    numberError = ipn_required
                    isDataValid = false
                }
            }
            number.length < MINIMUM_LENGTH_NUMBER -> {
                phoneFormState.apply {
                    numberError = ipn_too_short
                    isDataValid = false
                }
            }
            number.length > MAXIMUM_LENGTH_NUMBER -> {
                phoneFormState.apply {
                    numberError = ipn_too_long
                    isDataValid = false
                }
            }
            else -> {
                phoneFormState.apply {
                    isDataValid = true
                }
            }
        }

        _formState.value = phoneFormState

        return _formState.value?.isDataValid == true
    }

    fun submitNumber(number: String) {
        if (_isLoading.value == false && validationNumber(number)) {
            _isLoading.value = true
            postNumber(number)
        }
    }

    private fun postNumber(number: String) {
        launchCatchError(coroutineContext, {
            val response = inputOldPhoneNumberUseCase(number)

            when {
                response.data.status == STATUS_USER_ACTIVE ->
                    _statusPhoneNumber.value = Success(number)
                response.data.errors.isNotEmpty() ->
                    _statusPhoneNumber.value = Fail(MessageErrorException(response.data.errors[0]))
                else ->
                    _formState.value = phoneFormState.apply {
                        numberError = ipn_number_not_registered
                        isDataValid = false
                    }
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
        const val STATUS_USER_ACTIVE = 1
    }

}