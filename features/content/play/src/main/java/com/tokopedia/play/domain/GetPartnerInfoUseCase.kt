package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.ShopInfo
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject


/**
 * Created by mzennis on 2019-12-10.
 */

class GetPartnerInfoUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<ShopInfo>(graphqlRepository) {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopInfo {
        val gqlRequest = GraphqlRequest(query, ShopInfo.Response::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

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
        private const val PARAM_SOURCE = "source"
        private const val SOURCE_VALUE = "gql-play"

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

        fun createParam(partnerId: Int, partnerType: PartnerType, fields: List<String>? = DEFAULT_SHOP_FIELDS): RequestParams =
                RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, partnerId)
            putObject(PARAM_SHOP_FIELDS, fields)
            putString(PARAM_SOURCE, SOURCE_VALUE)
        }
    }
}