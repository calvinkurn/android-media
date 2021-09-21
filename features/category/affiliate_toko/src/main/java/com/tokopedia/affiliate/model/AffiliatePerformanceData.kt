package com.tokopedia.affiliate.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AffiliatePerformanceData(
        @SerializedName("getAffiliateItemsPerformanceList")
        var getAffiliateItemsPerformanceList: GetAffiliateItemsPerformanceList
) {
    @Keep
    data class GetAffiliateItemsPerformanceList(
            @SerializedName("Data")
            var `data`: Data?
    ) {
        @Keep
        data class Data(
                @SerializedName("Error")
                var error: Error? = null,
                @SerializedName("SectionData")
                var sectionData: SectionData? = null,
                @SerializedName("Status")
                var status: Boolean?
        ) {
            data class Error (
                    @SerializedName("ErrorType") var errorType : Int?,
                    @SerializedName("CtaText") var ctaText : String?,
                    @SerializedName("Message") var message : String?,
                    @SerializedName("ctaLink") val ctaLink : CtaLink?
            ){
                data class CtaLink (
                        @SerializedName("DesktopURL") val desktopUrl : String?,
                        @SerializedName("MobileURL") val mobileUrl : String?,
                        @SerializedName("AndroidURL") val iosUrl : String?,
                        @SerializedName("IosURL") val androidUrl : String?
                )
            }

            @Keep
            data class SectionData(
                    @SerializedName("AffiliateID")
                    var affiliateID: String?,
                    @SerializedName("SectionTitle")
                    var sectionTitle: String?,
                    @SerializedName("ItemTotalCount")
                    var itemTotalCount: Int?,
                    @SerializedName("ItemTotalCountFmt")
                    var itemTotalCountFmt: String?,
                    @SerializedName("StartTime")
                    var startTime: String?,
                    @SerializedName("EndTime")
                    var endTime: String?,
                    @SerializedName("DayRange")
                    var dayRange: Int?,
                    @SerializedName("Utems")
                    var items: List<Item>?
            ) {
                @Keep
                data class Item(
                        @SerializedName("LinkID")
                        var linkID: String,
                        @SerializedName("ItemID")
                        var itemID: String,
                        @SerializedName("ItemType")
                        var itemType: Int?,
                        @SerializedName("ItemTitle")
                        var itemTitle: String?,
                        @SerializedName("DefaultLinkURL")
                        var defaultLinkURL: String?,
                        @SerializedName("Status")
                        var Status: Int?,
                        @SerializedName("Image")
                        var image : Image?,
                        @SerializedName("title")
                        var title: String,
                        @SerializedName("footer")
                        var footer: List<Footer>?
                ) {
                    @Keep
                    data class Footer(
                            @SerializedName("footerIcon")
                            var footerIcon: String?= null,
                            @SerializedName("footerText")
                            var footerText: String?= null
                    )

                    @Keep
                    data class Image(
                            @SerializedName("AndroidURL")
                            var android: String?,
                            @SerializedName("DesktopURL")
                            var desktop: String?,
                            @SerializedName("IosURL")
                            var ios: String?,
                            @SerializedName("MobileURL")
                            var mobile: String?
                    )
                }
            }
        }
    }
}