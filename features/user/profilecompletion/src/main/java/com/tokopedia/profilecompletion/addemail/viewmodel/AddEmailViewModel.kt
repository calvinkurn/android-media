package com.tokopedia.profilecompletion.addemail.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.profilecompletion.addemail.data.AddEmailPojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_EMAIL
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEmailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                private val rawQueries: Map<String, String>,
                                                val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val mutateAddEmailResponse = MutableLiveData<Result<AddEmailPojo>>()

    fun mutateAddEmail(email: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {

                val params = mapOf(PARAM_EMAIL to email)

                val graphqlInfoRequest = GraphqlRequest(rawQueries[ProfileCompletionQueriesConstant.MUTATION_ADD_EMAIL],
                        AddEmailPojo::class.java, params)

                graphqlRepository.getReseponse(listOf(graphqlInfoRequest))
            }

            data.getSuccessData<AddEmailPojo>().let {
                val errorMessage = it.data.userProfileCompletionUpdate.errorMessage
                val isSuccess = it.data.userProfileCompletionUpdate.isSuccess

                if (errorMessage.isBlank() && isSuccess) {
                    mutateAddEmailResponse.value = Success(it)
                } else if (!errorMessage.isBlank()) {
                    mutateAddEmailResponse.value = Fail(MessageErrorException(errorMessage,
                            ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
                } else {
                    mutateAddEmailResponse.value = Fail(RuntimeException())
                }
            }

        }
        ) {
            it.printStackTrace()
            mutateAddEmailResponse.value = Fail(it)
        }

    }
}