package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.commom.query.GQL_PENJUALAN_TRANSACTION_LIST
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.GQLSalesTransactionListResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@GqlQuery("GqlPenjualanTransactionList", GQL_PENJUALAN_TRANSACTION_LIST)
class GetSalesTransactionListUseCase
@Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<GQLSalesTransactionListResponse>(graphqlRepository) {

    fun loadSalesTransactions(onSuccess: (response: GQLSalesTransactionListResponse) -> Unit,
                              onError: (Throwable) -> Unit, page: Int, startDate: Date, endDate: Date,) {
        try {
            val params = getRequestParams(startDate, endDate, page)
            this.setTypeClass(GQLSalesTransactionListResponse::class.java)
            this.setRequestParams(params)
            this.setGraphqlQuery(GqlPenjualanTransactionList.GQL_QUERY)
            execute({
                onSuccess(it)
            }, {
                onError(it)
            })
        } catch (e: Exception) {
            onError(e)
        }

    }

    @Throws(ParseException::class)
    private fun getRequestParams(startDate: Date,
                                 endDate: Date, page: Int)
            : MutableMap<String, Any?> {
        val formattedStartDateStr = getFormattedDate(startDate)
        val formattedEndDateStr = getFormattedDate(endDate)
        return mutableMapOf("params"  to mutableMapOf(
            PARAM_START_DATE to formattedStartDateStr,
            PARAM_END_DATE to formattedEndDateStr,
            PARAM_PER_PAGE to VALUE_PER_PAGE_ITEM,
            PARAM_PAGE to page,
            PARAM_INVOICE_NUMBER to ""
        ))
    }

    @Throws(ParseException::class)
    private fun getFormattedDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return dateFormat.format(date)
    }

    companion object {
        private const val PARAM_START_DATE = "date_from"
        private const val PARAM_END_DATE = "date_to"
        private const val PARAM_PER_PAGE = "limit"
        private const val PARAM_PAGE = "page"
        private const val PARAM_INVOICE_NUMBER = "invoice_number"
        private const val VALUE_PER_PAGE_ITEM = 25
    }


}