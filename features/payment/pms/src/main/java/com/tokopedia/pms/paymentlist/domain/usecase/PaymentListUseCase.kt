package com.tokopedia.pms.paymentlist.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pms.paymentlist.domain.data.DataPaymentList
import com.tokopedia.pms.paymentlist.domain.data.PaymentList
import javax.inject.Inject

@GqlQuery("PaymentListQuery", PaymentListUseCase.GQL_PAYMENT_LIST_QUERY)
class PaymentListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DataPaymentList>(graphqlRepository) {

    fun getPaymentList(
        onSuccess: (PaymentList?) -> Unit,
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

    private fun getRequestParams(cursor: String) = mapOf(LANGUAGE to "ID", CURSOR to cursor)

    companion object {
        const val CURSOR = "cursor"
        const val LANGUAGE = "lang"

        const val GQL_PAYMENT_LIST_QUERY = """
    query paymentList(${'$'}lang : String!, ${'$'}cursor : String!){
    paymentList(lang: ${'$'}lang, cursor:${'$'}cursor, perPage:20) {
        last_cursor
        has_next_page
        payment_list {
          transaction_id
          transaction_date
          transaction_expire_unix
          merchant_code
          payment_amount
          invoice_url
          product_name
          product_img
          gateway_name
          gateway_img
          payment_code
          is_va
          is_klikbca
          bank_img
          user_bank_account {
            acc_no
            acc_name
            bank_id
          }
          dest_bank_account {
            acc_no
            acc_name
            bank_id
          }
          show_upload_button
          show_edit_transfer_button
          show_edit_klikbca_button
          show_cancel_button
          show_help_page
          ticker_message
          show_ticker_message
          app_link
        }
    }
}
"""
    }
}