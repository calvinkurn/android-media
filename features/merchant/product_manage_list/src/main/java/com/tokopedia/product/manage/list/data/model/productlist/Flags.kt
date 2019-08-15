package com.tokopedia.product.manage.list.data.model.productlist


import com.google.gson.annotations.SerializedName

data class Flags(
    @SerializedName("isFeatured")
    val isFeatured: Boolean = false,
    @SerializedName("isFreereturn")
    val isFreereturn: Boolean = false,
    @SerializedName("isPreorder")
    val isPreorder: Boolean = false,
    @SerializedName("isVariant")
    val isVariant: Boolean = false,
    @SerializedName("withStock")
    val withStock: Boolean = false
)