package com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.di.OrderHistoryScope
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.mapper.OrderDetailMapper
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response.OrderHistoryResponse
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import javax.inject.Inject

@OrderHistoryScope
@GqlQuery("GetOrderHistoryQuery", OrderHistoryUseCase.QUERY)
class OrderHistoryUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    private val mapper: OrderDetailMapper
) : GraphqlUseCase<OrderHistoryResponse>(gqlRepository) {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetOrderHistoryQuery())
        setTypeClass(OrderHistoryResponse::class.java)
    }

    suspend fun execute(): OrderHistoryData {
        val response = executeOnBackground()
        val error = response.getBuyerHistory.errorList.firstOrNull()
        return when {
            response.getBuyerHistory.buyerHistoryData.isNotEmpty() -> mapper.getOrderHistoryData(response)
            else -> {
                error?.let { throw MessageErrorException(it.detail) }
                throw MessageErrorException(ERROR_MESSAGE)
            }
        }
    }

    companion object {
        const val QUERY = """
            query GetBuyerHistory(${'$'}userID: Int!, ${'$'}orderID: String!, ${'$'}requestBy: Int!, ${'$'}lang: String!) {
             get_buyer_history(userID:${'$'}userID, orderID:${'$'}orderID, requestBy:${'$'}requestBy, lang:${'$'}lang) {
              data {
                history_img
                history_title
                histories {
                  orderStatusColor
                  orderStatus
                  actionBy
                  date
                  hour
                  comment
                  status
                }
              }
              errors {
                code
                status
                title
                detail
              }
             }
            }
        """
        private const val ERROR_MESSAGE = "Failed to get order history"
    }

}