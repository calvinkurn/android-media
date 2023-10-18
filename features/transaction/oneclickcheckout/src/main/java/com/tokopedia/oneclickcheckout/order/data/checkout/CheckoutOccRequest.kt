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
    val mode: Int = 0, // 0 = instant payment, 1 = scrooge payment page
    @SerializedName("feature_type")
    val featureType: Int = FEATURE_TYPE_OCC_MULTI_NON_TOKONOW
) {
    companion object {
        const val FEATURE_TYPE_OCC_MULTI_NON_TOKONOW = 1
        const val FEATURE_TYPE_TOKONOW = 11
    }
}

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
    val shippingInfo: ShippingInfo = ShippingInfo(),
    @SerializedName("items")
    val items: List<AddOnItem> = emptyList(),
    @SerializedName("order_metadata")
    val orderMetadata: List<OrderMetadata> = emptyList()
)

data class AddOnItem(
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("item_id")
    val itemId: String = "",
    @SerializedName("item_qty")
    val itemQty: Long = 0,
    @SerializedName("item_metadata")
    val itemMetadata: String = "",
    @SerializedName("item_unique_id")
    val itemUniqueId: String = ""
)

data class ProductData(
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("product_quantity")
    val productQuantity: Int = 0,
    @SerializedName("product_notes")
    val productNotes: String = "",
    @SerializedName("is_ppp")
    val isPPP: Boolean = false,
    @SerializedName("items")
    val items: List<AddOnItem> = emptyList()
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

data class OrderMetadata(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: String = ""
) {
    companion object {
        const val FREE_SHIPPING_METADATA = "free_shipping_metadata"
        const val PRESCRIPTION_IDS_METADATA = "prescription_ids"
    }
}
