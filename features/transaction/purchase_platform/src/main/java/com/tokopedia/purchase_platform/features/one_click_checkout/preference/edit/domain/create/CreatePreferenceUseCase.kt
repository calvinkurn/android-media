package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.create

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.one_click_checkout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.purchase_platform.features.one_click_checkout.common.STATUS_OK
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.create.model.CreatePreferenceGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.create.model.CreatePreferenceRequest
import javax.inject.Inject

class CreatePreferenceUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<CreatePreferenceGqlResponse>) {

    fun execute(param: CreatePreferenceRequest, onSuccess: (CreatePreferenceGqlResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to param))
        graphqlUseCase.setTypeClass(CreatePreferenceGqlResponse::class.java)
        graphqlUseCase.execute({ response: CreatePreferenceGqlResponse ->
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