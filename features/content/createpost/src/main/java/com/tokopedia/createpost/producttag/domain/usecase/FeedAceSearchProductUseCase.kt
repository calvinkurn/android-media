package com.tokopedia.createpost.producttag.domain.usecase

import com.tokopedia.createpost.producttag.model.FeedAceSearchProductResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 28, 2022
 */
@GqlQuery(FeedAceSearchProductUseCase.QUERY_NAME, FeedAceSearchProductUseCase.QUERY)
class FeedAceSearchProductUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<FeedAceSearchProductResponse>(gqlRepository) {

    init {
        setGraphqlQuery(FeedAceSearchProductUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FeedAceSearchProductResponse::class.java)
    }

    companion object {
        private const val PARAMS = "params"
        private const val PARAM_DEVICE = "device"
        private const val PARAM_ROWS = "ROWS"
        private const val PARAM_START = "start"
        private const val PARAM_QUERY = "q"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_SORT = "ob"

        const val QUERY_NAME = "FeedAceSearchProductUseCaseQuery"
        const val QUERY = """
            query FeedAceSearchProduct(${"$$PARAMS"}: String!) {
              ace_search_product_v4($PARAMS: ${"$$PARAMS"}) {
                header {
                  totalData
                  totalDataText
                  responseCode
                  keywordProcess
                  componentId
                }
                data {
                  suggestion {
                    currentKeyword
                    suggestion
                    text
                  }
                  ticker {
                    text
                    query
                  }
                  products {
                    id
                    name
                    shop {
                      id
                      name
                      url
                      city
                      isOfficial
                      isPowerBadge
                    }
                    freeOngkir {
                      isActive
                      imgUrl
                    }
                    url
                    imageUrl300
                    price
                    priceInt
                    originalPrice
                    discountPercentage
                    ratingAverage
                    badges {
                      title
                      imageUrl
                      show
                    }
                    count_sold
                    stock
                  }
                }
              }
            }
        """

        fun createParams(
            rows: Int,
            start: Int,
            query: String,
            shopId: String,
            sort: Int,
        ): Map<String, Any> {
            return mapOf<String, Any>(
                PARAMS to generateParam(rows, start, query, shopId, sort)
            )
        }

        private fun generateParam(
            rows: Int,
            start: Int,
            query: String,
            shopId: String,
            sort: Int,
        ): String = mapOf<String, Any>(
            PARAM_DEVICE to "android",
            PARAM_ROWS to rows,
            PARAM_START to start,
            PARAM_QUERY to query,
            PARAM_SHOP_ID to shopId,
            PARAM_SOURCE to "universe",
            PARAM_SORT to sort,
        ).toString()
            .replace(" ", "")
            .replace("{", "")
            .replace("}", "")
            .replace(",", "&")
    }
}