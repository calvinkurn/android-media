package com.tokopedia.discovery2

import com.google.gson.annotations.SerializedName

data class StockWording(

        @SerializedName("title")
        var title: String? = "",

        @SerializedName("color")
        var color: String? = ""

)