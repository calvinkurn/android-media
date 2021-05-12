package com.tokopedia.pms.paymentlist.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pms.payment.data.model.CancelDetail
import com.tokopedia.pms.payment.data.model.DataCancelDetail
import com.tokopedia.pms.paymentlist.domain.gql.GQL_GET_CANCEL_QUERY
import javax.inject.Inject

@GqlQuery("PaymentCancelDetailQuery", GQL_GET_CANCEL_QUERY)
class PaymentCancelDetailUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DataCancelDetail>(graphqlRepository) {

    fun gerCancelDetail(
        onSuccess: (CancelDetail) -> Unit,
        onError: (Throwable) -> Unit,
        transactionId: String,
        merchantCode: String
    ) {
        this.setTypeClass(DataCancelDetail::class.java)
        this.setRequestParams(getRequestParams(transactionId, merchantCode))
        this.setGraphqlQuery(PaymentCancelDetailQuery.GQL_QUERY)
        this.execute(
            { result ->
                onSuccess(result.cancelDetail)
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
    }
}
