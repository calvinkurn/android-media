package com.tokopedia.pms.paymentlist.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pms.paymentlist.domain.data.DataPaymentList
import com.tokopedia.pms.paymentlist.domain.data.PaymentList
import com.tokopedia.pms.paymentlist.domain.gql.GQL_PAYMENT_LIST_QUERY
import javax.inject.Inject

@GqlQuery("PaymentListQuery", GQL_PAYMENT_LIST_QUERY)
class PaymentListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DataPaymentList>(graphqlRepository) {

    fun getPaymentList(
        onSuccess: (PaymentList) -> Unit,
        onError: (Throwable) -> Unit, cursor: String,
    ) {
        this.setTypeClass(DataPaymentList::class.java)
        this.setRequestParams(getRequestParams(cursor))
        this.setGraphqlQuery(PaymentListQuery.GQL_QUERY)
        this.execute(
            { result ->
                onSuccess(result.paymentList)
            }, { error ->
                onError(error)
            }
        )
    }

    private fun getRequestParams(cursor: String) = mapOf(CURSOR to cursor)

    companion object {
        const val CURSOR = "cursor"
    }
}