package com.tokopedia.profilecompletion.addphone.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.addphone.data.AddPhoneResult
import com.tokopedia.profilecompletion.addphone.data.CheckPhonePojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_MSISDN
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_OTP_CODE
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_PHONE
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddPhoneViewModel @Inject constructor(private val addPhoneGraphQlUseCase: GraphqlUseCase<AddPhonePojo>,
                                            private val checkMsisdnGraphQlUseCase: GraphqlUseCase<CheckPhonePojo>,
                                            private val rawQueries: Map<String, String>,
                                            val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val mutateAddPhoneResponse = MutableLiveData<Result<AddPhoneResult>>()
    val mutateCheckPhoneResponse = MutableLiveData<Result<CheckPhonePojo>>()


    fun mutateAddPhone(msisdn: String, otp : String) {

        rawQueries[ProfileCompletionQueriesConstant.MUTATION_ADD_PHONE]?.let { query ->

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

    fun mutateCheckPhone(msisdn: String) {

        rawQueries[ProfileCompletionQueriesConstant.MUTATION_CHECK_PHONE]?.let { query ->

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



}