package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.GetDistrictBoundaryResponse
import com.tokopedia.logisticCommon.domain.param.GetDistrictBoundariesParam
import javax.inject.Inject

class GetDistrictBoundariesUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Long, GetDistrictBoundaryResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: Long): GetDistrictBoundaryResponse {
        return gql.request(graphqlQuery(), GetDistrictBoundariesParam(params))
    }

    companion object {
        private const val QUERY = """
        query keroGetDistrictBoundaryArray(${'$'}districtId: Int!) {
          keroGetDistrictBoundaryArray(input: {
            district_id: ${'$'}districtId
          }) {
            type
            properties {
              id
              name
            }
            geometry {
              type
              coordinates
              crs {
                type
                properties {
                  id
                  name
                }
              }
            }
          }
        }
    """
    }
}
