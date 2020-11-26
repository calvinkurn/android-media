package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.ShopStatusResponse
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ShopStatusTypeUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<ShopType>() {

    companion object {
        const val QUERY = "query GetShopStatusType(\$shopId: Int!) {\n" +
                "  goldGetPMOSStatus(shopID: \$shopId, includeOS: true) {\n" +
                "    data {\n" +
                "      power_merchant {\n" +
                "        status\n" +
                "      }\n" +
                "      official_store {\n" +
                "        status\n" +
                "        error\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val SHOP_ID_KEY = "shopId"

        fun createRequestParams(shopId: Int) = HashMap<String, Any>().apply {
            put(SHOP_ID_KEY, shopId)
        }
    }

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): ShopType {
        val gqlRequest = GraphqlRequest(QUERY, ShopStatusResponse::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val gqlError = gqlResponse.getError(ShopStatusResponse::class.java)
        if (gqlError.isNullOrEmpty()) {
            val shopStatusResponse: ShopStatusResponse = gqlResponse.getData(ShopStatusResponse::class.java)
            val shopStatusType = shopStatusResponse.shopStatusResult?.shopStatus?.getShopType()
            shopStatusType?.let {
                return it
            }
        }
        throw MessageErrorException(gqlError.joinToString { it.message })
    }
}