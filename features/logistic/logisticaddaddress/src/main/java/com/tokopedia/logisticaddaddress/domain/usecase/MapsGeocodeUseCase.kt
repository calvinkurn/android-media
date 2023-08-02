package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticaddaddress.data.entity.mapsgeocode.MapsGeocodeParam
import com.tokopedia.logisticaddaddress.data.entity.mapsgeocode.MapsGeocodeResponse
import javax.inject.Inject

class MapsGeocodeUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<MapsGeocodeParam, MapsGeocodeResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = KERO_ADDRESS_GEOCODE_QUERY

    @GqlQuery(QUERY_GET_ADDRESS_GEOCODE, KERO_ADDRESS_GEOCODE_QUERY)
    override suspend fun execute(params: MapsGeocodeParam): MapsGeocodeResponse {
        return repository.request(graphqlQuery(), params)
    }

    companion object {
        private const val KERO_ADDRESS_GEOCODE_QUERY = """
        query KeroAddressGeocode(${'$'}input: KeroAddressGeocodeInput!) {
            KeroAddressGeocode(input: ${'$'}input) {
                status
                config
                server_process_time
                error_code
                data {
                  address_components {
                    long_name
                    short_name
                    types
                  }
                  formatted_address
                  geometry {
                    types
                    location {
                      lat
                      lng
                    }
                    location_type
                    bounds {
                      northeast {
                        lat
                        lng
                      }
                      southwest {
                        lat
                        lng
                      }
                    }
                    viewport {
                      northeast {
                        lat
                        lng
                      }
                      southwest {
                        lat
                        lng
                      }
                    }
                  }
                  types
                  place_id
                  partial_match
                  plus_code {
                    global_code
                  }
                }
                message_error
            }
        }
    """

        private const val QUERY_GET_ADDRESS_GEOCODE = "KeroAddressGeocode"
    }
}
