package com.tokopedia.transactiondata.insurance.entity.request

import com.google.gson.annotations.SerializedName

data class AddMarketPlaceToCartRequest(

        @SerializedName("productID")
        var productId: Long = 0,

        @SerializedName("shopID")
        var shoppId: Long = 0,

        @SerializedName("warehouseID")
        var warehouseID: Long = 0,

        @SerializedName("quantity")
        var quantity: Int = 0,

        @SerializedName("notes")
        var notes: String = ""

)