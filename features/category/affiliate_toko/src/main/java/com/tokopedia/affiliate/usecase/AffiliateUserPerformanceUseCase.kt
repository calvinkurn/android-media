package com.tokopedia.affiliate.usecase


import com.tokopedia.affiliate.model.GQL_Affiliate_Date_FILTER
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.response.AffiliateDateFilterResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.usecase.AffiliateUserPerformanceUseCase.Companion.GET_AFFILIATE_USER_PERFORMANCE
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery("GetAffiliateUserPerformance", GET_AFFILIATE_USER_PERFORMANCE)
class AffiliateUserPerformanceUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(dayRange: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_DAY_RANGE] = dayRange
        return request
    }

    suspend fun affiliateUserperformance(dayRange: String): AffiliateUserPerformaListItemData {
        return repository.getGQLData(
            GetAffiliateUserPerformance.GQL_QUERY,
            AffiliateUserPerformaListItemData::class.java,
                createRequestParams(dayRange)
        )

    }

    suspend fun getAffiliateFilter(): AffiliateDateFilterResponse {
        return repository.getGQLData(
            GQL_Affiliate_Date_FILTER,
            AffiliateDateFilterResponse::class.java,
            HashMap<String, Any>()
        )
    }

    companion object {
        private const val PARAM_DAY_RANGE = "dayRange"
        const val GET_AFFILIATE_USER_PERFORMANCE = "query getAffiliatePerformance(${'$'}dayRange: String!) {\n" +
            "  getAffiliatePerformance(dayRange: ${"$"}dayRange) {" +
            "    Data {" +
            "      Status" +
            "       Error {" +
            "        ErrorType" +
            "        Message" +
            "        CtaText" +
            "        CtaLink {" +
            "          DesktopURL" +
            "          AndroidURL" +
            "          IosURL" +
            "          MobileURL" +
            "        }" +
            "      }" +
            "      Data {" +
            "        AffiliateID" +
            "        DayRange" +
            "        EndTime" +
            "        StartTime" +
            "        Metrics {" +
            "          MetricType" +
            "          MetricTitle" +
            "          MetricValue" +
            "          MetricValueFmt" +
            "          MetricDifferenceValue" +
            "          MetricDifferenceValueFmt" +
            "          Order" +
            "          Tooltip{" +
            "            Description" +
            "            Ticker" +
            "            Metrics{" +
            "              MetricType" +
            "              MetricTitle" +
            "              MetricValue" +
            "              MetricValueFmt" +
            "              MetricDifferenceValue" +
            "              MetricDifferenceValueFmt" +
            "              Order" +
            "              Tooltip{" +
            "              Description" +
            "                 Metrics{" +
            "                  MetricType" +
            "                  MetricTitle" +
            "                  MetricValue" +
            "                  MetricValueFmt" +
            "                  MetricDifferenceValue" +
            "                  MetricDifferenceValueFmt" +
            "                  Order" +
            "                 }" +
            "              }" +
            "            }" +
            "          }" +
            "        }" +
            "      }" +
            "    }" +
            "  }" +
            "}"
    }
}
