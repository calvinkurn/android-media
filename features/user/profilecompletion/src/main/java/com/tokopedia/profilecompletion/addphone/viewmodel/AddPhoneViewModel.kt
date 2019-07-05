package com.tokopedia.profilecompletion.addphone.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_MSISDN
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_OTP_CODE
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddPhoneViewModel @Inject constructor(private val graphqlUseCase: GraphqlUseCase<AddPhonePojo>,
                                            private val rawQueries: Map<String, String>,
                                            val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val mutateAddPhoneResponse = MutableLiveData<Result<AddPhonePojo>>()

    fun mutateAddPhone(msisdn: String, otp : String) {

        rawQueries[ProfileCompletionQueriesConstant.MUTATION_ADD_PHONE]?.let { query ->

            val params = mapOf(
                    PARAM_MSISDN to msisdn,
                    PARAM_OTP_CODE to otp)

            graphqlUseCase.setTypeClass(AddPhonePojo::class.java)
            graphqlUseCase.setRequestParams(params)
            graphqlUseCase.setGraphqlQuery(query)

            graphqlUseCase.execute(
                    onSuccessMutateAddPhone(),
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

    private fun onSuccessMutateAddPhone(): (AddPhonePojo) -> Unit {
       return {
           val errorMessage = it.data.userProfileCompletionUpdate.errorMessage
           val isSuccess = it.data.userProfileCompletionUpdate.isSuccess

           if (errorMessage.isBlank() && isSuccess) {
               mutateAddPhoneResponse.value = Success(it)
           } else if (!errorMessage.isBlank()) {
               mutateAddPhoneResponse.value = Fail(MessageErrorException(errorMessage,
                       ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
           } else {
               mutateAddPhoneResponse.value = Fail(RuntimeException())
           }
       }
    }
}