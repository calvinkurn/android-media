package com.tokopedia.orderhistory.usecase

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.data.OrderHistoryParam
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class GetProductOrderHistoryUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher,
) : CoroutineUseCase<OrderHistoryParam, ChatHistoryProductResponse>(dispatcher) {

    private val paramShopId = "shopID"
    private val paramMinOrderTime = "minOrderTime"

//    fun loadProductHistory(
//            shopId: String,
//            onSuccess: (ChatHistoryProductResponse) -> Unit,
//            onError: (Throwable) -> Unit
//    ) {
//        launchCatchError(dispatchers.io,
//                {
//                    val params = generateParams(shopId)
//                    val response = gqlUseCase.apply {
//                        setTypeClass(ChatHistoryProductResponse::class.java)
//                        setRequestParams(params)
//                        setGraphqlQuery(query)
//                    }.executeOnBackground()
//                    withContext(dispatchers.main) {
//                        onSuccess(response)
//                        updateMinOrderTime(response)
//                    }
//                },
//                {
//                    withContext(dispatchers.main) {
//                        onError(it)
//                    }
//                }
//        )
//    }

//    private fun updateMinOrderTime(result: ChatHistoryProductResponse) {
//        minOrderTime = result.minOrderTime
//    }
//
//    private fun generateParams(shopId: String): Map<String, Any> {
//        return mapOf(
//                paramShopId to shopId,
//                paramMinOrderTime to minOrderTime
//        )
//    }

    override fun graphqlQuery(): String {
        return """
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

    override suspend fun execute(params: OrderHistoryParam): ChatHistoryProductResponse {
        return repository.request(graphqlQuery(), params)
    }
}