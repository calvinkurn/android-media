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
) : CoroutineUseCase<Map<String, Any>, StoriesProductResponse>(dispatchers.io) {

    override suspend fun execute(params: Map<String, Any>): StoriesProductResponse {
        return repo.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = QUERY

    fun convertToMap(params: Param): Map<String, Any> {
        val whId = addressHelper.getChosenAddress().tokonow.warehouseId
        return mapOf(
            REQ_PARAM to mapOf(
                SHOP_ID to params.id,
                REQ_WAREHOUSE_ID to whId,
                REQ_LIMIT to 50,
                REQ_WITH_TRACKING to true,
                REQ_CAT_NAME to params.catName,
            )
        )
    }

    data class Param(
        val id: String,
        val catName: String,
    )

    companion object {
        private const val REQ_PARAM = "request"
        private const val SHOP_ID = "id"
        private const val REQ_WAREHOUSE_ID = "warehouseID"
        private const val REQ_LIMIT = "limit"
        private const val REQ_WITH_TRACKING = "withTracking"
        private const val REQ_CAT_NAME = "storyCategory"

        private const val QUERY = """
            query StoriesProduct(${'$'}request: ContentStoryProductsRequest!){
              contentStoryProducts(request:${'$'}request){
                campaign{
                    id
                    status
                    name
                    shortName
                    startTime
                    endTime
                    restrictions{
                      isActive
                      label
                    }
                  }
                products{
                  id
                  hasVariant
                  isParent
                  parentID
                  name
                  imageURL
                  webLink
                  appLink
                  isDiscount
                  discount
                  discountFmt
                  price
                  priceFmt
                  priceOriginal
                  priceOriginalFmt
                  priceDiscount
                  priceDiscountFmt
                  priceMasked
                  priceMaskedFmt
                  isBebasOngkir
                  bebasOngkirStatus
                  bebasOngkirURL
                  totalSold
                  totalSoldFmt
                  isCartable
                  isWishlisted
                  isStockAvailable
                  stockWording
                  stockSoldPercentage
                }
                hasVoucher
                nextCursor
              }
            }
    """
    }
}


