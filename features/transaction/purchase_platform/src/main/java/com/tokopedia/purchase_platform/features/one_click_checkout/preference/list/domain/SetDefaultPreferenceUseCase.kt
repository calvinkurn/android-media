package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.domain.model.SetDefaultPreferenceGqlResponse
import javax.inject.Inject

class SetDefaultPreferenceUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase<SetDefaultPreferenceGqlResponse>) {

    fun execute(profileId: Int, onSuccess: (SetDefaultPreferenceGqlResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to profileId))
        graphqlUseCase.setTypeClass(SetDefaultPreferenceGqlResponse::class.java)
        graphqlUseCase.execute({ response: SetDefaultPreferenceGqlResponse ->
            if (response.response.status.equals("OK", true)) {
                if (response.response.data.success == 1) {
                    onSuccess(response)
                } else if (response.response.data.messages.isNotEmpty()) {
                    onError(MessageErrorException(response.response.data.messages[0]))
                } else {
                    onError(MessageErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi"))
                }
            } else if (response.response.errorMessage.isNotEmpty()) {
                onError(MessageErrorException(response.response.errorMessage[0]))
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
            mutation set_default_profile_occ(${"$"}profileId : Int) {
            set_default_profile_occ(profile_id: ${"$"}profileId, dummy: 2){
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