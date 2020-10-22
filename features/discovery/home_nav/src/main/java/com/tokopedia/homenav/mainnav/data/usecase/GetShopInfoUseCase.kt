package com.tokopedia.homenav.mainnav.data.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


class GetShopInfoUseCase @Inject constructor(private val gqlUseCase: GraphqlRepository): UseCase<ShopInfoPojo>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopInfoPojo {
        val gqlRequest = GraphqlRequest(query, ShopInfoPojo.Response::class.java, params.parameters)
        val gqlResponse = gqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(ShopInfoPojo.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ShopInfoPojo.Response::class.java) as ShopInfoPojo.Response).result.data[0]
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SHOP_IDS = "shopIds"
        private const val PARAM_SHOP_FIELDS = "fields"
        private const val PARAM_SOURCE = "source"
        private const val SOURCE_VALUE = "home-navigation"

        private val query = getQuery()

        private fun getQuery(): String {
            val shopId = "\$shopIds"
            val fields = "\$fields"
            val source = "\$source"


            return """query getShopInfo($shopId: [Int!]!, $fields: [String!]!, $source: String){
                 shopInfoByID(input: {
                     shopIDs: $shopId,
                     fields: $fields,
                     source: $source}){
                     result {
                         shopCore {
                            name,
                            shopID
                          }
                     }
                     error {
                         message
                     }
                 }
             }
            """.trimIndent()
        }

        private val DEFAULT_SHOP_FIELDS = listOf("core", "favorite", "assets", "shipment",
                "last_active", "location", "terms", "allow_manage",
                "is_owner", "other-goldos", "status", "is_open", "closed_info", "create_info")

        fun createParam(partnerId: Int, fields: List<String>? = DEFAULT_SHOP_FIELDS): RequestParams =
                RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, partnerId)
            putObject(PARAM_SHOP_FIELDS, fields)
            putString(PARAM_SOURCE, SOURCE_VALUE)
        }
    }
}