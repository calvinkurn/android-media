package com.tokopedia.pms.paymentlist.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pms.paymentlist.domain.data.NotificationResponse
import javax.inject.Inject

@GqlQuery("GetListCount", GetPaymentListCountUseCase.GQL_GET_LIST_COUNT)
class GetPaymentListCountUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<NotificationResponse>(graphqlRepository) {

    fun getPaymentCount(
        onSuccess: (Int) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.setTypeClass(NotificationResponse::class.java)
        this.setGraphqlQuery(GetListCount.GQL_QUERY)
        this.execute(
            { result ->
                onSuccess(result.notificationData.statusData.paymentStatusCount ?: 0)
            }, { error ->
                onError(error)
            }
        )
    }

    companion object {
        const val GQL_GET_LIST_COUNT = """
            query {
                notifications{
                buyerOrderStatus {
                paymentStatus
                }
            }
        }
        """
    }
}