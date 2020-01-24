package com.tokopedia.profilecompletion.addphone.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.addphone.data.AddPhoneResult
import com.tokopedia.profilecompletion.addphone.data.UserValidatePojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_MSISDN
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_OTP_CODE
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_PHONE
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddPhoneViewModel @Inject constructor(
        private val addPhoneGraphQlUseCase: GraphqlUseCase<AddPhonePojo>,
        private val userValidateGraphQlUseCase: GraphqlUseCase<UserValidatePojo>,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    private val _addPhoneResponse = MutableLiveData<Result<AddPhoneResult>>()
    val addPhoneResponse: LiveData<Result<AddPhoneResult>>
        get() = _addPhoneResponse

    private val _userValidateResponse = MutableLiveData<Result<UserValidatePojo>>()
    val userValidateResponse: LiveData<Result<UserValidatePojo>>
        get() = _userValidateResponse

    fun mutateAddPhone(context: Context, msisdn: String, otp: String) {
        GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_phone)?.let { query ->

            val params = mapOf(
                    PARAM_MSISDN to msisdn,
                    PARAM_OTP_CODE to otp)

            addPhoneGraphQlUseCase.setTypeClass(AddPhonePojo::class.java)
            addPhoneGraphQlUseCase.setRequestParams(params)
            addPhoneGraphQlUseCase.setGraphqlQuery(query)

            addPhoneGraphQlUseCase.execute(
                    onSuccessMutateAddPhone(msisdn),
                    onErrorMutateAddPhone()
            )
        }
    }

    private fun onErrorMutateAddPhone(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _addPhoneResponse.value = Fail(it)
        }
    }

    private fun onSuccessMutateAddPhone(msisdn: String): (AddPhonePojo) -> Unit {
        return {
            val errorMessage = it.data.errorMessage
            val isSuccess = it.data.isSuccess

            if (errorMessage.isBlank() && isSuccess) {
                _addPhoneResponse.value = Success(AddPhoneResult(it, msisdn))
            } else if (!errorMessage.isBlank()) {
                _addPhoneResponse.value = Fail(MessageErrorException(errorMessage))
            } else {
                _addPhoneResponse.value = Fail(RuntimeException())
            }
        }
    }

    fun userProfileCompletionValidate(context: Context, msisdn: String) {
        GraphqlHelper.loadRawString(context.resources, R.raw.mutation_user_profile_completion_validate)?.let { query ->

            val params = mapOf(PARAM_PHONE to msisdn)

            userValidateGraphQlUseCase.setTypeClass(UserValidatePojo::class.java)
            userValidateGraphQlUseCase.setRequestParams(params)
            userValidateGraphQlUseCase.setGraphqlQuery(query)

            userValidateGraphQlUseCase.execute(
                    onSuccessUserValidatePojo(),
                    onErrorUserValidatePojo()
            )
        }
    }

    private fun onErrorUserValidatePojo(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _userValidateResponse.postValue(Fail(it))
        }
    }

    private fun onSuccessUserValidatePojo(): (UserValidatePojo) -> Unit {
        return {
            val errorMessage = it.userProfileCompletionValidate.msisdnMessage
            val isValid = it.userProfileCompletionValidate.isValid

            if (isValid) {
                _userValidateResponse.postValue(Success(it))
            } else if (!errorMessage.isBlank()) {
                _userValidateResponse.postValue(Fail(MessageErrorException(errorMessage)))
            } else {
                _userValidateResponse.postValue(Fail(RuntimeException()))
            }
        }
    }
}