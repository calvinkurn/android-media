package com.tokopedia.profilecompletion.addphone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addphone.data.AddPhoneResult
import com.tokopedia.profilecompletion.addphone.data.UserValidatePojo
import com.tokopedia.profilecompletion.addphone.domain.UserProfileUpdateUseCase
import com.tokopedia.profilecompletion.addphone.domain.UserProfileValidateUseCase
import com.tokopedia.profilecompletion.addphone.domain.param.UserProfileUpdateParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddPhoneViewModel @Inject constructor(
    private val userProfileUpdateUseCase: UserProfileUpdateUseCase,
    private val userProfileValidateUseCase: UserProfileValidateUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _addPhoneResponse = MutableLiveData<Result<AddPhoneResult>>()
    val addPhoneResponse: LiveData<Result<AddPhoneResult>>
        get() = _addPhoneResponse

    private val _userValidateResponse = MutableLiveData<Result<UserValidatePojo>>()
    val userValidateResponse: LiveData<Result<UserValidatePojo>>
        get() = _userValidateResponse

    fun mutateAddPhone(msisdn: String, validateToken: String) {
        val params = UserProfileUpdateParam(
            phone = msisdn,
            currentValidateToken = validateToken
        )

        launchCatchError(block = {
            val response = userProfileUpdateUseCase(params)

            val errorMessage = response.data.errors
            val isSuccess = response.data.isSuccess

            when {
                isSuccess == 1 -> {
                    _addPhoneResponse.value = Success(AddPhoneResult(response, msisdn))
                }
                errorMessage.isNotEmpty() && errorMessage[0].isNotEmpty() -> {
                    _addPhoneResponse.value = Fail(MessageErrorException(errorMessage[0]))
                }
                else -> _addPhoneResponse.value = Fail(RuntimeException())
            }
        }, onError = {
            _addPhoneResponse.value = Fail(it)
        })
    }

    fun userProfileValidate(msisdn: String) {
        launchCatchError(block = {
            val response = userProfileValidateUseCase(msisdn)

            val errorMessage = response.userProfileValidate.message
            val isValid = response.userProfileValidate.isValid

            when {
                isValid -> _userValidateResponse.value = Success(response)
                errorMessage.isNotEmpty() -> _userValidateResponse.value =
                    Fail(MessageErrorException(errorMessage))
                else -> _userValidateResponse.value = Fail(RuntimeException())
            }
        }, onError = {
            _userValidateResponse.value = Fail(it)
        })
    }
}
