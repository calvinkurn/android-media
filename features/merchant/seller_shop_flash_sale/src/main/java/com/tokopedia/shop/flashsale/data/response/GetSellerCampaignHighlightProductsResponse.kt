package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GetSellerCampaignHighlightProductsResponse(
    @SerializedName("getSellerCampaignHighlightProducts")
    val getSellerCampaignHighlightProducts: GetSellerCampaignHighlightProducts = GetSellerCampaignHighlightProducts()
) {
    data class GetSellerCampaignHighlightProducts(
        @SerializedName("highlight_product_datas")
        val highlightProductDatas: List<HighlightProductData> = listOf()
    ) {
        data class HighlightProductData(
            @SerializedName("CampaignStatus")
            val campaignStatus: Int = 0,
            @SerializedName("DiscountedPercentage")
            val discountedPercentage: Int = 0,
            @SerializedName("DiscountedPrice")
            val discountedPrice: String = "",
            @SerializedName("EndDate")
            val endDate: String = "",
            @SerializedName("ID")
            val id: Long = 0,
            @SerializedName("ImageURL")
            val imageURL: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("OriginalPrice")
            val originalPrice: String = "",
            @SerializedName("Price")
            val price: String = "",
            @SerializedName("StartDate")
            val startDate: String = "",
            @SerializedName("URL")
            val url: String = ""
        )
    }
}