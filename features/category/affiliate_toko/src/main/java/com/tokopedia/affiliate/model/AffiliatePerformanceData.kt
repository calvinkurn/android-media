package com.tokopedia.affiliate.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AffiliatePerformanceData(
        @SerializedName("affiliatePerformance")
        var affiliatePerformance: AffiliatePerformance
) {
    @Keep
    data class AffiliatePerformance(
            @SerializedName("data")
            var `data`: Data
    ) {
        @Keep
        data class Data(
                @SerializedName("error")
                var error: Error,
                @SerializedName("filters")
                var filters: Filters,
                @SerializedName("links")
                var links: Links,
                @SerializedName("performanceSummary")
                var performanceSummary: PerformanceSummary,
                @SerializedName("status")
                var status: Boolean
        ) {
            @Keep
            class Error

            @Keep
            class Filters

            @Keep
            data class Links(
                    @SerializedName("has_more")
                    var hasMore: Boolean,
                    @SerializedName("items")
                    var items: List<Item>,
                    @SerializedName("sectionID")
                    var sectionID: Int,
                    @SerializedName("sectionTitle")
                    var sectionTitle: String,
                    @SerializedName("totalCount")
                    var totalCount: Int
            ) {
                @Keep
                data class Item(
                        @SerializedName("footer")
                        var footer: List<Footer>,
                        @SerializedName("id")
                        var id: Int,
                        @SerializedName("image")
                        var image: Image,
                        @SerializedName("performanceSummary")
                        var performanceSummary: PerformanceSummary,
                        @SerializedName("status")
                        var status: Int,
                        @SerializedName("title")
                        var title: String
                ) {
                    @Keep
                    data class Footer(
                            @SerializedName("footerIcon")
                            var footerIcon: String,
                            @SerializedName("footerText")
                            var footerText: String
                    )

                    @Keep
                    data class Image(
                            @SerializedName("android")
                            var android: String,
                            @SerializedName("desktop")
                            var desktop: String,
                            @SerializedName("ios")
                            var ios: String,
                            @SerializedName("mobile")
                            var mobile: String
                    )

                    @Keep
                    data class PerformanceSummary(
                            @SerializedName("click")
                            var click: Int,
                            @SerializedName("commission")
                            var commission: Int,
                            @SerializedName("compareClick")
                            var compareClick: Int,
                            @SerializedName("compareCommissionPercentage")
                            var compareCommissionPercentage: Int,
                            @SerializedName("compareSold")
                            var compareSold: Int,
                            @SerializedName("conversionPercentage")
                            var conversionPercentage: Double,
                            @SerializedName("formattedCommission")
                            var formattedCommission: String,
                            @SerializedName("sold")
                            var sold: Int
                    )
                }
            }

            @Keep
            data class PerformanceSummary(
                    @SerializedName("click")
                    var click: Int,
                    @SerializedName("commission")
                    var commission: Int,
                    @SerializedName("compareClick")
                    var compareClick: Int,
                    @SerializedName("compareCommissionPercentage")
                    var compareCommissionPercentage: Int,
                    @SerializedName("compareSold")
                    var compareSold: Int,
                    @SerializedName("conversionPercentage")
                    var conversionPercentage: Double,
                    @SerializedName("formattedCommission")
                    var formattedCommission: String,
                    @SerializedName("sold")
                    var sold: Int
            )
        }
    }
}