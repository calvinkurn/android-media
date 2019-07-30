package com.tokopedia.profilecompletion.addemail.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.profilecompletion.addemail.data.AddEmailPojo
import com.tokopedia.profilecompletion.addemail.data.AddEmailResult
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_EMAIL
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_OTP_CODE
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddEmailViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                            private val rawQueries: Map<String, String>,
                                            private val graphqlUseCase: GraphqlUseCase<AddEmailPojo>)
    : BaseViewModel(dispatcher) {

    val mutateAddEmailResponse = MutableLiveData<Result<AddEmailResult>>()

    fun mutateAddEmail(email: String, otpCode: String) {
        rawQueries[ProfileCompletionQueriesConstant.MUTATION_ADD_EMAIL]?.let { query ->

            val params = mapOf(PARAM_EMAIL to email,
                    PARAM_OTP_CODE to otpCode)

            graphqlUseCase.setTypeClass(AddEmailPojo::class.java)
            graphqlUseCase.setRequestParams(params)
            graphqlUseCase.setGraphqlQuery(query)

            graphqlUseCase.execute(
                    onSuccessMutateAddEmail(email),
                    onErrorMutateAddEmail()
            )
        }

    }

    private fun onErrorMutateAddEmail(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutateAddEmailResponse.value = Fail(it)
        }
    }

    private fun onSuccessMutateAddEmail(email: String): (AddEmailPojo) -> Unit {
        return {
            val errorMessage = it.data.errorMessage
            val isSuccess = it.data.isSuccess

            if (errorMessage.isBlank() && isSuccess) {
                mutateAddEmailResponse.value = Success(AddEmailResult(it, email))
            } else if (!errorMessage.isBlank()) {
                mutateAddEmailResponse.value = Fail(MessageErrorException(errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
            } else {
                mutateAddEmailResponse.value = Fail(RuntimeException())
            }
        }
    }
}