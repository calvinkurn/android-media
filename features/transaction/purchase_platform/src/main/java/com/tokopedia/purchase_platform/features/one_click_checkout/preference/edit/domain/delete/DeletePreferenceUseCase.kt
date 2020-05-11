package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.delete

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.one_click_checkout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.purchase_platform.features.one_click_checkout.common.STATUS_OK
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.delete.model.DeletePreferenceGqlResponse
import javax.inject.Inject

class DeletePreferenceUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase<DeletePreferenceGqlResponse>) {

    fun execute(profileId: Int, onSuccess: (DeletePreferenceGqlResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to profileId))
        graphqlUseCase.setTypeClass(DeletePreferenceGqlResponse::class.java)
        graphqlUseCase.execute({ response: DeletePreferenceGqlResponse ->
            if (response.response.status.equals(STATUS_OK, true) && response.response.data.success == 1) {
                onSuccess(response)
            } else if (response.response.data.messages.isNotEmpty()) {
                onError(MessageErrorException(response.response.data.messages[0]))
            } else if (response.response.errorMessage.isNotEmpty()) {
                onError(MessageErrorException(response.response.errorMessage[0]))
            } else {
                onError(MessageErrorException(DEFAULT_ERROR_MESSAGE))
            }
        }, { throwable: Throwable ->
            onError(throwable)
        })
    }

    companion object {
        const val PARAM_KEY = "profileId"
        val QUERY = """
            mutation delete_profile_occ(${"$"}profileId : Int) {
              delete_profile_occ(profile_id: ${"$"}profileId){
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