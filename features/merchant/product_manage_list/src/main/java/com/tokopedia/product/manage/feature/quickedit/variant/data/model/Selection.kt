package com.tokopedia.product.manage.feature.quickedit.variant.data.model

import com.google.gson.annotations.SerializedName

data class Selection (
    @SerializedName("variantName")
    val variantName: String,
    @SerializedName("unitName")
    val unitName: String,
    @SerializedName("unitID")
    val unitID: String,
    @SerializedName("identifier")
    val identifier: String,
    @SerializedName("options")
    val options: List<Option>
)