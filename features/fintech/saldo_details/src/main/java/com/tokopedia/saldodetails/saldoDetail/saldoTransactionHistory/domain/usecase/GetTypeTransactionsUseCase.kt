package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.commom.query.GQL_LOAD_TYPE_TRANSACTION_LIST
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.GqlCompleteTransactionResponse
import com.tokopedia.saldodetails.commom.utils.TransactionType
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@GqlQuery("GqlLoadTypeTransactionList", GQL_LOAD_TYPE_TRANSACTION_LIST)
class GetTypeTransactionsUseCase @Inject
constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GqlCompleteTransactionResponse>(graphqlRepository) {

    fun loadTypeTransactions(onSuccess: (response: GqlCompleteTransactionResponse) -> Unit,
                             onError: (Throwable) -> Unit,
                             page: Int, startDate: Date, endDate: Date,
                             transactionType: TransactionType
    ) {
        try {
            val params = getRequestParams(startDate, endDate, transactionType.type, page)
            this.setTypeClass(GqlCompleteTransactionResponse::class.java)
            this.setRequestParams(params)
            this.setGraphqlQuery(GqlLoadTypeTransactionList.GQL_QUERY)
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
                                 endDate: Date, transactionType: Int, page: Int)
            : MutableMap<String, Any?> {
        val formattedStartDateStr = getFormattedDate(startDate)
        val formattedEndDateStr = getFormattedDate(endDate)
        return mutableMapOf(
                PARAM_START_DATE to formattedStartDateStr,
                PARAM_END_DATE to formattedEndDateStr,
                PARAM_PER_PAGE to VALUE_PER_PAGE_ITEM,
                PARAM_PAGE to page,
                PARAM_SALDO_TYPE to transactionType
        )
    }

    @Throws(ParseException::class)
    private fun getFormattedDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return dateFormat.format(date)
    }

    companion object {
        private const val PARAM_START_DATE = "dateFrom"
        private const val PARAM_END_DATE = "dateTo"
        private const val PARAM_PER_PAGE = "maxRows"
        private const val PARAM_PAGE = "page"
        private const val PARAM_SALDO_TYPE = "saldoType"
        private const val VALUE_PER_PAGE_ITEM = 25
    }


}