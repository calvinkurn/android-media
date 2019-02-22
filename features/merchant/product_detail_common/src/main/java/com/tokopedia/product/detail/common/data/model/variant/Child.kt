package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 01/02/19.
 */
data class Child(

        @SerializedName("ProductID")
        @Expose
        private val productId: Int? = null, //ex: 15212348

        @SerializedName("Price")
        @Expose
        private val price: Float? = null, //ex: 100000000

        @SerializedName("PriceFmt")
        @Expose
        private val priceFmt: String? = null, //ex: Rp 100.000.000

        @SerializedName("SKU")
        @Expose
        private val sku: String? = null, // ex:gew2134

        @SerializedName("Stock")
        @Expose
        private val stock: VariantStock? = null,

        @SerializedName("OptionID")
        @Expose
        private val optionIds: List<Int>? = null,

        @SerializedName("Enabled")
        @Expose
        private val enabled: Boolean? = null,

        @SerializedName("ProductName")
        @Expose
        private val name: String? = null,

        @SerializedName("ProductURL")
        @Expose
        private val url: String? = null,

        @SerializedName("Picture")
        @Expose
        private val picture: Picture? = null,

        @SerializedName("CampaignInfo")
        @Expose
        private val campaign: Campaign? = null,

        @SerializedName("IsWishlist")
        @Expose
        private val isWishlist: Boolean? = null,

        @SerializedName("IsCOD")
        @Expose
        private val isCod: Boolean? = false
)

data class Campaign(

        @SerializedName("CampaignID")
        @Expose
        private val campaignID: String? = "",

        @SerializedName("IsActive")
        @Expose
        private val isActive: Boolean? = null,

        @SerializedName("OriginalPrice")
        @Expose
        private val originalPrice: Float? = null,

        @SerializedName("OriginalPriceFmt")
        @Expose
        private val originalPriceFmt: String? = null,

        @SerializedName("DiscountPercentage")
        @Expose
        private val discountedPercentage: Float? = 0f,

        @SerializedName("DiscountPrice")
        @Expose
        private val discountedPrice: Float? = 0f,

        @SerializedName("DiscountPriceFmt")
        @Expose
        private val discountedPriceFmt: String? = null,

        @SerializedName("CampaignType")
        @Expose
        private val campaignType: Int? = null,

        @SerializedName("CampaignTypeName")
        @Expose
        private val campaignTypeName: String? = null,

        @SerializedName("StartDate")
        @Expose
        private val startDate: String? = null,

        @SerializedName("EndDate")
        @Expose
        private val endDate: String? = null,

        @SerializedName("Stock")
        @Expose
        private val stock: Int? = null,

        @SerializedName("IsAppsOnly")
        @Expose
        private val isAppsOnly: Boolean? = null,

        @SerializedName("AppLinks")
        @Expose
        private val applinks: String? = null
)


data class VariantStock(
        @SerializedName("Stock")
        @Expose
        private val stock: Int? = 0,

        @SerializedName("IsBuyable")
        @Expose
        private val isBuyable: Boolean? = false,

        @SerializedName("AlwaysAvailable")
        @Expose
        private val alwaysAvailable: Boolean? = false,

        @SerializedName("IsLimitedStock")
        @Expose
        private val isLimitedStock: Boolean? = false,

        @SerializedName("StockWording")
        @Expose
        private val stockWording: String? = "",

        @SerializedName("StockWordingHTML")
        @Expose
        private val stockWordingHTML: String? = "",

        @SerializedName("OtherVariantStock")
        @Expose
        private val otherVariantStock: String? = "",

        @SerializedName("MinimumOrder")
        @Expose
        private val minimumOrder: Int? = 0,

        @SerializedName("MaximumOrder")
        @Expose
        private val maximumOrder: Int? = 0
)