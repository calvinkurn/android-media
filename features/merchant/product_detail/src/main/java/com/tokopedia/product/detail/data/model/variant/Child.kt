package com.tokopedia.product.detail.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 01/02/19.
 */
data class Child(

        @SerializedName("product_id")
        @Expose
        private val productId: Int? = null,
        @SerializedName("price")
        @Expose
        private val price: Int? = null,
        @SerializedName("stock")
        @Expose
        private val stock: Int? = null,
        @SerializedName("min_order")
        @Expose
        private val minOrder: Int? = null,
        @SerializedName("max_order")
        @Expose
        private val maxOrder: Int? = null,
        @SerializedName("sku")
        @Expose
        private val sku: String? = null,
        @SerializedName("option_ids")
        @Expose
        private val optionIds: List<Int>? = null,
        @SerializedName("enabled")
        @Expose
        private val enabled: Boolean? = null,
        @SerializedName("name")
        @Expose
        private val name: String? = null,
        @SerializedName("url")
        @Expose
        private val url: String? = null,
        @SerializedName("is_buyable")
        @Expose
        private val isBuyable: Boolean? = null,
        @SerializedName("picture")
        @Expose
        private val picture: Picture? = null,
        @SerializedName("price_fmt")
        @Expose
        private val priceFmt: String? = null,
        @SerializedName("campaign")
        @Expose
        private val campaign: Campaign? = null,
        @SerializedName("is_wishlist")
        @Expose
        private val isWishlist: Boolean? = null,
        @SerializedName("always_available")
        @Expose
        private val alwaysAvailable: Boolean? = null,
        @SerializedName("stock_wording")
        @Expose
        private val stockWording: String? = null,
        @SerializedName("stock_wording_html")
        @Expose
        private val stockWordingHtml: String? = null,
        @SerializedName("other_variant_stock")
        @Expose
        private val otherVariantStock: String? = null,
        @SerializedName("is_limited_stock")
        @Expose
        private val isLimitedStock: Boolean? = null,
        @SerializedName("is_cod")
        @Expose
        private val isCod: Boolean? = null
)

data class Campaign(
        @SerializedName("is_active")
        @Expose
        private val isActive: Boolean? = null,
        @SerializedName("original_price")
        @Expose
        private val originalPrice: Int? = null,
        @SerializedName("original_price_fmt")
        @Expose
        private val originalPriceFmt: String? = null,
        @SerializedName("discounted_percentage")
        @Expose
        private val discountedPercentage: Int? = null,
        @SerializedName("discounted_price")
        @Expose
        private val discountedPrice: Int? = null,
        @SerializedName("discounted_price_fmt")
        @Expose
        private val discountedPriceFmt: String? = null,
        @SerializedName("campaign_type")
        @Expose
        private val campaignType: Int? = null,
        @SerializedName("campaign_type_name")
        @Expose
        private val campaignTypeName: String? = null,
        @SerializedName("campaign_short_name")
        @Expose
        private val campaignShortName: String? = null,
        @SerializedName("start_date")
        @Expose
        private val startDate: String? = null,
        @SerializedName("end_date")
        @Expose
        private val endDate: String? = null,
        @SerializedName("end_date_unix")
        @Expose
        private val endDateUnix: Int? = null,
        @SerializedName("stock")
        @Expose
        private val stock: Int? = null,
        @SerializedName("apps_only")
        @Expose
        private val appsOnly: Boolean? = null,
        @SerializedName("applinks")
        @Expose
        private val applinks: String? = null
)