package com.tokopedia.product.manage.feature.filter.data.model

import com.google.gson.annotations.SerializedName

data class Sort(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("value")
        val value: String = "",
        @SerializedName("name")
        val name: String = ""
)