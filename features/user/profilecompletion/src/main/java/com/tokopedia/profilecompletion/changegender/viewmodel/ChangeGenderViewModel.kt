package com.tokopedia.profilecompletion.changegender.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderPojo
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderResult
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_GENDER
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChangeGenderViewModel @Inject constructor(private val graphqlUseCase:  GraphqlUseCase<ChangeGenderPojo>,
                                                val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val mutateChangeGenderResponse = MutableLiveData<Result<ChangeGenderResult>>()

    fun mutateChangeGender(context: Context, gender: Int) {

        GraphqlHelper.loadRawString(context.resources, R.raw.mutation_change_gender)?.let { query ->

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

            val errorMessage = it.data.errorMessage
            val isSuccess = it.data.isSuccess

            if (errorMessage.isBlank() && isSuccess) {
                mutateChangeGenderResponse.value = Success(ChangeGenderResult(it.data, gender))
            } else if (!errorMessage.isBlank()) {
                mutateChangeGenderResponse.value = Fail(MessageErrorException(errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
            } else {
                mutateChangeGenderResponse.value = Fail(RuntimeException())
            }
        }
    }
}