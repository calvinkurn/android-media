package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.ShopTotalFollowers
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopTotalFollowersUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : UseCase<Int>() {

    companion object {
        const val QUERY = "query SingleShopTotalFollowers(\$shopID: Int!) {\n" +
                "  shopInfoByID(input: {\n" +
                "    shopIDs: [\$shopID],\n" +
                "    fields: [\"favorite\"]}) {\n" +
                "    result {\n" +
                "      favoriteData {\n" +
                "        totalFavorite\n" +
                "      }\n" +
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val SHOP_ID_KEY = "shopID"

        fun createRequestParams(shopId: Int) = HashMap<String, Any>().apply {
            put(SHOP_ID_KEY, shopId)
        }
    }

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): Int {
        val gqlRequest = GraphqlRequest(QUERY, ShopTotalFollowers::class.java, params)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val gqlErrors = gqlResponse.getError(ShopTotalFollowers::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val shopTotalFollowersResponse : ShopTotalFollowers = gqlResponse.getData(ShopTotalFollowers::class.java)
            val responseErrorMessage = shopTotalFollowersResponse.shopInfoById.error.message
            responseErrorMessage.run {
                if (isNotEmpty()) throw MessageErrorException(this)
                else return shopTotalFollowersResponse.shopInfoById.result.firstOrNull()?.favoriteData?.totalFavorite?: 0
            }
        } else throw MessageErrorException(gqlErrors.firstOrNull()?.message)
    }
}