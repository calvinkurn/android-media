package com.tokopedia.product.manage.list.data.model.mutationeditproduct


import com.google.gson.annotations.SerializedName

data class ProductUpdateV3Data(
    @SerializedName("header")
    val header: ProductUpdateV3Header = ProductUpdateV3Header(),
    @SerializedName("isSuccess")
    val isSuccess: Boolean = false
)