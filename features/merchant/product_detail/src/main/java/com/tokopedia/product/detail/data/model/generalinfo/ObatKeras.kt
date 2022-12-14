package com.tokopedia.product.detail.data.model.generalinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ObatKeras(
    @SerializedName("applink")
    @Expose
    val applink: String = "",

    @SerializedName("subtitle")
    @Expose
    val subtitle: String = ""
)
