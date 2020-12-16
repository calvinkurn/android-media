package com.tokopedia.product.manage.common.feature.variant.data.model.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.common.feature.variant.data.model.Option

data class VariantSelectionInput(
    @Expose
    @SerializedName("variantID")
    val id: String,
    @Expose
    @SerializedName("unitID")
    val unitID: String,
    @Expose
    @SerializedName("options")
    val options: List<Option>
)