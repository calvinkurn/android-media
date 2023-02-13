package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliatePerformanceListData(
    @SerializedName("getAffiliatePerformanceList")
    var getAffiliatePerformanceList: GetAffiliatePerformanceList?
) {
    data class GetAffiliatePerformanceList(
        @SerializedName("Data")
        var `data`: Data?
    ) {
        data class Data(
            @SerializedName("Data")
            var `data`: Data?,
            @SerializedName("Error")
            var error: Error?,
            @SerializedName("Status")
            var status: Int?
        ) {
            data class Data(
                @SerializedName("AffiliateID")
                var affiliateID: String?,
                @SerializedName("DayRange")
                var dayRange: String?,
                @SerializedName("EndTime")
                var endTime: String?,
                @SerializedName("ItemTotalCount")
                var itemTotalCount: Int?,
                @SerializedName("ItemTotalCountFmt")
                var itemTotalCountFmt: String?,
                @SerializedName("LastID")
                var lastID: String?,
                @SerializedName("Items")
                var items: List<Item?>?,
                @SerializedName("StartTime")
                var startTime: String?
            ) {
                data class Item(
                    @SerializedName("DefaultLinkURL")
                    var defaultLinkURL: String?,
                    @SerializedName("Image")
                    var image: Image?,
                    @SerializedName("ItemID")
                    var itemID: String?,
                    @SerializedName("LinkGeneratedAt")
                    var linkGeneratedAt: String?,
                    @SerializedName("ItemTitle")
                    var itemTitle: String?,
                    @SerializedName("ItemType")
                    var itemType: Int?,
                    @SerializedName("Metrics")
                    var metrics: List<Metric?>?,
                    @SerializedName("Status")
                    var status: Int?,
                    @SerializedName("message")
                    var message: String?,
                    @SerializedName("ssaMessage")
                    var ssaMessage: String?,
                    @SerializedName("SSAStatus")
                    var ssaStatus: Boolean?,
                    @SerializedName("Label")
                    var ssaLabel: Label?
                ) {
                    data class Image(
                        @SerializedName("AndroidURL")
                        var androidURL: String?,
                        @SerializedName("DesktopURL")
                        var desktopURL: String?,
                        @SerializedName("IosURL")
                        var iosURL: String?,
                        @SerializedName("MobileURL")
                        var mobileURL: String?
                    )

                    data class Metric(
                        @SerializedName("MetricDifferenceValue")
                        var metricDifferenceValue: String?,
                        @SerializedName("MetricDifferenceValueFmt")
                        var metricDifferenceValueFmt: String?,
                        @SerializedName("MetricTitle")
                        var metricTitle: String?,
                        @SerializedName("MetricType")
                        var metricType: String?,
                        @SerializedName("MetricValue")
                        var metricValue: String?,
                        @SerializedName("MetricValueFmt")
                        var metricValueFmt: String?,
                        @SerializedName("Order")
                        var order: Int?
                    )

                    data class Label(
                        @SerializedName("LabelType")
                        var labelType: String?,
                        @SerializedName("LabelText")
                        var labelText: String?
                    )
                }
            }

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
        }
    }
}
