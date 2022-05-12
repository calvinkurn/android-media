package com.tokopedia.createpost.producttag.domain.usecase

import com.tokopedia.createpost.producttag.model.FeedAceSearchShopResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
@GqlQuery(FeedAceSearchShopUseCase.QUERY_NAME, FeedAceSearchShopUseCase.QUERY)
class FeedAceSearchShopUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<FeedAceSearchShopResponse>(gqlRepository) {

    init {
        setGraphqlQuery(FeedAceSearchShopUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FeedAceSearchShopResponse::class.java)
    }

    companion object {
        private const val PARAMS = "params"
        private const val PARAM_DEVICE = "device"
        private const val PARAM_FROM = "from"
        private const val PARAM_ROWS = "rows"
        private const val PARAM_START = "start"
        private const val PARAM_QUERY = "q"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_ST = "st"
        private const val PARAM_SORT = "ob"

        const val QUERY_NAME = "FeedAceSearchShopUseCaseQuery"
        const val QUERY = """
            query FeedAceSearchShop(${"$$PARAMS"}: String!) {
              aceSearchShop($PARAMS: ${"$$PARAMS"}) {
                shops {
                  shop_id
                  shop_name
                  shop_domain
                  shop_url
                  shop_image
                  shop_location
                  shop_gold_shop
                  shop_status
                  reputation_image_uri
                  is_official
                  is_pm_pro
                }
              }
            }
        """

        fun createParams(
            rows: Int,
            start: Int,
            query: String,
            sort: Int,
        ): Map<String, Any> {
            return mapOf<String, Any>(
                PARAMS to generateParam(rows, start, query, sort)
            )
        }

        private fun generateParam(
            rows: Int,
            start: Int,
            query: String,
            sort: Int,
        ): String = mutableMapOf<String, Any>(
            PARAM_DEVICE to "android",
            PARAM_FROM to "feed_content",
            PARAM_ROWS to rows,
            PARAM_START to start,
            PARAM_QUERY to query.trim(),
            PARAM_SOURCE to "universe",
            PARAM_ST to "shop",
            PARAM_SORT to sort,
        ).toString()
            .replace("{", "")
            .replace("}", "")
            .replace(", ", "&")
    }
}