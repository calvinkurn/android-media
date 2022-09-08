package com.tokopedia.sellerorder.detail.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerorder.detail.data.model.GetResolutionTicketStatusResponse
import com.tokopedia.sellerorder.detail.di.SomDetailScope
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

@SomDetailScope
@GqlQuery("GetOrderHistoryQuery", SomResolutionGetTicketStatusUseCase.QUERY)
class SomResolutionGetTicketStatusUseCase @Inject constructor(
    repository: GraphqlRepository
): GraphqlUseCase<GetResolutionTicketStatusResponse>() {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetOrderHistoryQuery())
        setTypeClass(GetResolutionTicketStatusResponse::class.java)
    }

    suspend fun execute(): Result<GetResolutionTicketStatusResponse> {
        val response = executeOnBackground()
        val errors = response.resolutionGetTicketStatus?.messageError
        return try {
            if (errors.isNullOrEmpty()) {
                Success(response)
            } else {
                Fail(MessageErrorException(errors.firstOrNull() ?: ERROR_MESSAGE))
            }
        } catch (e: Exception) {
            Fail(e)
        }
    }

    companion object {
        const val QUERY = """
            query resolutionGetTicketStatus(${'$'}orderID: Int64!) {
              resolutionGetTicketStatus(orderID: ${'$'}orderID) {
                data {
                  profile_picture
                  card_title
                  resolution_status {
                    status
                    text
                  }
                  deadline {
                    datetime
                    background_color
                    background_color_unify
                    show_deadline
                  }
                  description
                  redirect_path {
                    lite
                    android
                  }
                }
                messageError
                status
              }
            }
        """
        private const val ERROR_MESSAGE = "Failed to get resolution ticket status"
    }

}