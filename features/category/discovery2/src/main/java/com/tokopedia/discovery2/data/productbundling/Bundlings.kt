package com.tokopedia.discovery2.data.productbundling


import com.google.gson.annotations.SerializedName

data class Bundlings(
        @SerializedName("widget_id")
        var widgetId: Long? = null,
        @SerializedName("widget_master_id")
        var widgetMasterId: Long? = null,
        @SerializedName("name")
        var name: String? = "",
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("header")
        var header: BundlingHeader? = null,
        @SerializedName("bundling_data")
        var bundlingData: List<BundlingData?>? = null
)

data class BundlingData(

        @SerializedName("bundle_group_id")
        var bundleGroupId: Long? = null,
        @SerializedName("bundle_name")
        var bundleName: String? = "",
        @SerializedName("bundle_details")
        var bundleDetails: List<BundleDetails?>? = null,
        @SerializedName("bundle_products")
        var bundleProducts: List<BundleProducts?>? = null

)

data class BundleProducts(

        @SerializedName("product_id")
        var productId: Long? = null,
        @SerializedName("product_name")
        var productName: String? = "",
        @SerializedName("image_url")
        var imageUrl: String? = "",
        @SerializedName("applink")
        var applink: String? = ""

)

data class BundleDetails(

        @SerializedName("bundle_id")
        var bundleId: Long? = null,
        @SerializedName("original_price")
        var originalPrice: String? = "",
        @SerializedName("display_price")
        var displayPrice: String? = "",
        @SerializedName("discount_percentage")
        var discountPercentage: Double? = null,
        @SerializedName("saving_amount_wording")
        var savingAmountWording: String? = "",
        @SerializedName("min_order")
        var minOrder: Int? = null,
        @SerializedName("is_po")
        var preOrder: Boolean? = null,
        @SerializedName("display_price_raw")
        var displayPriceRaw: Long? = null

)

data class BundlingHeader(

        @SerializedName("title")
        var title: String? = "",
        @SerializedName("is_active")
        var isActive: Boolean? = null,
        @SerializedName("size_option")
        var sizeOption: String? = ""

)