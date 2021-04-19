package com.tokopedia.orderhistory.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class GetProductOrderHistoryUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatHistoryProductResponse>,
        private val dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    private val paramShopId = "shopID"
    private val paramMinOrderTime = "minOrderTime"
    private var minOrderTime = "0"

    fun loadProductHistory(
            shopId: String,
            onSuccess: (ChatHistoryProductResponse) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.io,
                {
                    val params = generateParams(shopId)
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatHistoryProductResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.main) {
                        onSuccess(response)
                        updateMinOrderTime(response)
                    }
                },
                {
                    withContext(dispatchers.main) {
                        onError(it)
                    }
                }
        )
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