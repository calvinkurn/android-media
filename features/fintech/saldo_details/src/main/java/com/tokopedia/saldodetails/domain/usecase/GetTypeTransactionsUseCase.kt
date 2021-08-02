package com.tokopedia.saldodetails.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.domain.query.GQL_LOAD_TYPE_TRANSACTION_LIST
import com.tokopedia.saldodetails.response.model.GqlCompleteTransactionResponse
import com.tokopedia.saldodetails.view.fragment.new.TransactionType
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
                             transactionType: TransactionType) {
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
                PARAM_PER_PAGE to 25,
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
        private val PARAM_START_DATE = "dateFrom"
        private val PARAM_END_DATE = "dateTo"
        private val PARAM_PER_PAGE = "maxRows"
        private val PARAM_PAGE = "page"
        private val PARAM_SALDO_TYPE = "saldoType"
    }


}