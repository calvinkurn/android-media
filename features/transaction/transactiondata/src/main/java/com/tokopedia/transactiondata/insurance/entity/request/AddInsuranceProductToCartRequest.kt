package com.tokopedia.transactiondata.insurance.entity.request

import com.google.gson.annotations.SerializedName

data class AddInsuranceProductToCartRequest(
        @SerializedName("page")
        var page: String = "",

        @SerializedName("client_version")
        var clientVersion: String = "",

        @SerializedName("client_type")
        var clientType: String = "android",

        @SerializedName("client_language")
        var clientLanguage: String = "",

        @SerializedName("data")
        var addInsuranceData: ArrayList<AddInsuranceProductData> = ArrayList()
)

data class AddInsuranceProductData(
        @SerializedName("shop_id")
        var shopId: Long = 0,

        @SerializedName("items")
        var shopItems: List<AddInsuranceProductItems> = emptyList()
)

data class AddInsuranceProductItems(
        @SerializedName("product_id")
        var productId: Long = 0,

        @SerializedName("quantity")
        var productQuantity: Int = 0,

        @SerializedName("digital_product")
        var digitalProductList: ArrayList<AddInsuranceProduct> = ArrayList()
)

data class AddInsuranceProduct(
        @SerializedName("digital_product_id")
        var digitalProductId: Long = 0,

        @SerializedName("type_id")
        var typeId: Long = 0,

        @SerializedName("application_details")
        var applicationDetails: ArrayList<AddInsuranceProductApplicationDetails> = ArrayList()
)

data class AddInsuranceProductApplicationDetails(
        @SerializedName("id")
        var id: Int = 0,

        @SerializedName("value")
        var value: String = ""
)