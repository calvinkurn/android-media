package com.tokopedia.product.manage.common.feature.list.data.model.filter

import com.google.gson.annotations.SerializedName

data class Sort(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("value")
        val value: String = ""
)