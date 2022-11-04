package com.tokopedia.profilecompletion.addphone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.common.isPhoneExceedMaximumLength
import com.tokopedia.profilecompletion.addphone.common.isPhoneTooShortLength
import com.tokopedia.profilecompletion.addphone.data.UserProfileUpdatePhone
import com.tokopedia.profilecompletion.addphone.domain.UserProfileUpdateUseCase
import com.tokopedia.profilecompletion.addphone.domain.UserProfileValidateUseCase
import com.tokopedia.profilecompletion.addphone.domain.param.UserProfileUpdateParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class NewAddPhoneViewModel @Inject constructor(
    private val userProfileValidateUseCase: UserProfileValidateUseCase,
    private val userProfileUpdateUseCase: UserProfileUpdateUseCase,
    private val userSessionInterface: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private var phoneError = INITIAL_RESOURCE

    private val _formState = SingleLiveEvent<Int>()
    val formState: LiveData<Int> get() = _formState

    private val _userValidateLoading = MutableLiveData<Boolean>()
    val userValidateLoading: LiveData<Boolean> get() = _userValidateLoading

    private val _userProfileValidate = SingleLiveEvent<Result<Unit>>()
    val userProfileValidate: LiveData<Result<Unit>> get() = _userProfileValidate

    private val _userUpdateLoading = MutableLiveData<Boolean>()
    val userUpdateLoading: LiveData<Boolean> get() = _userUpdateLoading

    private val _userPhoneUpdate = SingleLiveEvent<Result<Pair<String, UserProfileUpdatePhone>>>()
    val userPhoneUpdate: LiveData<Result<Pair<String, UserProfileUpdatePhone>>> get() = _userPhoneUpdate

    fun validatePhone(phone: String) {
        phoneError = when {
            phone.isEmpty() -> {
                R.string.new_add_phone_must_be_filled
            }
            isPhoneTooShortLength(phone) -> {
                R.string.new_add_phone_min_length_error
            }
            isPhoneExceedMaximumLength(phone) -> {
                R.string.new_add_phone_max_length_error
            }
            else -> {
                EMPTY_RESOURCE
            }
        }

        _formState.value = phoneError
    }

    private fun isPhoneNumberValid(): Boolean {
        return phoneError == EMPTY_RESOURCE
    }

    fun submitForm(phone: String) {
        if (isPhoneNumberValid()) {
            userProfileValidate(phone)
        } else {
            validatePhone(phone)
        }
    }

    private fun userProfileValidate(phone: String) {
        _userValidateLoading.value = true
        launchCatchError(coroutineContext, {
            val response = userProfileValidateUseCase(phone).userProfileValidate

            _userValidateLoading.value = false
            _userProfileValidate.value = if (response.isValid) {
                Success(Unit)
            } else {
                Fail(MessageErrorException(response.message))
            }
        }, {
            _userValidateLoading.value = false
            _userProfileValidate.value = Fail(it)
        })
    }

    fun userProfileUpdate(phone: String, validateToken: String) {
        _userUpdateLoading.value = true
        val parameter = UserProfileUpdateParam(
            phone = phone,
            currentValidateToken = validateToken
        )

        launchCatchError(coroutineContext, {
            val response = userProfileUpdateUseCase(parameter).data

            _userUpdateLoading.value = false
            _userPhoneUpdate.value = if (response.errors.isEmpty()) {
                savePhoneSession(phone)
                Success(Pair(phone, response))
            } else {
                Fail(MessageErrorException(response.errors.first()))
            }
        }, {
            _userUpdateLoading.value = false
            _userPhoneUpdate.value = Fail(it)
        })
    }

    private fun savePhoneSession(phone: String) {
        userSessionInterface.apply {
            setIsMSISDNVerified(true)
            phoneNumber = phone
        }
    }

    companion object {
        const val EMPTY_RESOURCE = 0
        const val INITIAL_RESOURCE = -1
    }

}
