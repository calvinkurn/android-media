package com.tokopedia.oneclickcheckout.order.data.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckoutOccRequest(
        @SerializedName("profile")
        @Expose
        val profile: Profile = Profile(),
        @SerializedName("carts")
        @Expose
        val carts: ParamCart = ParamCart()
)

data class Profile(
        @SerializedName("profile_id")
        @Expose
        val profileId: Int = 0
)

data class PromoRequest(
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("code")
        @Expose
        val code: String = ""
)

data class ParamCart(
        @SerializedName("promos")
        @Expose
        val promos: List<PromoRequest> = emptyList(),
        @SerializedName("data")
        @Expose
        val data: List<ParamData> = emptyList()
)

data class ParamData(
        @SerializedName("address_id")
        @Expose
        val addressId: Int = 0,
        @SerializedName("shop_products")
        @Expose
        val shopProducts: List<ShopProduct> = emptyList()
)

data class ShopProduct(
        @SerializedName("promos")
        @Expose
        val promos: List<PromoRequest> = emptyList(),
        @SerializedName("shop_id")
        @Expose
        val shopId: Int = 0,
        @SerializedName("product_data")
        @Expose
        val productData: List<ProductData> = emptyList(),
        @SerializedName("is_preorder")
        @Expose
        val isPreorder: Int = 0,
        @SerializedName("warehouse_id")
        @Expose
        val warehouseId: Int = 0,
        @SerializedName("finsurance")
        @Expose
        val finsurance: Int = 0,
        @SerializedName("shipping_info")
        @Expose
        val shippingInfo: ShippingInfo = ShippingInfo()
)

data class ProductData(
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SerializedName("product_quantity")
        @Expose
        val productQuantity: Int = 0,
        @SerializedName("product_notes")
        @Expose
        val productNotes: String = ""
)

data class ShippingInfo(
        @SerializedName("shipping_id")
        @Expose
        val shippingId: Int = 0,
        @SerializedName("sp_id")
        @Expose
        val spId: Int = 0,
        @SerializedName("rates_id")
        @Expose
        val ratesId: String = "",
        @SerializedName("ut")
        @Expose
        val ut: String = "",
        @SerializedName("checksum")
        @Expose
        val checksum: String = ""
)