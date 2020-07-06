package com.tokopedia.oneclickcheckout.common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.data.model.PreferenceListGqlResponse
import com.tokopedia.oneclickcheckout.common.domain.mapper.PreferenceModelMapper
import com.tokopedia.oneclickcheckout.common.view.model.preference.PreferenceListResponseModel
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

abstract class GetPreferenceListUseCase(defaultDispatchers: CoroutineDispatcher = Dispatchers.Default, mainDispatchers: CoroutineDispatcher = Dispatchers.Main) : UseCase<PreferenceListResponseModel>(defaultDispatchers, mainDispatchers)

class GetPreferenceListUseCaseImpl @Inject constructor(private val graphql: GraphqlUseCase<PreferenceListGqlResponse>, private val preferenceModelMapper: PreferenceModelMapper) : GetPreferenceListUseCase() {
    override suspend fun executeOnBackground(): PreferenceListResponseModel {
        graphql.setGraphqlQuery(QUERY)
        graphql.setTypeClass(PreferenceListGqlResponse::class.java)
        val result = graphql.executeOnBackground()
        if (result.response.status.equals(STATUS_OK, true) && result.response.data.success == 1) {
            return preferenceModelMapper.convertToUIModel(result.response.data)
        }
        throw MessageErrorException(result.getErrorMessage() ?: DEFAULT_ERROR_MESSAGE)
    }

    companion object {
        private val QUERY = """
            query get_preference_list {
                get_all_profiles_occ {
                    error_message
                    status
                    data {
                        messages
                        success
                        max_profile
                        profiles {
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
            }""".trimIndent()
    }
}