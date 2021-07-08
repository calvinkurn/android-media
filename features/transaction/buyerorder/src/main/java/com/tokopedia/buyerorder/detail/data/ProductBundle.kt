package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName

data class ProductBundle(
        @SerializedName("bundle_name")
        val bundleName: String,
        @SerializedName("bundle_items")
        val productList: List<ProductBundleItem>
)

data class ProductBundleItem(
        @SerializedName("product_thumbnail")
        val productThumbnailUrl: String,
        @SerializedName("product_name")
        val productName: String,
        @SerializedName("price_text")
        val priceText: String
)