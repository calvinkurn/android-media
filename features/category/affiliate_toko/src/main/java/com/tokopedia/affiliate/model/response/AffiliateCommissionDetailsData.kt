package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName


data class AffiliateCommissionDetailsData(
    @SerializedName("getAffiliateCommissionDetail")
    var getAffiliateCommissionDetail: GetAffiliateCommissionDetail?
) {


    data class GetAffiliateCommissionDetail(
        @SerializedName("Data")
        var `data`: Data?
    ) {


        data class Data(
            @SerializedName("CardDetail")
            var cardDetail: CardDetail?,
            @SerializedName("CreatedAt")
            var createdAt: String?,
            @SerializedName("CreatedAtFormatted")
            var createdAtFormatted: String?,
            @SerializedName("Detail")
            var detail: List<Detail?>?,
            @SerializedName("DetailTitle")
            var detailTitle: String?,
            @SerializedName("Error")
            var error: Error?,
            @SerializedName("Notes")
            var notes: String?,
            @SerializedName("Status")
            var status: Int?,
            @SerializedName("UpdatedAt")
            var updatedAt: String?,
            @SerializedName("UpdatedAtFormatted")
            var updatedAtFormatted: String?,
            @SerializedName("CommissionType")
            var commisionType: String?
        ) {


            data class CardDetail(
                @SerializedName("CardPrice")
                var cardPrice: Int?,
                @SerializedName("CardPriceFormatted")
                var cardPriceFormatted: String?,
                @SerializedName("CardTitle")
                var cardTitle: String?,
                @SerializedName("Image")
                var image: Image?,
                @SerializedName("OrderID")
                var orderID: String?,
                @SerializedName("ProductID")
                var productID: String?,
                @SerializedName("ShopBadge")
                var shopBadge: String?,
                @SerializedName("ShopName")
                var shopName: String?
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
            }


            data class Detail(
                @SerializedName("DetailDescription")
                var detailDescription: String?,
                @SerializedName("TextType")
                var textType: String?,
                @SerializedName("DetailType")
                var detailType: String?,
                @SerializedName("TextSize")
                var textSize: Int?,
                @SerializedName("DetailTitle")
                var detailTitle: String?,
                @SerializedName("DetailTooltip")
                var detailTooltip: String?,
                @SerializedName("AdvancedTooltip")
                var advanceTooltip: List<Tooltip>
            ){
                data class Tooltip(
                    @SerializedName("TooltipType")
                    var tooltipType: String?,
                    @SerializedName("TextType")
                    var textType: String?,
                    @SerializedName("TextStyle")
                    var textStyle: String?,
                    @SerializedName("TextSize")
                    var textSize: String?,
                    @SerializedName("TooltipText")
                    var tooltipText: String?,
                )
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