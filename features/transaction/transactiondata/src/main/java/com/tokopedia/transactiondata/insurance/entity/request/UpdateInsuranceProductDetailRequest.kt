package com.tokopedia.transactiondata.insurance.entity.request

import com.google.gson.annotations.SerializedName

data class UpdateInsuranceProductDetailRequest(
        @SerializedName("page")
        var page: String = "",

        @SerializedName("client_version")
        var clientVersion: String = "",

        @SerializedName("client_type")
        var clientType: String = "android",

        @SerializedName("data")
        var removeInsuranceData: ArrayList<UpdateInsuranceData> = ArrayList()
)

data class UpdateInsuranceData(
        @SerializedName("shop_id")
        var shopId: Long,

        @SerializedName("items")
        var items: ArrayList<UpdateInsuranceProductItems>
)

data class UpdateInsuranceProductItems(
        @SerializedName("product_id")
        var productId: Long,

        @SerializedName("quantity")
        var quantity: Int,

        @SerializedName("digital_product")
        var digitalProductList: ArrayList<UpdateInsuranceProduct>
)

data class UpdateInsuranceProduct(
        @SerializedName("digital_product_id")
        var digitalProductId: Long,

        @SerializedName("cart_item_id")
        var cartItemId: Long,

        @SerializedName("type_id")
        var typeId: Long,

        @SerializedName("application_details")
        var applicationDetails: ArrayList<UpdateInsuranceProductApplicationDetails>
)

data class UpdateInsuranceProductApplicationDetails(
        @SerializedName("id")
        var id: Int,

        @SerializedName("value")
        var value: String
)

data class UpdateInsuranceDataCart(
        @SerializedName("cart_id")
        var cartId: String,

        @SerializedName("quantity")
        var quantity: Int = 1
)