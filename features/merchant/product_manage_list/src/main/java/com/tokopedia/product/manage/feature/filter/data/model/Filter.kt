package com.tokopedia.product.manage.feature.filter.data.model

import com.google.gson.annotations.SerializedName

data class Filter(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("value")
        val value: List<String> = listOf(),
        @SerializedName("name")
        val name: String = ""
)