package com.tokopedia.saldodetails.transactionDetailPages.penjualan

import android.content.Context
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.commom.di.scope.SaldoDetailsScope
import com.tokopedia.saldodetails.commom.query.GQL_DEPOSIT_HISTORY_INVOICE
import com.tokopedia.saldodetails.transactionDetailPages.withdrawal.FeeDetailData
import javax.inject.Inject

@GqlQuery("GetDepositInfoQuery", GQL_DEPOSIT_HISTORY_INVOICE)
class GetDepositHistoryInfoUseCase @Inject constructor(
    @SaldoDetailsScope val context: Context,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ResponseDepositHistoryInvoiceInfo>(graphqlRepository) {

    fun getDepositHistoryInvoiceInfo(onSuccess: (DepositHistoryData) -> Unit, onError: (Throwable) -> Unit, summaryId: String) {
        try {
            this.setTypeClass(ResponseDepositHistoryInvoiceInfo::class.java)
            this.setRequestParams(getRequestParams(summaryId))
            this.setGraphqlQuery(GetDepositInfoQuery.GQL_QUERY)
            execute({
                parseResponse(it.response, onSuccess, onError)
            }, {
                onError(it)
            })
        } catch (e: Exception) {
            onError(e)
        }

    }

    private fun parseResponse(response: DepositHistoryInvoiceDetail, onSuccess: (DepositHistoryData) -> Unit, onError: (Throwable) -> Unit) {
        if (response.isSuccess) {
            response.data.depositDetail.add(FeeDetailData(context.getString(R.string.saldo_sales_total_received), response.data.totalAmount,DEFAULT_LATE_STATUS,DEFAULT_LATE_MESSAGE))
            onSuccess(response.data)
        } else onError(NullPointerException("GQL Failure"))
    }

    private fun getRequestParams(summaryId: String) =
        mutableMapOf(
            SaldoDetailsConstants.DetailScreenParams.SUMMARY_ID to summaryId,
        )


    companion object{

        const val DEFAULT_LATE_STATUS = false
        const val DEFAULT_LATE_MESSAGE = ""
    }

}