package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailScope
import com.tokopedia.buyerorderdetail.domain.mapper.GetBuyerOrderDetailMapper
import com.tokopedia.buyerorderdetail.domain.models.GetResolutionTicketStatusResponse
import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUIModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@BuyerOrderDetailScope
@GqlQuery("GetOrderResolutionQuery", GetOrderResolutionUseCase.QUERY)
class GetOrderResolutionUseCase @Inject constructor(
    val mapper: GetBuyerOrderDetailMapper
) : GraphqlUseCase<GetResolutionTicketStatusResponse>() {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetOrderResolutionQuery())
        setTypeClass(GetResolutionTicketStatusResponse::class.java)
    }

    suspend fun execute(): OrderResolutionUIModel {
        val response = executeOnBackground()
        return mapper.mapResolutionResponseToUIModel(response.resolutionGetTicketStatus?.data)
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
                    font_color
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
    }
}