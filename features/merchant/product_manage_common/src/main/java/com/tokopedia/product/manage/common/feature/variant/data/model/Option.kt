package com.tokopedia.product.manage.common.feature.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Option (
    @Expose
    @SerializedName("unitValueID")
    val unitValueID: String,
    @Expose
    @SerializedName("value")
    val value: String,
    @Expose
    @SerializedName("hexCode")
    val hexCode: String
)