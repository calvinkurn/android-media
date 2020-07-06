package com.tokopedia.oneclickcheckout.preference.edit.domain.create

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_SUCCESS_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.preference.edit.domain.create.model.CreatePreferenceGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.create.model.CreatePreferenceRequest
import javax.inject.Inject

interface CreatePreferenceUseCase {
    fun execute(param: CreatePreferenceRequest, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit)
}

class CreatePreferenceUseCaseImpl @Inject constructor(private val graphqlUseCase: GraphqlUseCase<CreatePreferenceGqlResponse>): CreatePreferenceUseCase {

    override fun execute(param: CreatePreferenceRequest, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to param))
        graphqlUseCase.setTypeClass(CreatePreferenceGqlResponse::class.java)
        graphqlUseCase.execute({ response: CreatePreferenceGqlResponse ->
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
        mutation insert_profile_occ(${"$"}profile: InsertProfileOCCParams){
          insert_profile_occ(profiles: ${"$"}profile){
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