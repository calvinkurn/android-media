package com.tokopedia.affiliate.usecase

import com.google.gson.Gson
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_USER_PERFORMANCE
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
//        return repository.getGQLData(
//                GQL_Affiliate_USER_PERFORMANCE,
//            AffiliateUserPerformaListItemData::class.java,
//                createRequestParams(dayRange)
//        )

        return Gson().fromJson("{\n" +
                "        \"getAffiliatePerformance\": {\n" +
                "            \"Data\": {\n" +
                "                \"Status\": 1,\n" +
                "                \"Error\": {\n" +
                "                    \"ErrorType\": 0,\n" +
                "                    \"Message\": \"\",\n" +
                "                    \"CtaText\": \"\",\n" +
                "                    \"CtaLink\": {\n" +
                "                        \"DesktopURL\": \"\",\n" +
                "                        \"AndroidURL\": \"\",\n" +
                "                        \"IosURL\": \"\",\n" +
                "                        \"MobileURL\": \"\"\n" +
                "                    }\n" +
                "                },\n" +
                "                \"Data\": {\n" +
                "                    \"AffiliateID\": \"5479999\",\n" +
                "                    \"DayRange\": \"0\",\n" +
                "                    \"EndTime\": \"\",\n" +
                "                    \"StartTime\": \"\",\n" +
                "                    \"Metrics\": [\n" +
                "                        {\n" +
                "                            \"MetricType\": \"totalItems\",\n" +
                "                            \"MetricTitle\": \"-\",\n" +
                "                            \"MetricValue\": \"24\",\n" +
                "                            \"MetricValueFmt\": \"24\",\n" +
                "                            \"MetricDifferenceValue\": \"-12\",\n" +
                "                            \"MetricDifferenceValueFmt\": \"-12\",\n" +
                "                            \"Order\": 0,\n" +
                "                            \"Tooltip\": {\n" +
                "                                \"Description\": \"\"\n" +
                "                            }\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"MetricType\": \"totalCommission\",\n" +
                "                            \"MetricTitle\": \"Total Pendapatan\",\n" +
                "                            \"MetricValue\": \"102796684\",\n" +
                "                            \"MetricValueFmt\": \"Rp102.796.684\",\n" +
                "                            \"MetricDifferenceValue\": \"-35794092\",\n" +
                "                            \"MetricDifferenceValueFmt\": \"-Rp35.794.092\",\n" +
                "                            \"Order\": 10,\n" +
                "                            \"Tooltip\": {\n" +
                "                                \"Description\": \"Jumlah komisi yang kamu dapatkan dari tingkat pembelian produk dan kunjungan.\",\n" +
                "                                \"Metrics\": [\n" +
                "                                    {\n" +
                "                                        \"MetricType\": \"trafficCommission\",\n" +
                "                                        \"MetricTitle\": \"Komisi Kunjungan\",\n" +
                "                                        \"MetricValue\": \"102796684\",\n" +
                "                                        \"MetricValueFmt\": \"Rp102.796.684\",\n" +
                "                                        \"MetricDifferenceValue\": \"-35794092\",\n" +
                "                                        \"MetricDifferenceValueFmt\": \"-Rp35.794.092\",\n" +
                "                                        \"Order\": 2,\n" +
                "                                        \"Tooltip\": {\n" +
                "                                            \"Description\": \"Komisi kunjungan dihitung dari kunjungan terpilih x komisi per 1 kunjungan. Contoh: 20 kunjungan terpilih x Rp50 = Rp1.000.\"\n" +
                "                                        }\n" +
                "                                    },\n" +
                "                                    {\n" +
                "                                        \"MetricType\": \"orderCommission\",\n" +
                "                                        \"MetricTitle\": \"Komisi Pendapatan\",\n" +
                "                                        \"MetricValue\": \"102796684\",\n" +
                "                                        \"MetricValueFmt\": \"Rp102.796.684\",\n" +
                "                                        \"MetricDifferenceValue\": \"-35794092\",\n" +
                "                                        \"MetricDifferenceValueFmt\": \"-Rp35.794.092\",\n" +
                "                                        \"Order\": 1,\n" +
                "                                        \"Tooltip\": {\n" +
                "                                            \"Description\": \"\"\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"MetricType\": \"order\",\n" +
                "                            \"MetricTitle\": \"Terjual\",\n" +
                "                            \"MetricValue\": \"1341\",\n" +
                "                            \"MetricValueFmt\": \"1.341\",\n" +
                "                            \"MetricDifferenceValue\": \"318\",\n" +
                "                            \"MetricDifferenceValueFmt\": \"318\",\n" +
                "                            \"Order\": 20,\n" +
                "                            \"OrderTooltip\": 0,\n" +
                "                            \"Tooltip\": {\n" +
                "                                \"Description\": \"Total penjualan produk dari link affiliate kamu.\"\n" +
                "                            }\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"MetricType\": \"totalClick\",\n" +
                "                            \"MetricTitle\": \"Total Klik\",\n" +
                "                            \"MetricValue\": \"761733\",\n" +
                "                            \"MetricValueFmt\": \"761.733\",\n" +
                "                            \"MetricDifferenceValue\": \"465866\",\n" +
                "                            \"MetricDifferenceValueFmt\": \"465.866\",\n" +
                "                            \"Order\": 30,\n" +
                "                            \"OrderTooltip\": 0,\n" +
                "                            \"Tooltip\": {\n" +
                "                                \"Description\": \"Total kunjungan dihitung berdasarkan jumlah klik melalui link yang dibagikan.\",\n" +
                "                                \"Metrics\": [\n" +
                "                                    {\n" +
                "                                        \"MetricType\": \"totalClickCopy\",\n" +
                "                                        \"MetricTitle\": \"Total Kunjungan\",\n" +
                "                                        \"MetricValue\": \"761733\",\n" +
                "                                        \"MetricValueFmt\": \"761.733\",\n" +
                "                                        \"MetricDifferenceValue\": \"465866\",\n" +
                "                                        \"MetricDifferenceValueFmt\": \"465.866\",\n" +
                "                                        \"Order\": 3,\n" +
                "                                        \"Tooltip\": {\n" +
                "                                            \"Description\": \"\"\n" +
                "                                        }\n" +
                "                                    },\n" +
                "                                    {\n" +
                "                                        \"MetricType\": \"eligibleClick\",\n" +
                "                                        \"MetricTitle\": \"Total Kunjungan Terpilih\",\n" +
                "                                        \"MetricValue\": \"761733\",\n" +
                "                                        \"MetricValueFmt\": \"761.733\",\n" +
                "                                        \"MetricDifferenceValue\": \"465866\",\n" +
                "                                        \"MetricDifferenceValueFmt\": \"465.866\",\n" +
                "                                        \"Order\": 4,\n" +
                "                                        \"Tooltip\": {\n" +
                "                                            \"Description\": \"Semua total kunjungan yang dipilih berdasarkan aturan yang berlaku di Tokopedia dan juga dieliminasi dari pelanggaran & bot.\"\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"MetricType\": \"conversion\",\n" +
                "                            \"MetricTitle\": \"Konversi\",\n" +
                "                            \"MetricValue\": \"0.1\",\n" +
                "                            \"MetricValueFmt\": \"0,1%\",\n" +
                "                            \"MetricDifferenceValue\": \"-0.2\",\n" +
                "                            \"MetricDifferenceValueFmt\": \"-0,2%\",\n" +
                "                            \"Order\": 40,\n" +
                "                            \"Tooltip\": {\n" +
                "                                \"Description\": \"Persentase penjualan produk dari link affiliate kamu.\"\n" +
                "                            }\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }",AffiliateUserPerformaListItemData::class.java)
    }

    companion object {
        private const val PARAM_DAY_RANGE = "dayRange"
    }
}