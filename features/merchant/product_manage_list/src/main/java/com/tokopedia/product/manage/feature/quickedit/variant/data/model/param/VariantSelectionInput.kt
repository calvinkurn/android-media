package com.tokopedia.product.manage.feature.quickedit.variant.data.model.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Option

class VariantSelectionInput(
    @SerializedName("variantID")
    val id: String,
    @SerializedName("unitID")
    val unitID: String,
    @SerializedName("options")
    val options: List<Option>
)