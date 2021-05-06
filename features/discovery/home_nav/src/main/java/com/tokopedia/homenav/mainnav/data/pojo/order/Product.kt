package com.tokopedia.homenav.mainnav.data.pojo.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("imageURL")
        @Expose
        val imageURL: String? = "",
        @SerializedName("inline1")
        @Expose
        val inline1: Inline1? = Inline1(),
        @SerializedName("inline2")
        @Expose
        val inline2: Inline2? = Inline2(),
        @SerializedName("title")
        @Expose
        val title: String? = ""
)