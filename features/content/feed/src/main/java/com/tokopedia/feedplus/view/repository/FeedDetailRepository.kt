package com.tokopedia.feedplus.view.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import javax.inject.Inject

private const val PARAM_DETAIL_ID = "detailID"
private const val PARAM_ACTIVITY_ID = "activityID"
private const val PARAM_PAGE = "pageDetail"
private const val PARAM_USER_ID = "userID"
private const val PARAM_LIMIT_DETAIL = "limitDetail"
private const val PARAM_LIMIT = "limit"
private const val PARAM_CURSOR = "cursor"
private const val LIMIT_DETAIL = 30
const val FEED_X_GET_ACTIVITY_PRODUCTS_QUERY: String = """
query FeedXGetActivityProducts(${'$'}req: FeedXGetActivityProductsRequest!){
  feedXGetActivityProducts(req:${'$'}req){
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
          totalSold
          isBebasOngkir
          bebasOngkirStatus
          bebasOngkirURL
          mods
        }
    nextCursor
  }
}

"""


class FeedDetailRepository @Inject constructor() {

    @Inject
    lateinit var baseRepository: BaseRepository


    private fun getFeedDetailParam(detailId: String, cursor: String): Map<String, Any> {

        val queryMap = mutableMapOf(
                PARAM_ACTIVITY_ID to detailId,
                PARAM_CURSOR to cursor,
                PARAM_LIMIT to LIMIT_DETAIL
        )
        return mutableMapOf("req" to queryMap)

    }

    suspend fun fetchFeedDetail(detailId: String,  cursor: String): FeedXGQLResponse {
        return baseRepository.getGQLData(FEED_X_GET_ACTIVITY_PRODUCTS_QUERY, FeedXGQLResponse::class.java, getFeedDetailParam(detailId, cursor))
    }

}