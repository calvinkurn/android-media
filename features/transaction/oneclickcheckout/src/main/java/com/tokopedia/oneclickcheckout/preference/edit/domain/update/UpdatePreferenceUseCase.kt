package com.tokopedia.oneclickcheckout.preference.edit.domain.update

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_SUCCESS_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.preference.edit.domain.update.model.UpdatePreferenceGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.update.model.UpdatePreferenceRequest
import javax.inject.Inject

class UpdatePreferenceUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<UpdatePreferenceGqlResponse>) {

    fun execute(param: UpdatePreferenceRequest, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to param))
        graphqlUseCase.setTypeClass(UpdatePreferenceGqlResponse::class.java)
        graphqlUseCase.execute({ response: UpdatePreferenceGqlResponse ->
            if (response.response.status.equals(STATUS_OK, true) && response.response.data.success == 1) {
                onSuccess(response.response.data.messages.firstOrNull() ?: DEFAULT_SUCCESS_MESSAGE)
            } else {
                onError(MessageErrorException(response.getErrorMessage() ?: DEFAULT_ERROR_MESSAGE))
            }
        }, { throwable: Throwable ->
            onError(throwable)
        })
    }

    companion object {
        const val PARAM_KEY = "profile"
        val QUERY = """
        mutation update_profile_occ(${"$"}profile: UpdateProfileOCCParams){
          update_profile_occ(profiles: ${"$"}profile){
                error_message,
                status,
                data {
                    messages
                    success
                    profile{
                        profile_id
                        status
                        address {
                                address_id
                                receiver_name
                                address_name
                                address_street
                                district_id
                                district_name
                                city_id
                                city_name
                                province_id
                                province_name
                                phone
                                longitude
                                latitude
                                geolocation
                                postal_code
                        }
                        payment {
                                enable
                                active
                                gateway_code
                                gateway_name
                                image
                                description
                                url
                                fee
                                minimum_amount
                                maximum_amount
                                flags {
                                    pin
                                }
                        }
                        shipment {
                                service_id
                                service_duration
                                service_name
                        }
                    }
                }
          }
        }
        """.trimIndent()
    }
}