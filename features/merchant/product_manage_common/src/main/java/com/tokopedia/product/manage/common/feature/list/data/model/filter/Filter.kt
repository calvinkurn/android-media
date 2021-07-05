package com.tokopedia.product.manage.common.feature.list.data.model.filter

import com.google.gson.annotations.SerializedName

data class Filter(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("value")
        val value: List<String> = listOf(),
        @SerializedName("name")
        val name: String = ""
)