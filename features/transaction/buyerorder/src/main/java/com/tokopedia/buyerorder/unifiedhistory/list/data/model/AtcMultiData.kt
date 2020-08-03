package com.tokopedia.buyerorder.unifiedhistory.list.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 03/08/20.
 */
data class AtcMultiData (
        @SerializedName("add_to_cart_multi")
        val atcMulti: AtcMulti = AtcMulti()
) {
    data class AtcMulti(
            @SerializedName("error_message")
            val errorMessage: String = "",

            @SerializedName("status")
            val status: String = "",

            @SerializedName("data")
            val buyAgainData: BuyAgainData = BuyAgainData()
    ) {
        data class BuyAgainData(
                @SerializedName("success")
                val success: Int = -1,

                @SerializedName("messages")
                val message: List<String> = listOf(),

                @SerializedName("data")
                val listProducts: List<AtcProduct> = listOf()
        ) {
            data class AtcProduct(
                    @SerializedName("cart_id")
                    val cartId: Int = -1,

                    @SerializedName("product_id")
                    val productId: Int = -1,

                    @SerializedName("quantity")
                    val quantity: Int = -1,

                    @SerializedName("notes")
                    val notes: String = "",

                    @SerializedName("shop_id")
                    val shopId: Int = -1,

                    @SerializedName("customer_id")
                    val customerId: Int = -1
            )
        }
    }
}