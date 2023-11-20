package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateUserPerformaListItemData(
    @SerializedName("getAffiliatePerformance")
    var getAffiliatePerformance: GetAffiliatePerformance
) {
    data class GetAffiliatePerformance(
        @SerializedName("Data")
        var performanceData: Data?
    ) {
        data class Data(
            @SerializedName("Error")
            var error: Error?,
            @SerializedName("Data")
            var userData: UserData,
            @SerializedName("Status")
            var status: Int?
        ) {
            data class Error(
                @SerializedName("CtaLink")
                var ctaLink: CtaLink?,
                @SerializedName("CtaText")
                var ctaText: String?,
                @SerializedName("ErrorType")
                var errorType: Int?,
                @SerializedName("Message")
                var message: String?
            ) {
                data class CtaLink(
                    @SerializedName("AndroidURL")
                    var androidURL: String?,
                    @SerializedName("DesktopURL")
                    var desktopURL: String?,
                    @SerializedName("IosURL")
                    var iosURL: String?,
                    @SerializedName("MobileURL")
                    var mobileURL: String?
                )
            }
            data class UserData(
                @SerializedName("AffiliateID")
                var affiliateID: String?,
                @SerializedName("DayRange")
                var dayRange: String?,
                @SerializedName("EndTime")
                var endTime: String?,
                @SerializedName("StartTime")
                var startTime: String?,
                @SerializedName("Metrics")
                var metrics: List<Metrics?>
            ) {
                data class Metrics(
                    @SerializedName("MetricType")
                    var metricType: String?,
                    @SerializedName("MetricTitle")
                    var metricTitle: String,
                    @SerializedName("MetricValue")
                    var metricValue: String?,
                    @SerializedName("MetricValueFmt")
                    var metricValueFmt: String?,
                    @SerializedName("MetricDifferenceValue")
                    var metricDifferenceValue: String? = "0",
                    @SerializedName("MetricDifferenceValueFmt")
                    var metricDifferenceValueFmt: String?,
                    @SerializedName("Order")
                    var order: Int?,
                    @SerializedName("Tooltip")
                    var tooltip: Tooltip?
                ) {
                    data class Tooltip(
                        @SerializedName("Description")
                        var description: String?,
                        @SerializedName("Ticker")
                        var ticker: String?,
                        @SerializedName("Metrics")
                        var metrics: List<SubMetrics?>
                    ) {
                        data class SubMetrics(
                            @SerializedName("MetricType")
                            var metricType: String?,
                            @SerializedName("MetricTitle")
                            var metricTitle: String,
                            @SerializedName("MetricValue")
                            var metricValue: String?,
                            @SerializedName("MetricValueFmt")
                            var metricValueFmt: String?,
                            @SerializedName("MetricDifferenceValue")
                            var metricDifferenceValue: String? = "0",
                            @SerializedName("MetricDifferenceValueFmt")
                            var metricDifferenceValueFmt: String?,
                            @SerializedName("Order")
                            var order: Int?,
                            @SerializedName("Tooltip")
                            var tooltip: SubTooltip?,

                            // customAttribute
                            var isLastItem: Boolean = false
                        ) {
                            data class SubTooltip(
                                @SerializedName("Metrics")
                                var metrics: List<SubSubMetrics?>,

                                @SerializedName("Description")
                                var description: String?
                            ) {
                                data class SubSubMetrics(
                                    @SerializedName("MetricType")
                                    var metricType: String?,
                                    @SerializedName("MetricTitle")
                                    var metricTitle: String,
                                    @SerializedName("MetricValue")
                                    var metricValue: String?,
                                    @SerializedName("MetricValueFmt")
                                    var metricValueFmt: String?,
                                    @SerializedName("MetricDifferenceValue")
                                    var metricDifferenceValue: String? = "0",
                                    @SerializedName("MetricDifferenceValueFmt")
                                    var metricDifferenceValueFmt: String?,
                                    @SerializedName("Order")
                                    var order: Int?
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
