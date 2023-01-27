package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class CPL(
    @SerializedName("shipperServices")
    val shipperServices: List<Long> = listOf()
)
