package com.tokopedia.orderhistory.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.data.Product
import javax.inject.Inject

class GetProductOrderHistoryUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatHistoryProductResponse>
) {

    private val paramShopId = "shopID"
    private val paramMinOrderTime = "minOrderTime"
    private var minOrderTime = "0"

    fun loadProductHistory(
            shopId: String,
            onSuccess: (ChatHistoryProductResponse) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        val params = generateParams(shopId)
        gqlUseCase.apply {
            setTypeClass(ChatHistoryProductResponse::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
            execute({ result ->
                onSuccess(result)
                updateMinOrderTime(result)
            }, { error ->
                onError(error)
            })
        }
    }

    private fun updateMinOrderTime(result: ChatHistoryProductResponse) {
        minOrderTime = result.minOrderTime
    }

    private fun generateParams(shopId: String): Map<String, Any> {
        return mapOf(
                paramShopId to shopId,
                paramMinOrderTime to minOrderTime
        )
    }

    private val query = """
        query chatHistoryProducts($$paramShopId: String!, $$paramMinOrderTime:String!) {
          chatHistoryProducts(shopID:$$paramShopId, minOrderTime:$$paramMinOrderTime) {
            minOrderTime
            products{
              productId
              productUrl
              name
              price
              priceInt
              priceBefore
              priceBeforeInt
              discountedPercentage
              isCampaignActive
              imageUrl
              listImageUrl
              status
              minOrder
              categoryId
              shopId
              playstoreStatus
            }
            hasNext
          }
        }
    """.trimIndent()
}