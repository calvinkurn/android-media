package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AllVariant (
    @SerializedName("VariantID")
    @Expose
    val variantId: Int = 0,

    @SerializedName("Name")
    @Expose
    val variantName: String = ""
)