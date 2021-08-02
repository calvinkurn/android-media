package com.tokopedia.saldodetails.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.domain.query.GQL_ALL_TYPE_TRANSACTION
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@GqlQuery("GQLAllTypeTransaction", GQL_ALL_TYPE_TRANSACTION)
class GetAllTypeTransactionUseCase
@Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<GqlAllDepositSummaryResponse>(graphqlRepository) {

    fun loadAllTypeTransactions(
        onSuccess: (response: GqlAllDepositSummaryResponse) -> Unit,
        onError: (Throwable) -> Unit,
        startDate: Date, endDate: Date
    ) {
        try {
            val params = getRequestParams(startDate, endDate)
            this.setTypeClass(GqlAllDepositSummaryResponse::class.java)
            this.setRequestParams(params)
            this.setGraphqlQuery(GQLAllTypeTransaction.GQL_QUERY)
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
    private fun getRequestParams(startDate: Date, endDate: Date): MutableMap<String, Any?> {
        val formattedStartDateStr = getFormattedDate(startDate)
        val formattedEndDateStr = getFormattedDate(endDate)
        return mutableMapOf(
            PARAM_START_DATE to formattedStartDateStr,
            PARAM_END_DATE to formattedEndDateStr,
            PARAM_PER_PAGE to 25,
            PARAM_PAGE to 1
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
    }

}