package com.tokopedia.content.common.producttag.domain.usecase

import com.tokopedia.content.common.producttag.model.GetFeedLastTaggedProductResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
@GqlQuery(GetFeedLastTaggedProductUseCase.QUERY_NAME, GetFeedLastTaggedProductUseCase.QUERY)
class GetFeedLastTaggedProductUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetFeedLastTaggedProductResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetFeedLastTaggedProductUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetFeedLastTaggedProductResponse::class.java)
    }

    companion object {
        private const val PARAM_AUTHOR_ID = "authorID"
        private const val PARAM_AUTHOR_TYPE = "authorType"
        private const val PARAM_CURSOR = "cursor"
        private const val PARAM_LIMIT = "limit"

        const val QUERY_NAME = "GetFeedLastTaggedProductUseCaseQuery"
        const val QUERY = """
            query FeedXGetLastTaggedProducts(${"$$PARAM_AUTHOR_ID"}: String!, ${"$$PARAM_AUTHOR_TYPE"}: String!, ${"$$PARAM_CURSOR"}: String!, ${"$$PARAM_LIMIT"}: Int!) {
              feedXGetLastTaggedProducts(req: {
                $PARAM_AUTHOR_ID: ${"$$PARAM_AUTHOR_ID"}, 
                $PARAM_AUTHOR_TYPE: ${"$$PARAM_AUTHOR_TYPE"}, 
                $PARAM_CURSOR: ${"$$PARAM_CURSOR"}, 
                $PARAM_LIMIT: ${"$$PARAM_LIMIT"}
              }) {
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
              }
            }
        """

        fun createParams(
            authorId: String,
            authorType: String,
            cursor: String,
            limit: Int,
        ): Map<String, Any> = mapOf(
            PARAM_AUTHOR_ID to authorId,
            PARAM_AUTHOR_TYPE to authorType,
            PARAM_CURSOR to cursor,
            PARAM_LIMIT to limit
        )
    }
}