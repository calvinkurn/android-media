package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.one_click_checkout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.purchase_platform.features.one_click_checkout.common.STATUS_OK
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model.GetPreferenceByIdGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model.GetPreferenceData
import javax.inject.Inject

class GetPreferenceByIdUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase<GetPreferenceByIdGqlResponse>, val mapper: PreferenceModelMapper) {

    fun execute(profileId: Int, addressId: Int, serviceId: Int, gatewayCode: String, metadata: String, onSuccess: (GetPreferenceData) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(
                PARAM_PROFILE_ID to profileId,
                PARAM_ADDRESS_ID to addressId,
                PARAM_SERVICE_ID to serviceId,
                PARAM_GATEWAY_CODE to gatewayCode,
                PARAM_METADATA to metadata
        ))
        graphqlUseCase.setTypeClass(GetPreferenceByIdGqlResponse::class.java)
        graphqlUseCase.execute({ response: GetPreferenceByIdGqlResponse ->
            if (response.response.status.equals(STATUS_OK, true) && response.response.data.success == 1) {
                onSuccess(mapper.convertToDomainModel(response))
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
        const val PARAM_PROFILE_ID = "profileId"
        const val PARAM_ADDRESS_ID = "addressId"
        const val PARAM_SERVICE_ID = "serviceId"
        const val PARAM_GATEWAY_CODE = "gatewayCode"
        const val PARAM_METADATA = "metadata"
        val QUERY = """
        query get_profile_by_id_occ(${"$"}profileId: Int, ${"$"}addressId: Int, ${"$"}serviceId: Int, ${"$"}gatewayCode: String, ${"$"}metadata: String) {
          get_profile_by_id_occ(profile_id: ${"$"}profileId, address_id: ${"$"}addressId, service_id: ${"$"}serviceId, gateway_code: ${"$"}gatewayCode, metadata: ${"$"}metadata){
                error_message
                status
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
                                metadata
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