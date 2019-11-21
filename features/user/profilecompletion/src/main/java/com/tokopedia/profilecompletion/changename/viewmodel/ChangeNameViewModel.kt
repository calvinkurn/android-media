package com.tokopedia.profilecompletion.changename.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNamePojo
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNameResult
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_NAME
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.Exception
import javax.inject.Inject

/**
 * created by rival 23/10/19
 */
class ChangeNameViewModel @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<ChangeNamePojo>,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val mutableChangeNameResponse = MutableLiveData<Result<ChangeNameResult>>()
    val changeNameResponse: LiveData<Result<ChangeNameResult>>
        get() = mutableChangeNameResponse

    fun changePublicName(publicName: String) {
        val rawQuery = rawQueries[ProfileCompletionQueryConstant.MUTATION_CHANGE_NAME]
        if(!rawQuery.isNullOrEmpty()){
            val params = mapOf(PARAM_NAME to publicName)

            graphqlUseCase.setTypeClass(ChangeNamePojo::class.java)
            graphqlUseCase.setRequestParams(params)
            graphqlUseCase.setGraphqlQuery(rawQuery)

            graphqlUseCase.execute(
                    onSuccessMutateChangeName(publicName),
                    onErrorMutateChangeName()
            )
        }
    }

    private fun onErrorMutateChangeName(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableChangeNameResponse.postValue(Fail(it))
        }
    }

    private fun onSuccessMutateChangeName(fullname: String): (ChangeNamePojo) -> Unit {
        return {
            try {
                val errorMessage = it.data.errors
                val isSuccess = it.data.isSuccess
                if (errorMessage.size == 0 && isSuccess == 1) {
                    mutableChangeNameResponse.postValue(Success(ChangeNameResult(it.data, fullname)))
                } else if (!errorMessage[0].isBlank()) {
                    mutableChangeNameResponse.postValue(Fail(MessageErrorException(errorMessage[0])))
                }
            } catch (e: Exception) {
                mutableChangeNameResponse.postValue(Fail(MessageErrorException(e.message)))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        graphqlUseCase.cancelJobs()
    }
}