package com.tokopedia.profilecompletion.changegender.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
import javax.inject.Named

class ChangeGenderViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                private val rawQueries: Map<String, String>,
                                                val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val mutateChangeGenderResponse = MutableLiveData<Result<ChangeGenderResult>>()

    fun mutateChangeGender(gender: Int) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {

                val params = mapOf(PARAM_GENDER to gender)

                val graphqlInfoRequest = GraphqlRequest(rawQueries[ProfileCompletionQueriesConstant.MUTATION_CHANGE_GENDER],
                        ChangeGenderPojo::class.java, params)

                graphqlRepository.getReseponse(listOf(graphqlInfoRequest))
            }

            data.getSuccessData<ChangeGenderPojo>().let {
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
        ) {
            it.printStackTrace()
            mutateChangeGenderResponse.value = Fail(it)
        }

    }
}