package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliatePerformanceData(
    @SerializedName("getAffiliateItemsPerformanceList")
    var getAffiliateItemsPerformanceList: GetAffiliateItemsPerformanceList?
) {
    data class GetAffiliateItemsPerformanceList(
        @SerializedName("Data")
        var itemPerformanceListData: Data?
    ) {
        data class Data(
            @SerializedName("Error")
            var error: Error?,
            @SerializedName("SectionData")
            var sectionData: SectionData?,
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

            data class SectionData(
                @SerializedName("AffiliateID")
                var affiliateID: String?,
                @SerializedName("DayRange")
                var dayRange: Int?,
                @SerializedName("EndTime")
                var endTime: String?,
                @SerializedName("ItemTotalCount")
                var itemTotalCount: Int?,
                @SerializedName("ItemTotalCountFmt")
                var itemTotalCountFmt: String?,
                @SerializedName("Items")
                var items: List<Item?>?,
                @SerializedName("SectionTitle")
                var sectionTitle: String?,
                @SerializedName("StartTime")
                var startTime: String?
            ) {
                data class Item(
                    @SerializedName("DefaultLinkURL")
                    var defaultLinkURL: String?,
                    @SerializedName("Footer")
                    var footer: List<Footer?>?,
                    @SerializedName("Image")
                    var image: Image?,
                    @SerializedName("ItemID")
                    var itemID: String,
                    @SerializedName("ItemTitle")
                    var itemTitle: String?,
                    @SerializedName("ItemType")
                    var itemType: Int?,
                    @SerializedName("LinkID")
                    var linkID: String,
                    @SerializedName("Status")
                    var status: Int?,
                    @SerializedName("SSAStatus")
                    var ssaStatus: Boolean?,
                    @SerializedName("Message")
                    var message: String?,
                    @SerializedName("Label")
                    var label: Label?,
                    @SerializedName("SSAMessage")
                    var ssaMessage: String?,
                    @SerializedName("Metrics")
                    var metrics: ArrayList<Metrics>?
                ) {
                    data class Footer(
                        @SerializedName("FooterIcon")
                        var footerIcon: String?,
                        @SerializedName("FooterText")
                        var footerText: String?,
                        @SerializedName("FooterType")
                        var footerType: Int?
                    )

                    data class Metrics(
                        @SerializedName("MetricType")
                        var metricType: String?,
                        @SerializedName("MetricTitle")
                        var metricTitle: String?,
                        @SerializedName("MetricValue")
                        var metricValue: String?,
                        @SerializedName("MetricDifferenceValue")
                        var metricDifferenceValue: String?,
                        @SerializedName("trend")
                        var trend: Int?
                    )

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

                    data class Label(
                        @SerializedName("LabelType")
                        var labelType: String?,
                        @SerializedName("LabelText")
                        var labelText: String?
                    )
                }
            }
        }
    }
}
