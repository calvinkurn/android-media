package com.tokopedia.oneclickcheckout.order.data.checkout

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class CheckoutOccRequest(
        @SerializedName("profile")
        val profile: Profile = Profile(),
        @SerializedName("carts")
        val carts: ParamCart = ParamCart()
)

data class Profile(
        @SuppressLint("Invalid Data Type")
        @SerializedName("profile_id")
        val profileId: Int = 0
)

data class PromoRequest(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("code")
        val code: String = ""
)

data class ParamCart(
        @SerializedName("promos")
        val promos: List<PromoRequest> = emptyList(),
        @SerializedName("data")
        val data: List<ParamData> = emptyList(),
        @SerializedName("mode")
        val mode: Int = 0 // 0 = instant payment, 1 = scrooge payment page
)

data class ParamData(
        @SuppressLint("Invalid Data Type")
        @SerializedName("address_id")
        val addressId: Long = 0,
        @SerializedName("shop_products")
        val shopProducts: List<ShopProduct> = emptyList()
)

data class ShopProduct(
        @SerializedName("promos")
        val promos: List<PromoRequest> = emptyList(),
        @SuppressLint("Invalid Data Type")
        @SerializedName("shop_id")
        val shopId: Long = 0,
        @SerializedName("product_data")
        val productData: List<ProductData> = emptyList(),
        @SerializedName("is_preorder")
        val isPreorder: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        val warehouseId: Long = 0,
        @SerializedName("finsurance")
        val finsurance: Int = 0,
        @SerializedName("shipping_info")
        val shippingInfo: ShippingInfo = ShippingInfo()
)

data class ProductData(
        @SuppressLint("Invalid Data Type")
        @SerializedName("product_id")
        val productId: Long = 0,
        @SerializedName("product_quantity")
        val productQuantity: Int = 0,
        @SerializedName("product_notes")
        val productNotes: String = "",
        @SerializedName("is_ppp")
        val isPPP: Boolean = false
)

data class ShippingInfo(
        @SuppressLint("Invalid Data Type")
        @SerializedName("shipping_id")
        val shippingId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        val spId: Int = 0,
        @SerializedName("rates_id")
        val ratesId: String = "",
        @SerializedName("ut")
        val ut: String = "",
        @SerializedName("checksum")
        val checksum: String = ""
)