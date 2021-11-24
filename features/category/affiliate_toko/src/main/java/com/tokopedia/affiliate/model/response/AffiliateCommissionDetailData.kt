package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName


data class AffiliateCommissionDetailData(
    @SerializedName("getAffiliateCommissionDetail")
    var getAffiliateCommissionDetail: GetAffiliateCommissionDetail?
) {
    data class GetAffiliateCommissionDetail(
        @SerializedName("Data")
        var `data`: Data?
    ) {
        data class Data(
            @SerializedName("card_detail")
            var cardDetail: CardDetail?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("created_at_formatted")
            var createdAtFormatted: String?,
            @SerializedName("detail")
            var detail: List<Detail?>?,
            @SerializedName("detail_title")
            var detailTitle: String?,
            @SerializedName("Error")
            var error: Error?,
            @SerializedName("notes")
            var notes: String?,
            @SerializedName("Status")
            var status: Int?,
            @SerializedName("updated_at")
            var updatedAt: String?,
            @SerializedName("updated_at_formatted")
            var updatedAtFormatted: String?
        ) {

            data class CardDetail(
                @SerializedName("card_price")
                var cardPrice: Double?,
                @SerializedName("card_price_formatted")
                var cardPriceFormatted: String?,
                @SerializedName("card_title")
                var cardTitle: String?,
                @SerializedName("image")
                var image: Image?,
                @SerializedName("orderID")
                var orderId: Int?,
                @SerializedName("productID")
                var productId: Int?,
                @SerializedName("shop_badge")
                var shopBadge: String?,
                @SerializedName("shop_name")
                var shopName: String?
            ) {
                data class Image(
                    @SerializedName("android")
                    var android: String?,
                    @SerializedName("desktop")
                    var desktop: String?,
                    @SerializedName("ios")
                    var ios: String?,
                    @SerializedName("mobile")
                    var mobile: String?
                )
            }
            data class Detail(
                @SerializedName("detail_description")
                var detailDescription: String?,
                @SerializedName("detail_style")
                var detailStyle: String?,
                @SerializedName("detail_title")
                var detailTitle: String?,
                @SerializedName("detail_tooltip")
                var detailTooltip: String?
            )

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