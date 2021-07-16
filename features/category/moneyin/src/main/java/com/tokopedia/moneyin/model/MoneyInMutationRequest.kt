package com.tokopedia.moneyin.model


import com.google.gson.annotations.SerializedName

data class MoneyInMutationRequest(
    @SerializedName("carts")
    val carts: ArrayList<Cart>
) {
    data class Cart(
            @SerializedName("business_type")
        val businessType: Int,
            @SerializedName("cart_info")
        val cartInfo: ArrayList<CartInfo>,
            @SerializedName("meta_data")
        val metaData: String?
    ) {
        data class CartInfo(
                @SerializedName("data_id")
            val dataId: Int,
                @SerializedName("data_type")
            val dataType: String,
                @SerializedName("fields")
            val fields: ArrayList<Field>,
                @SerializedName("quantity")
            val quantity: Int
        ) {
            data class Field(
                @SerializedName("field_name")
                val fieldName: String,
                @SerializedName("value_num")
                val valueNum: Int
            )
        }
    }
}
