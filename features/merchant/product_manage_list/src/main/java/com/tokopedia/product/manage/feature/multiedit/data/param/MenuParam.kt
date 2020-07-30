package com.tokopedia.product.manage.feature.multiedit.data.param

import com.google.gson.annotations.SerializedName

data class MenuParam(
    @SerializedName("menuID")
    val menuID: String,
    @SerializedName("name")
    val name: String
)