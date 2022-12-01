package com.tokopedia.tokopedianow.repurchase.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.repurchase.domain.model.TokoNowRepurchasePageResponse
import com.tokopedia.tokopedianow.repurchase.domain.model.TokoNowRepurchasePageResponse.*
import com.tokopedia.tokopedianow.repurchase.domain.param.GetRepurchaseProductListParam
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRepurchaseProductListUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) {

    companion object {
        private const val WAREHOUSE_ID = "warehouseID"
        private const val QUERY_PARAM = "queryParam"

        private const val DEFAULT_ERROR_MESSAGE = "Failed to get repurchase product list"

        private val QUERY = """
            query TokonowRepurchasePage(
                ${'$'}$WAREHOUSE_ID: String!, 
                ${'$'}$QUERY_PARAM: String 
            ) {
              TokonowRepurchasePage(
                warehouseID:${'$'}$WAREHOUSE_ID, 
                queryParam:${'$'}$QUERY_PARAM 
              ) {
                header {
                  process_time
                  messages
                  reason
                  error_code
                }
                data {
                  meta {
                    page
                    hasNext
                    totalScan
                  }
                  listProduct {
                    id
                    name
                    url
                    imageUrl
                    price
                    slashedPrice
                    discountPercentage
                    parentProductId
                    rating
                    ratingAverage
                    countReview
                    minOrder
                    maxOrder
                    stock
                    category
                    categoryID
                    wishlist
                    labelGroup {
                      position
                      title
                      type
                      url
                    }
                    labelGroupVariant {
                      title
                      type
                      typeVariant
                      hexColor
                    }
                    shop {
                      id
                    }
                  }
                }
              }
            }
        """.trimIndent()
    }

    private val graphql by lazy { GraphqlUseCase<TokoNowRepurchasePageResponse>(graphqlRepository) }

    suspend fun execute(param: GetRepurchaseProductListParam): GetRepurchaseProductListResponse {
        return graphql.run {
            val requestParams = RequestParams().apply {
                putString(WAREHOUSE_ID, param.warehouseID)
                putString(QUERY_PARAM, param.generateQuery())
            }.parameters

            setGraphqlQuery(QUERY)
            setTypeClass(TokoNowRepurchasePageResponse::class.java)
            setRequestParams(requestParams)

            val response = executeOnBackground().response
            val messages = response.header.messages

            when {
                messages.isNullOrEmpty() -> response.data
                messages.isNotEmpty() -> {
                    val message = messages.firstOrNull().orEmpty()
                    throw MessageErrorException(message)
                }
                else -> throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
            }
        }
    }
}
