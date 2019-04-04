package com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform

import com.google.gson.annotations.SerializedName

data class RelatedItem(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("title")
        val title: String = ""
)