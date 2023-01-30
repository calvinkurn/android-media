package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateSearchData(
    @SerializedName("searchAffiliate")
    var searchAffiliate: SearchAffiliate?
) {
    data class SearchAffiliate(
        @SerializedName("data")
        var `data`: Data?
    ) {
        data class Data(
            @SerializedName("cards")
            var cards: List<Card?>?,
            @SerializedName("error")
            var error: Error?,
            @SerializedName("status")
            var status: Int?
        ) {
            data class Card(
                @SerializedName("hasMore")
                var hasMore: Boolean?,
                @SerializedName("id")
                var id: String?,
                @SerializedName("items")
                var items: List<Item?>?,
                @SerializedName("title")
                var title: String?,
                @SerializedName("pageType")
                var pageType: String?,
                @SerializedName("itemID")
                var itemID: Long?
            ) {
                data class Item(
                    @SerializedName("productID")
                    var productID: String,
                    @SerializedName("additionalInformation")
                    var additionalInformation: List<AdditionalInformation?>?,
                    @SerializedName("cardUrl")
                    var cardUrl: String?,
                    @SerializedName("commission")
                    var commission: Commission?,
                    @SerializedName("footer")
                    var footer: List<Footer?>?,
                    @SerializedName("image")
                    var image: Image?,
                    @SerializedName("rating")
                    var rating: Int?,
                    @SerializedName("status")
                    var status: Status?,
                    @SerializedName("title")
                    var title: String?,
                    @SerializedName("titleEmblem")
                    var titleEmblem: String?,
                    @SerializedName("label")
                    var label: Label?,
                    @SerializedName("message")
                    var message: String?,
                    @SerializedName("ssaMessage")
                    var ssaMessage: String?,
                    // custom Attribute
                    var type: String?,
                    var itemId: String
                ) {
                    data class Label(
                        @SerializedName("LabelType")
                        var labelType: String,
                        @SerializedName("LabelText")
                        var labelText: String
                    )

                    data class AdditionalInformation(
                        @SerializedName("color")
                        var color: String?,
                        @SerializedName("htmlText")
                        var htmlText: String?,
                        @SerializedName("type")
                        var type: Int?
                    )

                    data class Commission(
                        @SerializedName("amount")
                        var amount: Int?,
                        @SerializedName("amountFormatted")
                        var amountFormatted: String?,
                        @SerializedName("percentage")
                        var percentage: Int?,
                        @SerializedName("percentageFormatted")
                        var percentageFormatted: String?
                    )

                    data class Footer(
                        @SerializedName("footerIcon")
                        var footerIcon: String?,
                        @SerializedName("footerText")
                        var footerText: String?,
                        @SerializedName("footerType")
                        var footerType: Int?
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

                    data class Status(
                        @SerializedName("isLinkGenerationAllowed")
                        var isLinkGenerationAllowed: Boolean?,
                        @SerializedName("messages")
                        var messages: List<Messages?>?
                    ) {
                        data class Messages(
                            @SerializedName("title")
                            var title: String?,
                            @SerializedName("description")
                            var description: String?,
                            @SerializedName("messageType")
                            var messageType: Int?,
                        )
                    }
                }
            }

            data class Error(
                @SerializedName("errorCta")
                var errorCta: List<ErrorCta?>?,
                @SerializedName("errorImage")
                var errorImage: ErrorImage?,
                @SerializedName("errorStatus")
                var errorStatus: Int?,
                @SerializedName("errorType")
                var errorType: Int?,
                @SerializedName("message")
                var message: String?,
                @SerializedName("title")
                var title: String?
            ) {
                data class ErrorCta(
                    @SerializedName("ctaText") val ctaText: String?,
                    @SerializedName("ctaType") val ctaType: Int?,
                    @SerializedName("ctaAction") val ctaAction: Int?,
                    @SerializedName("ctaLink") val ctaLink: CtaLink?,
                ) {

                    data class CtaLink(
                        @SerializedName("DesktopURL") val desktopUrl: String?,
                        @SerializedName("MobileURL") val mobileUrl: String?,
                        @SerializedName("IosURL") val iosUrl: String?,
                        @SerializedName("AndroidURL") val androidUrl: String?
                    )
                }

                data class ErrorImage(
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
