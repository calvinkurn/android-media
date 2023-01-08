package com.tokopedia.discovery2.data.productbundling


import com.google.gson.annotations.SerializedName

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
        @SerializedName("preorder_info")
        var preOrderInfo: String? = "",
        @SerializedName("min_order_wording")
        var minOrderWording: String? = "",
        @SerializedName("is_product_have_variant")
        var isProductHaveVariant: Boolean? = null,
        @SerializedName("display_price_raw")
        var displayPriceRaw: Long? = null

)