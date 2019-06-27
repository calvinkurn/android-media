package com.tokopedia.transactiondata.insurance.entity.request

import com.google.gson.annotations.SerializedName

data class RemoveInsuranceProductRequest(
        @SerializedName("page")
        var page: String,

        @SerializedName("client_version")
        var clientVersion: String,

        @SerializedName("client_type")
        var clientType: String = "android",

        @SerializedName("data")
        var removeInsuranceData: ArrayList<RemoveInsuranceData>
)

data class RemoveInsuranceData(
        @SerializedName("cart_item_id")
        var cartItemId: Long,

        @SerializedName("shop_id")
        var shopId: Long,

        @SerializedName("product_id")
        var productId: Long
)