package com.tokopedia.hotlist.data.hotListDetail

import com.google.gson.annotations.SerializedName

data class QueriesItem(

        @field:SerializedName("filterKey")
        val filterKey: String? = null,

        @field:SerializedName("filterValue")
        val filterValue: String? = null
)