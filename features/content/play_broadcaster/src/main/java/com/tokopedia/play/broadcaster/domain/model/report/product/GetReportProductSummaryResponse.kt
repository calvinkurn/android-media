package com.tokopedia.play.broadcaster.domain.model.report.product

import com.google.gson.annotations.SerializedName

/**
 * Created by Jonathan Darwin on 06 March 2024
 */
data class GetReportProductSummaryResponse(
    @SerializedName("broadcasterReportReportProductSummary")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("reportProductAggregate")
        val reportProductAggregate: ReportProductAggregate = ReportProductAggregate(),

        @SerializedName("reportProductMetricsWithDetail")
        val reportProductMetricsWithDetail: List<ReportProductMetricsWithDetail> = emptyList()
    )

    data class ReportProductAggregate(
        @SerializedName("estimatedIncomeFmt")
        val estimatedIncome: String = "",

        @SerializedName("visitPDPFmt")
        val visitPdp: String = "",

        @SerializedName("addToCartFmt")
        val addToCart: String = "",

        @SerializedName("productSoldQtyFmt")
        val productSoldQty: String = "",
    )

    data class ReportProductMetricsWithDetail(
        @SerializedName("reportProductMetric")
        val reportProductMetric: ReportProductMetric = ReportProductMetric(),

        @SerializedName("productName")
        val productName: String = "",

        @SerializedName("productImageURL")
        val productImageURL: String = "",

        @SerializedName("productID")
        val productId: String = "",
    )

    data class ReportProductMetric(
        @SerializedName("estimatedIncomeFmt")
        val estimatedIncome: String = "",

        @SerializedName("visitPDPFmt")
        val visitPdp: String = "",

        @SerializedName("addToCartFmt")
        val addToCart: String = "",

        @SerializedName("productSoldQtyFmt")
        val productSoldQty: String = "",
    )
}
