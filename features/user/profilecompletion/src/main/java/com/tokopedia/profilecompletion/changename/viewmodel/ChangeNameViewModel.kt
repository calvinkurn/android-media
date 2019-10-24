package com.tokopedia.profilecompletion.changename.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.changename.data.ChangeNamePojo
import com.tokopedia.profilecompletion.changename.data.ChangeNameResult
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_NAME
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * created by rival 23/10/19
 */
class ChangeNameViewModel @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<ChangeNamePojo>,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val mutateChangeNameResponse = MutableLiveData<Result<ChangeNameResult>>()

    fun mutateChangeName(context: Context, fullname: String) {

        GraphqlHelper.loadRawString(context.resources, R.raw.mutation_change_name)?.let { query ->

            val params = mapOf(PARAM_NAME to fullname)

            graphqlUseCase.setTypeClass(ChangeNamePojo::class.java)
            graphqlUseCase.setRequestParams(params)
            graphqlUseCase.setGraphqlQuery(query)

            graphqlUseCase.execute(
                    onSuccessMutateChangeName(fullname),
                    onErrorMutateChangeName()
            )
        }
    }

    private fun onErrorMutateChangeName(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutateChangeNameResponse.value = Fail(it)
        }
    }

    private fun onSuccessMutateChangeName(fullname: String): (ChangeNamePojo) -> Unit {
        return {

            val errorMessage = it.data.errors
            val isSuccess = it.data.isSuccess

            if (errorMessage.isBlank() && isSuccess) {
                mutateChangeNameResponse.value = Success(ChangeNameResult(it.data, fullname))
            } else if (!errorMessage.isBlank()) {
                mutateChangeNameResponse.value = Fail(MessageErrorException(errorMessage))
            } else {
                mutateChangeNameResponse.value = Fail(RuntimeException())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        graphqlUseCase.cancelJobs()
    }
}