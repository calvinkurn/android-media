package com.tokopedia.pms.paymentlist.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pms.paymentlist.domain.data.CancelDetailWrapper
import com.tokopedia.pms.paymentlist.domain.data.DataCancelDetail
import javax.inject.Inject

@GqlQuery("PaymentCancelDetailQuery", PaymentCancelDetailUseCase.GQL_GET_CANCEL_QUERY)
class PaymentCancelDetailUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DataCancelDetail>(graphqlRepository) {

    fun getCancelDetail(
        onSuccess: (CancelDetailWrapper) -> Unit,
        onError: (Throwable) -> Unit,
        transactionId: String,
        merchantCode: String,
        productName: String?
    ) {
        this.setTypeClass(DataCancelDetail::class.java)
        this.setRequestParams(getRequestParams(transactionId, merchantCode))
        this.setGraphqlQuery(PaymentCancelDetailQuery.GQL_QUERY)
        this.execute(
            { result ->
                onSuccess(
                    CancelDetailWrapper(
                        transactionId,
                        merchantCode,
                        productName,
                        result.cancelDetail
                    )
                )
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
        const val GQL_GET_CANCEL_QUERY = """
    query cancelDetail(${'$'}transactionID: String!, ${'$'}merchantCode: String!) {
    cancelDetail(transactionID: ${'$'}transactionID, merchantCode: ${'$'}merchantCode) {
      success
      hasRefund
      refundCCAmount
      refundWalletAmount
      refundMessage
      combineMessage
    }
}
"""

    }
}
