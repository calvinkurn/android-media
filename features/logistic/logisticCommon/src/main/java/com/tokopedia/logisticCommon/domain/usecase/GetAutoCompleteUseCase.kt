package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.domain.param.GetAutoCompleteParam
import javax.inject.Inject

class GetAutoCompleteUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetAutoCompleteParam, AutoCompleteResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetAutoCompleteParam): AutoCompleteResponse {
        return gql.request(graphqlQuery(), params)
    }

    companion object {
        private const val QUERY =
            """query KeroMapsAutoComplete(${'$'}param: String!, ${'$'}latlng: String, ${'$'}is_manage_address_flow: Boolean, ${'$'}with_administrative: Boolean) {
          kero_maps_autocomplete(input: ${'$'}param, latlng: ${'$'}latlng, is_manage_address_flow: ${'$'}is_manage_address_flow, with_administrative: ${'$'}with_administrative) {
            error_code
            data {
              predictions {
                description
                place_id
                types
                matched_substrings {
                  length
                  offset
                }
                terms {
                  value
                  offset
                }
                district_id
                city_id
                province_id
                district_name
                city_name
                province_name
                latitude
                longitude
                postal_code
                title
                structured_formatting {
                  main_text
                  main_text_matched_substrings {
                    length
                    offset
                  }
                  secondary_text
                }
              }
            }
          }
        }
    """
    }
}
