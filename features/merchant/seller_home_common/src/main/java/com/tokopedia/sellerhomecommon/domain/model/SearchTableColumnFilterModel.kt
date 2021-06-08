package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchTableColumnFilterModel(
        @Expose
        @SerializedName("name")
        val name: String? = "",
        @Expose
        @SerializedName("value")
        val value: String? = ""
)