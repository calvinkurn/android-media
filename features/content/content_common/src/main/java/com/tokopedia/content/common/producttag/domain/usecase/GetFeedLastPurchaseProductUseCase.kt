package com.tokopedia.content.common.producttag.domain.usecase

import com.tokopedia.content.common.producttag.model.GetFeedLastPurchaseProductResponse
import com.tokopedia.content.common.producttag.model.GetFeedLastTaggedProductResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 28, 2022
 */
@GqlQuery(GetFeedLastPurchaseProductUseCase.QUERY_NAME, GetFeedLastPurchaseProductUseCase.QUERY)
class GetFeedLastPurchaseProductUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetFeedLastPurchaseProductResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetFeedLastPurchaseProductUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetFeedLastPurchaseProductResponse::class.java)
    }

    companion object {
        private const val PARAM_CURSOR = "cursor"
        private const val PARAM_LIMIT = "limit"

        const val QUERY_NAME = "GetFeedLastPurchaseProductUseCaseQuery"
        const val QUERY = """
            query FeedXGetLastPurchaseProducts(${"$$PARAM_CURSOR"}: String!, ${"$$PARAM_LIMIT"}: Int!) {
                feedXGetLastPurchaseProducts(
                    req: {
                        $PARAM_CURSOR: ${"$$PARAM_CURSOR"}, 
                        $PARAM_LIMIT: ${"$$PARAM_LIMIT"}
                    }
                ) {
                    products {
                      id
                      shopID
                      shopName
                      shopBadgeURL
                      name
                      coverURL
                      webLink
                      appLink
                      star
                      price
                      priceFmt
                      isDiscount
                      discount
                      discountFmt
                      priceOriginal
                      priceOriginalFmt
                      priceDiscount
                      priceDiscountFmt
                      totalSold
                      totalSoldFmt
                      isBebasOngkir
                      bebasOngkirStatus
                      bebasOngkirURL
                      mods
                    }
                    nextCursor
                    coachmark
                    isCoachmarkShown
              }
            }
        """

        fun createParams(
            cursor: String,
            limit: Int,
        ): Map<String, Any> = mapOf(
            PARAM_CURSOR to cursor,
            PARAM_LIMIT to limit
        )
    }
}