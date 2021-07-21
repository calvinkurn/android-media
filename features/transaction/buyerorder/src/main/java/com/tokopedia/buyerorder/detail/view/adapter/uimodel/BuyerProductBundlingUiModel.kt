package com.tokopedia.buyerorder.detail.view.adapter.uimodel

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class BuyerProductBundlingUiModel(
        @SerializedName("bundle_name")
        val bundleName: String = "",
        @SerializedName("order_detail")
        val productList: List<ProductBundleItem> = listOf()
)

data class ProductBundleItem(
        @SuppressLint("Invalid Data Type")
        @SerializedName("product_id")
        val productId: Long,
        @SerializedName("product_thumbnail")
        val productThumbnailUrl: String,
        @SerializedName("product_name")
        val productName: String,
        @SerializedName("product_price")
        val productPrice: Double
)