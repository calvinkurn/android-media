package com.tokopedia.saldodetails.domain

import android.content.Context
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.di.SaldoDetailsScope
import com.tokopedia.saldodetails.domain.GetDepositHistoryInfoUseCase.Companion.GQL_DEPOSIT_HISTORY
import com.tokopedia.saldodetails.response.model.saldo_detail_info.DepositHistoryData
import com.tokopedia.saldodetails.response.model.saldo_detail_info.ResponseDepositHistoryInvoiceInfo
import com.tokopedia.saldodetails.response.model.saldo_detail_info.WithdrawalInfoData
import java.lang.NullPointerException
import javax.inject.Inject

@GqlQuery("GetDepositInfoQuery", GQL_DEPOSIT_HISTORY)
class GetDepositHistoryInfoUseCase
@Inject constructor(
    @SaldoDetailsScope val context: Context,
    graphqlRepository: GraphqlRepository
) :
    GraphqlUseCase<ResponseDepositHistoryInvoiceInfo>(graphqlRepository) {

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
               if(it.response.isSuccess) onSuccess(it.response.data)
                else onError(NullPointerException(""))
            }, {
                onError(it)
            })
        } catch (e: Exception) {
            onError(e)
        }

    }

    private fun getRequestParams(summaryId: String): MutableMap<String, Any?> {
        return mutableMapOf(
            SUMMARY_ID to summaryId,
        )
    }

    companion object {
        private const val SUMMARY_ID = "summaryID"

        const val GQL_DEPOSIT_HISTORY = """
            query MidasDepositHistoryInvoiceDetail(${'$'}summaryID: String!) {
                 MidasDepositHistoryInvoiceDetail(summaryID: (${'$'}summaryID) {
                    is_success
                    deposit_history {
                        total_amount
                        create_time
                        invoice_no
                        invoice_url
                        order_url_android
                        detail {
                          type_description
                          amount
                        }
                    }
                }
       
        }"""
    }

}