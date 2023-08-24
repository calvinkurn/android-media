package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import javax.inject.Inject

class FeedXGetActivityProductsUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val addressHelper: ChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, FeedXGQLResponse>(dispatchers.io) {

    override suspend fun execute(params: Map<String, Any>): FeedXGQLResponse =
        graphqlRepository.request(
            graphqlQuery(),
            params,
            GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
        )

    override fun graphqlQuery(): String = """
            query FeedXGetActivityProducts(${'$'}req: FeedXGetActivityProductsRequest!){
              feedXGetActivityProducts(req:${'$'}req){
                hasVoucher
                products {
                    id
                    shopID
                    isParent
                    parentID
                    hasVariant
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
                    affiliate {
                        id
                        channel
                    }
                    isStockAvailable
                }
                isFollowed
                contentType
                campaign {
                    id
                    status
                    name
                    shortName
                    startTime
                    endTime
                    restrictions {
                        label
                        isActive
                        __typename
                    }
                }
                nextCursor
              }
            }
    """.trimIndent()

    fun getFeedDetailParam(detailId: String, cursor: String): Map<String, Any> {
        val whId = addressHelper.getChosenAddress().tokonow.warehouseId
        val queryMap = mapOf(
            PARAM_ACTIVITY_ID to detailId,
            PARAM_CURSOR to cursor,
            PARAM_LIMIT to LIMIT_DETAIL,
            PARAMS_WH_ID to whId
        )
        return mapOf("req" to queryMap)
    }

    companion object {
        private const val PARAM_ACTIVITY_ID = "activityID"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_CURSOR = "cursor"
        private const val LIMIT_DETAIL = 99
        private const val PARAMS_WH_ID = "warehouseID"
    }
}
