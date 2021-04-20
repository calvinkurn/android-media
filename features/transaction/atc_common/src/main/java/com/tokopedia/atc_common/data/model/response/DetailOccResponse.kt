package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetailOccResponse(
        @SerializedName("cart_id")
        @Expose
        val cartId: Long = 0,

        @SerializedName("product_id")
        @Expose
        val productId: Long = 0,

        @SerializedName("product_name")
        @Expose
        val productName: String = "",

        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,

        @SerializedName("price")
        @Expose
        val price: Long = 0,

        @SerializedName("category")
        @Expose
        val category: String = "",

        @SerializedName("category_id")
        @Expose
        val categoryId: String = "",

        @SerializedName("notes")
        @Expose
        val notes: String = "",

        @SerializedName("shop_id")
        @Expose
        val shopId: Long = 0,

        @SerializedName("shop_type")
        @Expose
        val shopType: String = "",

        @SerializedName("shop_name")
        @Expose
        val shopName: String = "",

        @SerializedName("picture")
        @Expose
        val picture: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("brand")
        @Expose
        val brand: String = "",

        @SerializedName("variant")
        @Expose
        val variant: String = "",

        @SerializedName("customer_id")
        @Expose
        val customerId: Long = 0,

        @SerializedName("warehouse_id")
        @Expose
        val warehouseId: Long = 0,

        @SerializedName("is_trade_in")
        @Expose
        val isTradeIn: Boolean = false,

        @SerializedName("is_scp")
        @Expose
        val isScp: Boolean = false,

        @SerializedName("is_multi_origin")
        @Expose
        val isMultiOrigin: Boolean = false,

        @SerializedName("is_free_ongkir")
        @Expose
        val isFreeOngkir: Boolean = false,

        @SerializedName("is_free_ongkir_extra")
        @Expose
        val isFreeOngkirExtra: Boolean = false,

        @SerializedName("tracker_attribution")
        @Expose
        val trackerAttribution: String = ""
)