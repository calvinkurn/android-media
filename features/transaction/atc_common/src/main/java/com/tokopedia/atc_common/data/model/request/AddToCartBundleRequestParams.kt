package com.tokopedia.atc_common.data.model.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class AddToCartBundleRequestParams(
        @SerializedName("shop_id")
        var shopId: String = "",
        @SerializedName("bundle_id")
        var bundleId: String = "",
        @SerializedName("bundle_qty")
        var bundleQty: Int = 0,
        @SerializedName("selected_product_pdp")
        var selectedProductPdp: String = "",
        @SerializedName("product_detail")
        var productDetails: List<ProductDetail> = emptyList()
)

data class ProductDetail(
        @SerializedName("product_id")
        var productId: String = "",
        @SerializedName("quantity")
        var quantity: Int = 0,
        @SerializedName("notes")
        var notes: String = "",
        @SerializedName("shop_id")
        val shopId: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        val warehouseId: BigInteger = 0.toBigInteger(),
        @SerializedName("is_product_parent")
        val isProductParent: Boolean = false,
        @SerializedName("customer_id")
        val customerId: String = ""
)