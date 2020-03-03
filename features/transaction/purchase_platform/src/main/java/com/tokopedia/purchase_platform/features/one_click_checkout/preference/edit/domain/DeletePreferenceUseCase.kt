package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.model.DeletePreferenceGqlResponse
import javax.inject.Inject

class DeletePreferenceUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase<DeletePreferenceGqlResponse>) {

    fun execute(profileId: Int, onSuccess: (DeletePreferenceGqlResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to profileId))
        graphqlUseCase.setTypeClass(DeletePreferenceGqlResponse::class.java)
        graphqlUseCase.execute({ response: DeletePreferenceGqlResponse ->
            if (response.data.status.equals("OK", true)) {
                if (response.data.data.success == 1) {
                    onSuccess(response)
                } else if (response.data.data.messages.isNotEmpty()) {
                    onError(MessageErrorException(response.data.data.messages[0]))
                } else {
                    onError(MessageErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi"))
                }
            } else if (response.data.errorMessage.isNotEmpty()) {
                onError(MessageErrorException(response.data.errorMessage[0]))
            } else {
                onError(MessageErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi"))
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