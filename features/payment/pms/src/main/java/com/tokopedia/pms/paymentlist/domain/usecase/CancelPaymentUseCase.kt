package com.tokopedia.pms.paymentlist.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pms.paymentlist.domain.data.CancelPayment
import com.tokopedia.pms.paymentlist.domain.data.DataCancelPayment
import javax.inject.Inject

@GqlQuery("CancelPaymentMutation", CancelPaymentUseCase.GQL_CANCEL_PAYMENT_MUTATION)
class CancelPaymentUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DataCancelPayment>(graphqlRepository) {

    fun invokePaymentCancel(
        onSuccess: (CancelPayment) -> Unit,
        onError: (Throwable) -> Unit,
        transactionId: String,
        merchantCode: String
    ) {
        this.setTypeClass(DataCancelPayment::class.java)
        this.setRequestParams(getRequestParams(transactionId, merchantCode))
        this.setGraphqlQuery(CancelPaymentMutation.GQL_QUERY)
        this.execute(
            { result ->
                onSuccess(result.cancelPayment)
            }, { error ->
                onError(error)
            }
        )
    }

    private fun getRequestParams(transactionId: String, merchantCode: String) =
        mapOf(TRANSACTION_ID to transactionId, MERCHANT_CODE to merchantCode)

    companion object {
        const val TRANSACTION_ID = "transactionID"
        const val MERCHANT_CODE = "merchantCode"

        const val GQL_CANCEL_PAYMENT_MUTATION =
            """mutation cancelPayment(${'$'}transactionID: String!, ${'$'}merchantCode: String!){
  cancelPayment(transactionID: ${'$'}transactionID, merchantCode: ${'$'}merchantCode) {
    success
    message
  }
}
"""
    }
}
