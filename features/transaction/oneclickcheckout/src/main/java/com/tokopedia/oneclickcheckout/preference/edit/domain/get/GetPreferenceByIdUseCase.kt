package com.tokopedia.oneclickcheckout.preference.edit.domain.get

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.domain.mapper.PreferenceModelMapper
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.preference.edit.domain.get.model.GetPreferenceByIdGqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetPreferenceByIdUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<GetPreferenceByIdGqlResponse>, private val mapper: PreferenceModelMapper): UseCase<ProfilesItemModel>() {

    override suspend fun executeOnBackground(): ProfilesItemModel {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(useCaseRequestParams.parameters)
        graphqlUseCase.setTypeClass(GetPreferenceByIdGqlResponse::class.java)
        val result = graphqlUseCase.executeOnBackground()
        if (result.response.status.equals(STATUS_OK, true) && result.response.data.success == 1) {
            return mapper.getProfilesItemModel(result.response.data.profile)
        }
        throw MessageErrorException(result.getErrorMessage() ?: DEFAULT_ERROR_MESSAGE)
    }

    fun generateRequestParams(profileId: Int, addressId: Int, serviceId: Int, gatewayCode: String, metadata: String, paymentProfile: String, fromFlow: String): RequestParams {
        return RequestParams.create().apply {
            putInt(PARAM_PROFILE_ID, profileId)
            putInt(PARAM_ADDRESS_ID, addressId)
            putInt(PARAM_SERVICE_ID, serviceId)
            putString(PARAM_GATEWAY_CODE, gatewayCode)
            putString(PARAM_METADATA, metadata)
            putString(PARAM_PAYMENT_PROFILE, paymentProfile)
            putString(PARAM_FROM_FLOW, fromFlow)
        }
    }

    companion object {
        private const val PARAM_PROFILE_ID = "profileId"
        private const val PARAM_ADDRESS_ID = "addressId"
        private const val PARAM_SERVICE_ID = "serviceId"
        private const val PARAM_GATEWAY_CODE = "gatewayCode"
        private const val PARAM_METADATA = "metadata"
        private const val PARAM_PAYMENT_PROFILE = "paymentProfile"
        private const val PARAM_FROM_FLOW = "fromFlow"
        private val QUERY = """
        query get_profile_by_id_occ(${"$"}profileId: Int, ${"$"}addressId: Int, ${"$"}serviceId: Int, ${"$"}gatewayCode: String, ${"$"}metadata: String, ${"$"}paymentProfile: String, ${"$"}fromFlow: String) {
          get_profile_by_id_occ(profile_id: ${"$"}profileId, address_id: ${"$"}addressId, service_id: ${"$"}serviceId, gateway_code: ${"$"}gatewayCode, metadata: ${"$"}metadata, payment_profile: ${"$"}paymentProfile, from_flow:  ${"$"}fromFlow){
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
                                ticker_message
                        }
                        shipment {
                                service_id
                                service_duration
                                service_name
                                estimation
                        }
                    }
                }
            }
        }
        """.trimIndent()
    }
}