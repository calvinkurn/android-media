package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(FeedXGetActivityProductsUseCase.FEED_GET_ACTIVITY_PRODUCT_QUERY_NAME, FeedXGetActivityProductsUseCase.FEED_GET_ACTIVITY_PRODUCT_QUERY)
class FeedXGetActivityProductsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<FeedXGQLResponse>(graphqlRepository) {

    init {
        setTypeClass(FeedXGQLResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(FeedXGetActivityProductsQuery())
    }

    fun getFeedDetailParam(detailId: String, cursor: String): Map<String, Any> {
        val queryMap = mutableMapOf(
            PARAM_ACTIVITY_ID to detailId,
            PARAM_CURSOR to cursor,
            PARAM_LIMIT to LIMIT_DETAIL
        )
        return mutableMapOf("req" to queryMap)
    }

    companion object {
        private const val PARAM_ACTIVITY_ID = "activityID"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_CURSOR = "cursor"
        private const val LIMIT_DETAIL = 30

        const val FEED_GET_ACTIVITY_PRODUCT_QUERY_NAME = "FeedXGetActivityProductsQuery"
        const val FEED_GET_ACTIVITY_PRODUCT_QUERY = """
            query FeedXGetActivityProducts(${'$'}req: FeedXGetActivityProductsRequest!){
              feedXGetActivityProducts(req:${'$'}req){
                hasVoucher
                products{
                      id
                      shopID
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
                      priceMasked
                      priceMaskedFmt
                      stockWording
                      stockSoldPercentage
                      cartable
                      totalSold
                      isBebasOngkir
                      bebasOngkirStatus
                      bebasOngkirURL
                      mods
                    }
                    isFollowed
                    contentType
                    campaign{
                      id
                      status
                      name
                      shortName
                      startTime
                      endTime
                      restrictions{
                      label
                      isActive
                      __typename
                      }
                    }
                nextCursor
              }
            }
            """
    }
}
