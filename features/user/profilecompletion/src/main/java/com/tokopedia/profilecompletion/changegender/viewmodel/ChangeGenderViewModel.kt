package com.tokopedia.profilecompletion.changegender.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderPojo
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderResult
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_GENDER
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChangeGenderViewModel @Inject constructor(private val graphqlUseCase:  GraphqlUseCase<ChangeGenderPojo>,
                                                private val rawQueries: Map<String, String>,
                                                val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val mutateChangeGenderResponse = MutableLiveData<Result<ChangeGenderResult>>()

    fun mutateChangeGender(gender: Int) {

        rawQueries[ProfileCompletionQueriesConstant.MUTATION_CHANGE_GENDER]?.let { query ->

            val params = mapOf(PARAM_GENDER to gender)

            graphqlUseCase.setTypeClass(ChangeGenderPojo::class.java)
            graphqlUseCase.setRequestParams(params)
            graphqlUseCase.setGraphqlQuery(query)

            graphqlUseCase.execute(
                    onSuccessMutateChangeGender(gender),
                    onErrorMutateChangeGender()
            )
        }
    }

    private fun onErrorMutateChangeGender(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutateChangeGenderResponse.value = Fail(it)
        }
    }

    private fun onSuccessMutateChangeGender(gender: Int): (ChangeGenderPojo) -> Unit {
        return {

            val errorMessage = it.changeGenderData.userProfileCompletionUpdate.errorMessage
            val isSuccess = it.changeGenderData.userProfileCompletionUpdate.isSuccess

            if (errorMessage.isBlank() && isSuccess) {
                mutateChangeGenderResponse.value = Success(ChangeGenderResult(it.changeGenderData, gender))
            } else if (!errorMessage.isBlank()) {
                mutateChangeGenderResponse.value = Fail(MessageErrorException(errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
            } else {
                mutateChangeGenderResponse.value = Fail(RuntimeException())
            }
        }
    }
}