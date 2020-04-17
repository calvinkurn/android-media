package com.tokopedia.product.manage.feature.quickedit.variant.data.model

import com.google.gson.annotations.SerializedName

data class Option (
    @SerializedName("unitValueID")
    val unitValueID: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("hexCode")
    val hexCode: String
)