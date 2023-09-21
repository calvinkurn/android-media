package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.KeroAddrGetDistrictCenterResponse
import com.tokopedia.logisticCommon.domain.param.GetDistrictCenterParam
import javax.inject.Inject

class GetDistrictCenterUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Long, KeroAddrGetDistrictCenterResponse.Data>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: Long): KeroAddrGetDistrictCenterResponse.Data {
        return gql.request(graphqlQuery(), GetDistrictCenterParam(params))
    }

    companion object {
        private const val QUERY =
            """"${'"'}
        query kero_addr_get_district_center(${'$'}districtId: Int!) {
            kero_addr_get_district_center(districtId:${'$'}districtId) {
                district {
                    district_id
                    latitude
                    longitude
                }
            }
        }
    """
    }
}
