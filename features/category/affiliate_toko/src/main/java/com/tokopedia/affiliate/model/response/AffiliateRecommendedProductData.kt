package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateRecommendedProductData(
    @SerializedName("recommendedAffiliateProduct")
    var recommendedAffiliateProduct: RecommendedAffiliateProduct?
) {
    data class RecommendedAffiliateProduct(
            @SerializedName("data")
            var `data`: Data?,
            @SerializedName("error")
            val error: Error?
    ) {
        data class Data(
                @SerializedName("cards")
                val cards: List<Card?>?,
                @SerializedName("pageInfo")
                val pageInfo: PageInfo?,
                @SerializedName("status")
                val status: Int?
        ) {
            data class Card(
                    @SerializedName("id")
                    val id: String?,
                    @SerializedName("items")
                    val items: List<Item?>?,
                    @SerializedName("title")
                    val title: String?
            ) {
                data class Item(
                        @SerializedName("additionalInformation")
                        val additionalInformation: List<AdditionalInformation?>?,
                        @SerializedName("cardUrl")
                        val cardUrl: CardUrl?,
                        @SerializedName("commission")
                        val commission: Commission?,
                        @SerializedName("footer")
                        val footer: List<Footer?>?,
                        @SerializedName("image")
                        val image: Image?,
                        @SerializedName("productID")
                        val productID: String?,
                        @SerializedName("shopID")
                        val shopID: String?,
                        @SerializedName("rating")
                        val rating: Int?,
                        @SerializedName("title")
                        val title: String?,
                        var isLinkGenerationAllowed : Boolean = true
                ) {
                    data class AdditionalInformation(
                            @SerializedName("color")
                            val color: String?,
                            @SerializedName("htmlText")
                            val htmlText: String?,
                            @SerializedName("type")
                            val type: Int?
                    )

                    data class CardUrl(
                            @SerializedName("AndroidURL")
                            val androidURL: String?,
                            @SerializedName("DesktopURL")
                            val desktopURL: String?,
                            @SerializedName("IosURL")
                            val iosURL: String?,
                            @SerializedName("MobileURL")
                            val mobileURL: String?
                    )

                    data class Commission(
                            @SerializedName("amount")
                            val amount: Int?,
                            @SerializedName("percentage")
                            val percentage: Int?
                    )

                    data class Footer(
                            @SerializedName("footerIcon")
                            val footerIcon: String?,
                            @SerializedName("footerText")
                            val footerText: String?,
                            @SerializedName("footerType")
                            val footerType: Int?
                    )

                    data class Image(
                            @SerializedName("AndroidURL")
                            val androidURL: String?,
                            @SerializedName("DesktopURL")
                            val desktopURL: String?,
                            @SerializedName("IosURL")
                            val iosURL: String?,
                            @SerializedName("MobileURL")
                            val mobileURL: String?
                    )
                }
            }

            data class PageInfo(
                    @SerializedName("currentPage")
                    val currentPage: Int?,
                    @SerializedName("hasNext")
                    val hasNext: Boolean?,
                    @SerializedName("hasPrev")
                    val hasPrev: Boolean?,
                    @SerializedName("totalCount")
                    val totalCount: Int?,
                    @SerializedName("totalPage")
                    val totalPage: Int?
            )
        }

        data class Error(
                @SerializedName("CtaLink")
                val ctaLink: CtaLink?,
                @SerializedName("CtaText")
                val ctaText: String?,
                @SerializedName("ErrorType")
                val errorType: Int?,
                @SerializedName("Message")
                val message: String?
        ) {
            data class CtaLink(
                    @SerializedName("AndroidURL")
                    val androidURL: String?,
                    @SerializedName("DesktopURL")
                    val desktopURL: String?,
                    @SerializedName("IosURL")
                    val iosURL: String?,
                    @SerializedName("MobileURL")
                    val mobileURL: String?
            )
        }
    }
}