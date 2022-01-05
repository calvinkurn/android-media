package com.tokopedia.profilecompletion.addphone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.addphone.data.AddPhoneResult
import com.tokopedia.profilecompletion.addphone.data.UserValidatePojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_PHONE
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddPhoneViewModel @Inject constructor(
        private val addPhoneGraphQlUseCase: GraphqlUseCase<AddPhonePojo>,
        private val userValidateGraphQlUseCase: GraphqlUseCase<UserValidatePojo>,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.main) {

    private val _addPhoneResponse = MutableLiveData<Result<AddPhoneResult>>()
    val addPhoneResponse: LiveData<Result<AddPhoneResult>>
        get() = _addPhoneResponse

    private val _userValidateResponse = MutableLiveData<Result<UserValidatePojo>>()
    val userValidateResponse: LiveData<Result<UserValidatePojo>>
        get() = _userValidateResponse

    fun mutateAddPhone(msisdn: String) {
        rawQueries[ProfileCompletionQueryConstant.MUTATION_ADD_PHONE]?.let { query ->
            val params = mapOf(
                    PARAM_PHONE to msisdn)

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
            val errorMessage = it.data.errors
            val isSuccess = it.data.isSuccess

            when {
                isSuccess == 1 -> {
                    _addPhoneResponse.value = Success(AddPhoneResult(it, msisdn))
                }
                errorMessage.isNotEmpty() && errorMessage[0].isNotEmpty() -> {
                    _addPhoneResponse.postValue(Fail(MessageErrorException(errorMessage[0])))
                }
                else -> _addPhoneResponse.postValue(Fail(RuntimeException()))
            }
        }
    }

    fun userProfileValidate(msisdn: String) {
        rawQueries[ProfileCompletionQueryConstant.MUTATION_USER_VALIDATE]?.let { query ->
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
            val errorMessage = it.userProfileValidate.message
            val isValid = it.userProfileValidate.isValid

            when {
                isValid -> _userValidateResponse.value = Success(it)
                errorMessage.isNotEmpty() -> _userValidateResponse.postValue(
                        Fail(MessageErrorException(errorMessage)))
                else -> _userValidateResponse.postValue(Fail(RuntimeException()))
            }
        }
    }
}