package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 01/02/19.
 */
data class Child(

        @SerializedName("ProductID")
        @Expose
        val productId: Int? = null, //ex: 15212348

        @SerializedName("Price")
        @Expose
        val price: Float? = null, //ex: 100000000

        @SerializedName("PriceFmt")
        @Expose
        val priceFmt: String? = null, //ex: Rp 100.000.000

        @SerializedName("SKU")
        @Expose
        val sku: String? = null, // ex:gew2134

        @SerializedName("Stock")
        @Expose
        val stock: VariantStock? = null,

        @SerializedName("OptionID")
        @Expose
        val optionIds: List<Int> = listOf(),

        @SerializedName("Enabled")
        @Expose
        val enabled: Boolean? = null,

        @SerializedName("ProductName")
        @Expose
        val name: String? = null,

        @SerializedName("ProductURL")
        @Expose
        val url: String? = null,

        @SerializedName("Picture")
        @Expose
        val picture: Picture? = null,

        @SerializedName("CampaignInfo")
        @Expose
        val campaign: Campaign? = null,

        @SerializedName("IsWishlist")
        @Expose
        val isWishlist: Boolean? = null,

        @SerializedName("IsCOD")
        @Expose
        val isCod: Boolean? = false
) {
    val isBuyable: Boolean
        get() = stock?.isBuyable ?: false

    val hasPicture: Boolean
        get() = picture != null &&
                (picture.original?.isNotEmpty() == true
                        || picture.thumbnail?.isNotEmpty() == true)

}

data class Campaign(

        @SerializedName("CampaignID")
        @Expose
        val campaignID: String? = "",

        @SerializedName("IsActive")
        @Expose
        val isActive: Boolean? = null,

        @SerializedName("OriginalPrice")
        @Expose
        val originalPrice: Float? = null,

        @SerializedName("OriginalPriceFmt")
        @Expose
        val originalPriceFmt: String? = null,

        @SerializedName("DiscountPercentage")
        @Expose
        val discountedPercentage: Float? = 0f,

        @SerializedName("DiscountPrice")
        @Expose
        val discountedPrice: Float? = 0f,

        @SerializedName("DiscountPriceFmt")
        @Expose
        val discountedPriceFmt: String? = null,

        @SerializedName("CampaignType")
        @Expose
        val campaignType: Int? = null,

        @SerializedName("CampaignTypeName")
        @Expose
        val campaignTypeName: String? = null,

        @SerializedName("StartDate")
        @Expose
        val startDate: String? = null,

        @SerializedName("EndDate")
        @Expose
        val endDate: String? = null,

        @SerializedName("Stock")
        @Expose
        val stock: Int? = null,

        @SerializedName("IsAppsOnly")
        @Expose
        val isAppsOnly: Boolean? = null,

        @SerializedName("AppLinks")
        @Expose
        val applinks: String? = null
) {
    val activeAndHasId: Boolean
        get() = isActive == true && (campaignID?.isNotEmpty() == true)
}


data class VariantStock(
        @SerializedName("Stock")
        @Expose
        val stock: Int? = 0,

        @SerializedName("IsBuyable")
        @Expose
        val isBuyable: Boolean? = false,

        @SerializedName("AlwaysAvailable")
        @Expose
        val alwaysAvailable: Boolean? = false,

        @SerializedName("IsLimitedStock")
        @Expose
        val isLimitedStock: Boolean? = false,

        @SerializedName("StockWording")
        @Expose
        val stockWording: String? = "",

        @SerializedName("StockWordingHTML")
        @Expose
        val stockWordingHTML: String? = "",

        @SerializedName("OtherVariantStock")
        @Expose
        val otherVariantStock: String? = "",

        @SerializedName("MinimumOrder")
        @Expose
        val minimumOrder: Int? = 0,

        @SerializedName("MaximumOrder")
        @Expose
        val maximumOrder: Int? = 0
)