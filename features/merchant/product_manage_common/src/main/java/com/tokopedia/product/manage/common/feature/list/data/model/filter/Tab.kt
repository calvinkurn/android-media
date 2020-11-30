package com.tokopedia.product.manage.common.feature.list.data.model.filter

import com.google.gson.annotations.SerializedName

data class Tab(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("value")
        val value: String = "",
        @SerializedName("name")
        val name: String = ""
)