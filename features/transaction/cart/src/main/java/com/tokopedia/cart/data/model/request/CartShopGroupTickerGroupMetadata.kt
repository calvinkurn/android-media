package com.tokopedia.cart.data.model.request

import com.google.gson.annotations.SerializedName

data class CartShopGroupTickerGroupMetadata(
    @SerializedName("order_data")
    val listOrderData: ArrayList<OrderData> = arrayListOf()
) {
    data class OrderData(
        @SerializedName("order_metadata")
        val orderMetadata: String = "",
        @SerializedName("unique_id")
        val uniqueId: String = "",
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("warehouse_id")
        val warehouseId: String = "",
        @SerializedName("group_product")
        val listGroupProduct: ArrayList<GroupProduct> = arrayListOf()
    ) {
        data class Shop(
            @SerializedName("shop_id")
            val shopId: String = ""
        )

        data class GroupProduct(
            @SerializedName("group_qty")
            val groupQty: Int = 0,
            @SerializedName("products")
            val listProduct: ArrayList<Product> = arrayListOf()
        ) {
            data class Product(
                @SerializedName("unique_id")
                val uniqueId: String = "",
                @SerializedName("product_id")
                val productId: String = "",
                @SerializedName("quantity")
                val quantity: Int = 0,
                @SerializedName("product_metadata")
                val productMetadata: String = ""
            )
        }
    }
}
