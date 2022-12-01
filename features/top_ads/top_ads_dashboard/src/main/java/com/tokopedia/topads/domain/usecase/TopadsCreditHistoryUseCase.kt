package com.tokopedia.topads.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_END_DATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_START_DATE
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@GqlQuery("TopadsCreditHistory", QUERY)
class TopadsCreditHistoryUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface,
) : GraphqlUseCase<TopAdsCreditHistory.CreditsResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(TopadsCreditHistory())
        setTypeClass(TopAdsCreditHistory.CreditsResponse::class.java)
    }

    suspend fun executeQuery(
        startDate: Date? = null, endDate: Date? = null,
    ) = flow<Result<TopAdsCreditHistory>> {
        setParams(startDate, endDate)
        val data = executeOnBackground()

        when {
            data.response.errors.isEmpty() -> emit(Success(data.response.dataHistory))
            else -> emit(Fail(ResponseErrorException(data.response.errors)))
        }
    }

    private fun setParams(startDate: Date? = null, endDate: Date? = null) {
        val _startDate = startDate?.let {
            SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(it)
        }
        val _endDate = endDate?.let {
            SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(it)
        }

        val params = mapOf(
            ParamObject.SHOP_Id to userSession.shopId,
            PARAM_START_DATE to _startDate,
            PARAM_END_DATE to _endDate
        )

        setRequestParams(params)
    }
}

private const val QUERY = """
query topadsCreditHistoryV2(${'$'}shopId: String!, ${'$'}startDate: String!, ${'$'}endDate: String!) {
    topadsCreditHistoryV2(shopId: ${'$'}shopId, startDate: ${'$'}startDate, endDate: ${'$'}endDate){
        data{
            total_used
            total_addition
            total_used_fmt
            total_addition_fmt
            credit_history {
                amount
                amount_fmt
                date
                description
                is_reduction
                show_timestamp
                transaction_type
                invoice_url
                status
                expiry_date
            }
        }
        errors {
            code
            detail
            title
        }
    }
}
"""