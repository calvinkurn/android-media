package com.tokopedia.affiliate.feature.dashboard.domain.usecase

import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * @author by yfsx on 19/09/18.
 */
class GetAffiliateDashboardUseCase @Inject constructor(private val userSession: UserSessionInterface) : GraphqlUseCase() {

    private val query = """
        query GetDashboard(${'$'}userID: Int!, ${'$'}startDate: String, ${'$'}endDate: String) {
          affiliateStats(startDate: ${'$'}startDate, endDate: ${'$'}endDate){
            totalCommission
            productClick
            profileView
            productSold
            productCount
          }
          affiliateCheck{
            isAffiliate
            status
          }
          affiliatePostQuota{
            formatted
            number
          }
          balance {
            seller_usable
            buyer_usable
          }
          bymeProfileHeader(userIDTarget: ${'$'}userID) {
            profile {
              avatar
              link
              affiliateName
            }
          }
        }
    """
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun getRequest(startDate: Date?, endDate: Date?): GraphqlRequest {
        val request = GraphqlRequest(query, DashboardQuery::class.java, false)
        request.variables = getRequestParams(startDate, endDate, Integer.valueOf(userSession.userId)).parameters
        return request
    }

    private fun getRequestParams(startDate: Date?, endDate: Date?, userId: Int): RequestParams {
        val params = RequestParams.create()
        if (startDate != null) params.putString(PARAM_START_DATE, dateFormat.format(startDate))
        if (endDate != null) params.putString(PARAM_END_DATE, dateFormat.format(endDate))
        params.putInt(PARAM_USER_ID, userId)
        return params
    }

    companion object {
        private const val PARAM_START_DATE = "startDate"
        private const val PARAM_END_DATE = "endDate"
        private const val PARAM_USER_ID = "userID"
    }

}