package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductAddResponse (

    @SerializedName("data")
    @Expose
    var data: Data? = null

)

class ProductAddV3 (

    @SerializedName("header")
    @Expose
    var header: Header? = null,
    @SerializedName("isSuccess")
    @Expose
    var isSuccess: Boolean? = null

)

class Header (

    @SerializedName("messages")
    @Expose
    var messages: List<String>? = null,
    @SerializedName("reason")
    @Expose
    var reason: String? = null,
    @SerializedName("errorCode")
    @Expose
    var errorCode: String? = null

)

data class Data (

    @SerializedName("ProductAddV3")
    @Expose
    var productAddV3: ProductAddV3? = null

)