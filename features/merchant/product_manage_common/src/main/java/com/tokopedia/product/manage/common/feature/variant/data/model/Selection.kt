package com.tokopedia.product.manage.common.feature.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Selection (
    @Expose
    @SerializedName("variantID")
    val variantID: String,
    @Expose
    @SerializedName("variantName")
    val variantName: String,
    @Expose
    @SerializedName("unitName")
    val unitName: String,
    @Expose
    @SerializedName("unitID")
    val unitID: String,
    @Expose
    @SerializedName("identifier")
    val identifier: String,
    @Expose
    @SerializedName("options")
    val options: List<Option>
)