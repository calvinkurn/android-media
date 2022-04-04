package com.tokopedia.affiliate.usecase

import com.google.gson.Gson
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_USER_PERFORMANCE
import com.tokopedia.affiliate.model.response.AffiliateDateFilterResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateUserPerformanceUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(dayRange: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_DAY_RANGE] = dayRange
        return request
    }

    suspend fun affiliateUserperformance(dayRange: String): AffiliateUserPerformaListItemData {
        return repository.getGQLData(
                GQL_Affiliate_USER_PERFORMANCE,
            AffiliateUserPerformaListItemData::class.java,
                createRequestParams(dayRange)
        )
    }

    fun getAffiliateFilter(): AffiliateDateFilterResponse? {
        return Gson().fromJson("\n" +
                "{\n" +
                "  \"data\":{\n" +
                "  \n" +
                "    \"ticker\":[\n" +
                "    {\n" +
                "      \"TickerType\": \"info\",\n" +
                "      \"TickerDescription\": \"Selain data hari ini, Performa Affiliate akan di-update setiap 24:00 - 09:00 WIB.\"\n" +
                "    }\n" +
                "    ],\n" +
                "    \"getAffiliateDateFilter\":[\n" +
                "    {\n" +
                "      \"FilterType\": \"Today\",\n" +
                "      \"FilterTitle\": \"Hari ini\",\n" +
                "      \"FilterValue\": \"0\",\n" +
                "      \"FilterDescription\": \"11 Feb 2022 - update terakhir 07:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"FilterType\": \"Yesterday\",\n" +
                "      \"FilterTitle\": \"Kemarin\",\n" +
                "      \"FilterValue\": \"1\",\n" +
                "      \"FilterDescription\": \"10 Feb 2022\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"FilterType\": \"LastSevenDays\",\n" +
                "      \"FilterTitle\": \"7 Hari Terakhir\",\n" +
                "      \"FilterValue\": \"7\",\n" +
                "      \"FilterDescription\": \"04 Feb 2022 - 10 Feb 2022\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"FilterType\": \"LastThirtyDays\",\n" +
                "      \"FilterTitle\": \"30 Hari Terakhir\",\n" +
                "      \"FilterValue\": \"30\",\n" +
                "      \"FilterDescription\": \"12 Jan 2022 - 10 Feb 2022\"\n" +
                "    }\n" +
                "  ]\n" +
                " }\n" +
                "}",AffiliateDateFilterResponse::class.java)
    }

    companion object {
        private const val PARAM_DAY_RANGE = "dayRange"
    }
}