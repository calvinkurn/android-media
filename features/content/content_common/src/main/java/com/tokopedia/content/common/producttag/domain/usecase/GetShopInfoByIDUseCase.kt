package com.tokopedia.content.common.producttag.domain.usecase

import com.tokopedia.content.common.producttag.model.GetShopInfoByIDResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 20, 2022
 */
@GqlQuery(GetShopInfoByIDUseCase.QUERY_NAME, GetShopInfoByIDUseCase.QUERY)
class GetShopInfoByIDUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetShopInfoByIDResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetShopInfoByIDUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetShopInfoByIDResponse::class.java)
    }

    companion object {
        private const val KEY_SHOP_IDS = "shopIDs"
        private const val KEY_FIELDS = "fields"
        private const val KEY_SOURCE = "source"

        private val fields = listOf("core")

        const val QUERY_NAME = "GetShopInfoByIDUseCaseQuery"
        const val QUERY = """
            query GetShopInfoByIDUseCase(${"$$KEY_SHOP_IDS"}: [Int!]!, ${"$$KEY_FIELDS"}: [String!]!, ${"$$KEY_SOURCE"}: String!) {
              shopInfoByID(input: {
                $KEY_SHOP_IDS: ${"$$KEY_SHOP_IDS"}, 
                $KEY_FIELDS: ${"$$KEY_FIELDS"}, 
                $KEY_SOURCE: ${"$$KEY_SOURCE"}
              }) {
                result {
                  shopCore {
                    shopID
                    name
                  }
                  goldOS {
                    badge
                    isGold
                    isGoldBadge
                    isOfficial
                  }
                }
              }
            }
        """

        fun createParams(
            shopIds: List<Long>,
        ): Map<String, Any> {
            return mapOf(
                KEY_SHOP_IDS to shopIds,
                KEY_FIELDS to fields,
                KEY_SOURCE to "feed_content",
            )
        }
    }
}