package com.tokopedia.product.manage.filter.data.model

import com.google.gson.annotations.SerializedName

data class Filter(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("value")
        val value: String = "",
        @SerializedName("name")
        val name: String = ""
)