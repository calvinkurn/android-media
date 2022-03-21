package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cashback(
        @SerializedName("percentage")
        @Expose
        val percentage: Int = 0
)