package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.GetDistrictDetailsResponse
import com.tokopedia.logisticCommon.domain.param.GetZipCodeParam
import com.tokopedia.logisticCommon.domain.param.KeroGetAddressCornerInput
import javax.inject.Inject

class GetZipCodeUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetZipCodeParam, GetDistrictDetailsResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetZipCodeParam): GetDistrictDetailsResponse {
        return gql.request(graphqlQuery(), KeroGetAddressCornerInput())
    }

    companion object {
        private const val QUERY =
            """
        query KeroDistrictQuery(${'$'}query: String, ${'$'}page: String){
          keroGetDistrictDetails(query:${'$'}query, page:${'$'}page) {
            district {
              district_id
              district_name
              city_id
              city_name
              province_id
              province_name
              zip_code
            }
            next_available
          }
        }
    """
    }
}
