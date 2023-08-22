package com.tokopedia.stories.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.stories.usecase.response.StoriesProductResponse
import javax.inject.Inject

/**
 * @author by astidhiyaa on 21/08/23
 */
class StoriesProductUseCase @Inject constructor(
    private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
    private val addressHelper: ChosenAddressRequestHelper,
) : CoroutineUseCase<StoriesProductUseCase.Param, StoriesProductResponse>(dispatchers.io) {

    override suspend fun execute(params: Param): StoriesProductResponse {
        return repo.request(graphqlQuery(), params.convertToMap())
    }

    override fun graphqlQuery(): String = QUERY

    data class Param(
        val id: String,
        val warehouseID: String,
        val limit: Int,
        val cursor: String,
    ) {
        fun convertToMap() : Map<String, Any> {
            return mapOf(
                REQ_PARAM to mapOf(
                    REQ_PRODUCT_ID to id,
                    REQ_WAREHOUSE_ID to warehouseID, //TODO hardcode
                    REQ_LIMIT to limit, //TODO() hardcode
                    REQ_WITH_TRACKING to false,
                )
            )
        }
    }

    companion object {
        private const val REQ_PARAM = "req"
        private const val REQ_PRODUCT_ID = "id"
        private const val REQ_WAREHOUSE_ID = "warehouseID"
        private const val REQ_LIMIT = "limit"
        private const val REQ_WITH_TRACKING = "withTracking"

        private const val QUERY = """
            query StoriesProduct(${'$'}req: ContentStoryProductsRequest!){
              contentStoryProducts(req:${'$'}req){
                products {
                    id
                    shopID
                    isParent
                    parentID
                    hasVariant
                    name
                    imageURL
                    webLink
                    appLink
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
                    isCartable
                    totalSold
                    isBebasOngkir
                    bebasOngkirStatus
                    bebasOngkirURL
                    isStockAvailable
                }
                campaign {
                    id
                    status
                    name
                    shortName
                    startTime
                    endTime
                    restrictions {
                        isActive
                        label
                    }
                }
                hasVoucher
                nextCursor
              }
            }
    """
    }
}


