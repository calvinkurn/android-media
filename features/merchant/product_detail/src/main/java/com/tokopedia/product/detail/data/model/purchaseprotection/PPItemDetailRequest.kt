package com.tokopedia.product.detail.data.model.purchaseprotection

import com.google.gson.annotations.SerializedName

data class PPItemDetailRequest(

        @SerializedName("productID")
        var productId: Int = 0,

        @SerializedName("shopID")
        var shopId: Int = 0,

        @SerializedName("userID")
        var userId: String? = null,

        @SerializedName("condition")
        var condition: String? = null,

        @SerializedName("productTitle")
        var productTitle: String? = null,

        @SerializedName("price")
        var price: Int = 0
)
