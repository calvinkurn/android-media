package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Cashback(
        @SerializedName("percentage")
        val percentage: Int = 0
)