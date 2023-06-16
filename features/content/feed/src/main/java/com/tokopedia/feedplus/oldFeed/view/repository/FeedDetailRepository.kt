package com.tokopedia.feedplus.oldFeed.view.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

class FeedDetailRepository @Inject constructor(
    private val baseRepository: BaseRepository
) {

    private fun getFeedDetailParam(detailId: String, cursor: String): Map<String, Any> {

        val queryMap = mutableMapOf(
                PARAM_ACTIVITY_ID to detailId,
                PARAM_CURSOR to cursor,
                PARAM_LIMIT to LIMIT_DETAIL
        )
        return mutableMapOf("req" to queryMap)

    }

    @GqlQuery(FEED_GET_ACTIVITY_PRODUCT_QUERY_NAME, FEED_GET_ACTIVITY_PRODUCT_QUERY)
    suspend fun fetchFeedDetail(detailId: String,  cursor: String): FeedXGQLResponse {
        return baseRepository.getGQLData(FeedXGetActivityProductsQuery().getQuery(), FeedXGQLResponse::class.java, getFeedDetailParam(detailId, cursor))
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
