package com.tokopedia.layanan_finansial.view.models

import com.google.gson.annotations.SerializedName

data class LayananFinansialOuter (
        @SerializedName("ft_genie_financial_widget")
        val data: LayananFinansialModel? = null
)