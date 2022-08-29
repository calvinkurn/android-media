package com.tokopedia.product.addedit.preview.data.model.responses

import com.google.gson.annotations.SerializedName

data class ValidateProductNameResponse (
        @SerializedName("ProductValidateV3")
        var productValidateV3: ProductValidateV3 = ProductValidateV3()
)

data class ProductValidateV3 (
        @SerializedName("header")
        var header: ProductValidateHeader = ProductValidateHeader(),
        @SerializedName("isSuccess")
        var isSuccess: Boolean = false,
        @SerializedName("data")
        var data: ProductValidateData = ProductValidateData()
)

data class ProductValidateData (
        @SerializedName("productName")
        var validationResults: List<String> = emptyList()
)

data class ProductValidateHeader (
        @SerializedName("reason")
        var reason: String = "",
        @SerializedName("errorCode")
        var errorCode: String = "",
        @SerializedName("messages")
        var messages: List<String> = emptyList()
)