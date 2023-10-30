package com.tokopedia.epharmacy.network.request

import com.google.gson.annotations.SerializedName

data class EPharmacyUpdateCartParam(
    @SerializedName("input")
    val input: Input?
) {
    data class Input(
        @SerializedName("tokoConsultationID")
        val tokoConsultationID: Long,
        @SerializedName("productList")
        val productList: List<CartProduct?>?
    ) {
        data class CartProduct(
            @SerializedName("cartID")
            val cartID: Long,
            @SerializedName("quantity")
            val quantity: Int
        )
    }
}
