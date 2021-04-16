package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import javax.inject.Inject

/**
 * Created by Rafli Syam on 16/04/2021
 */
class GqlGetShopOperationalHoursListUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
): GraphqlUseCase<ShopOperationalHoursListResponse>(gqlRepository) {

    var params: HashMap<String, Any> = hashMapOf()

    override suspend fun executeOnBackground(): ShopOperationalHoursListResponse {
        val request = GraphqlRequest(QUERY, ShopOperationalHoursListResponse::class.java, params)
        return gqlRepository.getReseponse(listOf(request)).getData<ShopOperationalHoursListResponse>(
                ShopOperationalHoursListResponse::class.java
        )
    }

    companion object {

        fun createRequestParams(shopId: String): HashMap<String, Any> = hashMapOf(
                PARAM_SHOP_ID to shopId,
                PARAM_TYPE to TYPE_DEFAULT_VALUE
        )

        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_TYPE = "type"
        private const val TYPE_DEFAULT_VALUE = 1

        private const val QUERY = "query getShopOperationalHours(\$shopID:String!, \$type:Int!) {\n" +
                "  getShopOperationalHours(shopID: \$shopID, type: \$type) {\n" +
                "    data {\n" +
                "      day\n" +
                "      startTime\n" +
                "      endTime\n" +
                "      status\n" +
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"

    }
}