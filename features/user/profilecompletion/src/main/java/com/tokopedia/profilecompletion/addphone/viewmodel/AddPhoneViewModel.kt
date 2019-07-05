package com.tokopedia.profilecompletion.addphone.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_MSISDN
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant.PARAM_OTP_CODE
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddPhoneViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                private val rawQueries: Map<String, String>,
                                                val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val mutateAddPhoneResponse = MutableLiveData<Result<AddPhonePojo>>()

    fun mutateAddPhone(msisdn: String, otp : String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {

                val params = mapOf(
                        PARAM_MSISDN to msisdn,
                        PARAM_OTP_CODE to otp)

                val graphqlInfoRequest = GraphqlRequest(rawQueries[ProfileCompletionQueriesConstant.MUTATION_ADD_PHONE],
                        AddPhonePojo::class.java, params)

                graphqlRepository.getReseponse(listOf(graphqlInfoRequest))
            }

            data.getSuccessData<AddPhonePojo>().let {
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
        ) {
            it.printStackTrace()
            mutateAddPhoneResponse.value = Fail(it)
        }

    }
}