package com.tokopedia.oneclickcheckout.preference.list.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_SUCCESS_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.preference.list.domain.model.SetDefaultPreferenceGqlResponse
import javax.inject.Inject

interface SetDefaultPreferenceUseCase {
    fun execute(profileId: Int, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit)
}

class SetDefaultPreferenceUseCaseImpl @Inject constructor(val graphqlUseCase: GraphqlUseCase<SetDefaultPreferenceGqlResponse>) : SetDefaultPreferenceUseCase {

    override fun execute(profileId: Int, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to profileId))
        graphqlUseCase.setTypeClass(SetDefaultPreferenceGqlResponse::class.java)
        graphqlUseCase.execute({ response: SetDefaultPreferenceGqlResponse ->
            if (response.response.status.equals(STATUS_OK, true) && response.response.data.success == 1) {
                onSuccess(response.response.data.messages.firstOrNull() ?: DEFAULT_SUCCESS_MESSAGE)
            } else {
                onError(MessageErrorException(response.response.data.messages.firstOrNull()
                        ?: DEFAULT_ERROR_MESSAGE))
            }
        }, { throwable: Throwable ->
            onError(throwable)
        })
    }

    companion object {
        const val PARAM_KEY = "profileId"
        val QUERY = """
            mutation set_default_profile_occ(${"$"}profileId : Int) {
            set_default_profile_occ(profile_id: ${"$"}profileId){
                  error_message
                  status
                  data{
                    messages
                    success
                  }
              }
            }
        """.trimIndent()
    }
}