package com.tokopedia.pms.paymentlist.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.pms.paymentlist.domain.data.NotificationResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("GetListCount", GetPaymentListCountUseCase.GQL_GET_LIST_COUNT)
class GetPaymentListCountUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<NotificationResponse>(graphqlRepository) {

    fun getPaymentCount(
        onSuccess: (Int) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.setTypeClass(NotificationResponse::class.java)
        this.setGraphqlQuery(GetListCount.GQL_QUERY)
        this.setRequestParams(getParams())
        this.execute(
            { result ->
                onSuccess(result.notificationData.statusData.paymentStatusCount ?: 0)
            }, { error ->
                onError(error)
            }
        )
    }

    private fun getParams(): Map<String, Any?> {
        return mapOf(
            PARAM_INPUT to Param(userSession.shopId)
        )
    }

    data class Param(
        @SerializedName(PARAM_SHOP_ID)
        val shopId: String
    )

    companion object {
        private const val PARAM_INPUT = "input"
        private const val PARAM_SHOP_ID = "shop_id"

        const val GQL_GET_LIST_COUNT = """
            query notifications(${'$'}$PARAM_INPUT: NotificationRequest) {
                notifications(input: $$PARAM_INPUT) {
                    buyerOrderStatus {
                        paymentStatus
                    }
                }
            }
        """
    }
}