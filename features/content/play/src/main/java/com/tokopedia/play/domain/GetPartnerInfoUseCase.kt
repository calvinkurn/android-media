package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 2019-12-10.
 */

class GetPartnerInfoUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<ShopInfo>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopInfo {
        val gqlRequest = GraphqlRequest(query, ShopInfo.Response::class.java, params.parameters)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopInfo.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ShopInfo.Response::class.java) as ShopInfo.Response).result.data[0]
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SHOP_IDS = "shopIds"
        private const val PARAM_SHOP_FIELDS = "fields"

        private val query = getQuery()

        private fun getQuery(): String {
            val shopId = "\$shopIds"
            val fields = "\$fields"

            return """query getShopInfo($shopId: [Int!]!, $fields: [String!]!){
                 shopInfoByID(input: {
                     shopIDs: $shopId,
                     fields: $fields}){
                     result {
                         shopCore {
                            name,
                            shopID
                          },
                         favoriteData{
                             totalFavorite
                             alreadyFavorited
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

        fun createParam(shopId: String, fields: List<String>? = DEFAULT_SHOP_FIELDS): RequestParams =
                RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, shopId.toIntOrZero())
            putObject(PARAM_SHOP_FIELDS, fields)
        }
    }
}