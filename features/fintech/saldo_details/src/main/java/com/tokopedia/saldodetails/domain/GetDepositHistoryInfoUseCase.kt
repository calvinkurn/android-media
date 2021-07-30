package com.tokopedia.saldodetails.domain

import android.content.Context
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.di.SaldoDetailsScope
import com.tokopedia.saldodetails.domain.query.GQL_DEPOSIT_HISTORY_INVOICE
import com.tokopedia.saldodetails.response.model.saldo_detail_info.DepositHistoryData
import com.tokopedia.saldodetails.response.model.saldo_detail_info.ResponseDepositHistoryInvoiceInfo
import javax.inject.Inject

@GqlQuery("GetDepositInfoQuery", GQL_DEPOSIT_HISTORY_INVOICE)
class GetDepositHistoryInfoUseCase @Inject constructor(graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ResponseDepositHistoryInvoiceInfo>(graphqlRepository) {

    fun getDepositHistoryInvoiceInfo(
        summaryId: String,
        onSuccess: (DepositHistoryData) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(ResponseDepositHistoryInvoiceInfo::class.java)
            this.setRequestParams(getRequestParams(summaryId))
            this.setGraphqlQuery(GetDepositInfoQuery.GQL_QUERY)
            execute({
                if (it.response.isSuccess) onSuccess(it.response.data)
                else onError(NullPointerException("GQL Failure"))
            }, {
                onError(it)
            })
        } catch (e: Exception) {
            onError(e)
        }

    }

    private fun getRequestParams(summaryId: String) =
        mutableMapOf(
            SaldoDetailsConstants.DetailScreenParams.SUMMARY_ID to summaryId,
        )

}