package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateProductNameExistResponse (
        @SerializedName("ProductValidateV3")
        @Expose
        var productValidateV3: ProductValidateV3 = ProductValidateV3()
)

data class ProductValidateV3 (
        @SerializedName("header")
        @Expose
        var header: ProductValidateHeader = ProductValidateHeader(),
        @SerializedName("isSuccess")
        @Expose
        var isSuccess: Boolean = false,
        @SerializedName("data")
        @Expose
        var data: ProductValidateData = ProductValidateData()
)

class ProductValidateData {
    @SerializedName("productName")
    @Expose
    var productName: List<String>? = null
}

data class ProductValidateHeader (
        @SerializedName("reason")
        @Expose
        var reason: String = "",
        @SerializedName("errorCode")
        @Expose
        var errorCode: String = "",
        @SerializedName("messages")
        @Expose
        var messages: List<String> = emptyList()
)