package com.tokopedia.saldodetails.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.di.GqlQueryModule
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse
import com.tokopedia.saldodetails.response.model.GqlCompleteTransactionResponse
import com.tokopedia.saldodetails.view.fragment.new.TransactionType
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class GetTypeTransactionsUseCase @Inject
constructor(@Named(GqlQueryModule.DEPOSITE_ALL_TRANSACTION_QUERY) val query: String,
            graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GqlCompleteTransactionResponse>(graphqlRepository) {

    fun loadTypeTransactions(page: Int, startDate: Date, endDate: Date,
                             transactionType: TransactionType,
                             onSuccess: (response: GqlCompleteTransactionResponse) -> Unit,
                             onError: (Throwable) -> Unit) {
        try {
            val params = getRequestParams(startDate, endDate, transactionType.type, page)
            this.setTypeClass(GqlCompleteTransactionResponse::class.java)
            this.setRequestParams(params)
            this.setGraphqlQuery(query)
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
    private fun getRequestParams(startDateUnix: Date,
                                 endDateUnix: Date, transactionType: Int, page: Int)
            : MutableMap<String, Any?> {
        val formattedStartDateStr = getFormattedDate(startDateUnix)
        val formattedEndDateStr = getFormattedDate(endDateUnix)
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