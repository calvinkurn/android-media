package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class BannerGeneratorDataResponse(
    @SerializedName("getMerchantCampaignBannerGeneratorData")
    val getMerchantCampaignBannerGeneratorData: GetMerchantCampaignBannerGeneratorData = GetMerchantCampaignBannerGeneratorData()
) {
    data class GetMerchantCampaignBannerGeneratorData(
        @SerializedName("campaign")
        val campaign: Campaign = Campaign(),
        @SerializedName("formatted_end_date")
        val formattedEndDate: String = "",
        @SerializedName("formatted_review_end_date")
        val formattedReviewEndDate: String = "",
        @SerializedName("formatted_sharing_end_date")
        val formattedSharingEndDate: String = "",
        @SerializedName("formatted_sharing_review_end_date")
        val formattedSharingReviewEndDate: String = "",
        @SerializedName("formatted_sharing_start_date")
        val formattedSharingStartDate: String = "",
        @SerializedName("formatted_start_date")
        val formattedStartDate: String = "",
        @SerializedName("shop_data")
        val shopData: ShopData = ShopData()
    ) {
        data class Campaign(
            @SerializedName("campaign_id")
            val campaignId: String = "",
            @SerializedName("discount_percentage_text")
            val discountPercentageText: String = "",
            @SerializedName("end_date")
            val endDate: String = "",
            @SerializedName("highlight_products")
            val highlightProducts: HighlightProducts = HighlightProducts(),
            @SerializedName("max_discount_percentage")
            val maxDiscountPercentage: Int = 0,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("review_end_date")
            val reviewEndDate: String = "",
            @SerializedName("start_date")
            val startDate: String = "",
            @SerializedName("status_id")
            val statusId: String = "",
            @SerializedName("status_text")
            val statusText: String = "",
            @SerializedName("total_product")
            val totalProduct: Int = 0,
            @SerializedName("total_product_overload")
            val totalProductOverload: Int = 0
        ) {
            data class HighlightProducts(
                @SerializedName("Products")
                val products: List<Product> = listOf(),
                @SerializedName("total_product")
                val totalProduct: String = "",
                @SerializedName("total_product_wording")
                val totalProductWording: String = ""
            ) {
                data class Product(
                    @SerializedName("campaign")
                    val campaign: Campaign = Campaign(),
                    @SerializedName("ID")
                    val id: Long = 0,
                    @SerializedName("imageUrl")
                    val imageUrl: String = "",
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("URL")
                    val uRL: String = ""
                ) {
                    data class Campaign(
                        @SerializedName("DiscountPercentage")
                        val discountPercentage: Int = 0,
                        @SerializedName("DiscountedPrice")
                        val discountedPrice: String = "",
                        @SerializedName("OriginalPrice")
                        val originalPrice: String = ""
                    )
                }
            }
        }

        data class ShopData(
            @SerializedName("Badge")
            val badge: Badge = Badge(),
            @SerializedName("City")
            val city: String = "",
            @SerializedName("Domain")
            val domain: String = "",
            @SerializedName("IsGold")
            val isGold: Boolean = false,
            @SerializedName("IsOfficial")
            val isOfficial: Boolean = false,
            @SerializedName("Logo")
            val logo: String = "",
            @SerializedName("Name")
            val name: String = "",
            @SerializedName("ShopID")
            val shopID: Int = 0,
            @SerializedName("URL")
            val uRL: String = "",
            @SerializedName("URLApps")
            val uRLApps: String = "",
            @SerializedName("URLMobile")
            val uRLMobile: String = ""
        ) {
            data class Badge(
                @SerializedName("ImageURL")
                val imageURL: String = "",
                @SerializedName("Title")
                val title: String = ""
            )
        }
    }
}