package com.tokopedia.profilecompletion.addphone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.addphone.data.AddPhoneResult
import com.tokopedia.profilecompletion.addphone.data.CheckPhonePojo
import com.tokopedia.profilecompletion.addphone.data.UserValidatePojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_MSISDN
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_OTP_CODE
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_PHONE
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddPhoneViewModel @Inject constructor(private val addPhoneGraphQlUseCase: GraphqlUseCase<AddPhonePojo>,
                                            private val checkMsisdnGraphQlUseCase: GraphqlUseCase<CheckPhonePojo>,
                                            private val userValidateGraphQlUseCase: GraphqlUseCase<UserValidatePojo>,
                                            val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val mutateAddPhoneResponse = MutableLiveData<Result<AddPhoneResult>>()
    val mutateCheckPhoneResponse = MutableLiveData<Result<CheckPhonePojo>>()
    private val mutableUserValidateResponse = MutableLiveData<Result<UserValidatePojo>>()
    val userValidateResponse: LiveData<Result<UserValidatePojo>>
        get() = mutableUserValidateResponse


    fun mutateAddPhone(context: Context, msisdn: String, otp : String) {
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
            mutateAddPhoneResponse.value = Fail(it)
        }
    }

    private fun onSuccessMutateAddPhone(msisdn: String): (AddPhonePojo) -> Unit {
       return {
           val errorMessage = it.data.errorMessage
           val isSuccess = it.data.isSuccess

           if (errorMessage.isBlank() && isSuccess) {
               mutateAddPhoneResponse.value = Success(AddPhoneResult(it, msisdn))
           } else if (!errorMessage.isBlank()) {
               mutateAddPhoneResponse.value = Fail(MessageErrorException(errorMessage,
                       ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
           } else {
               mutateAddPhoneResponse.value = Fail(RuntimeException())
           }
       }
    }

    fun mutateCheckPhone(context: Context, msisdn: String) {

        GraphqlHelper.loadRawString(context.resources, R.raw.mutation_check_phone)?.let { query ->

            val params = mapOf(PARAM_PHONE to msisdn)

            checkMsisdnGraphQlUseCase.setTypeClass(CheckPhonePojo::class.java)
            checkMsisdnGraphQlUseCase.setRequestParams(params)
            checkMsisdnGraphQlUseCase.setGraphqlQuery(query)

            checkMsisdnGraphQlUseCase.execute(
                    onSuccessCheckPhonePojo(),
                    onErrorCheckPhonePojo()
            )
        }
    }

    private fun onErrorCheckPhonePojo(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutateCheckPhoneResponse.value = Fail(it)
        }
    }

    private fun onSuccessCheckPhonePojo(): (CheckPhonePojo) -> Unit {
        return {
            val errorMessage = it.checkMsisdn.errorMessage

            when {
                errorMessage.isEmpty() -> mutateCheckPhoneResponse.value = Success(it)
                errorMessage.isNotEmpty() -> mutateCheckPhoneResponse.value = Fail(MessageErrorException(errorMessage[0],
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
                else -> mutateCheckPhoneResponse.value = Fail(RuntimeException())
            }
        }
    }

    fun userProfileCompletionValidate(context: Context, msisdn: String){
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
            mutableUserValidateResponse.postValue(Fail(it))
        }
    }

    private fun onSuccessUserValidatePojo(): (UserValidatePojo) -> Unit {
        return {
            val errorMessage = it.userProfileCompletionValidate.msisdnMessage
            val isValid = it.userProfileCompletionValidate.isValid

            if (errorMessage.isBlank() && isValid) {
                mutableUserValidateResponse.postValue(Success(it))
            } else if (!errorMessage.isBlank()) {
                mutableUserValidateResponse.postValue(Fail(MessageErrorException(errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString())))
            } else {
                mutableUserValidateResponse.postValue(Fail(RuntimeException()))
            }
        }
    }
}