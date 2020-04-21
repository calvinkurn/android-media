package com.tokopedia.topads.data.response

import com.google.gson.annotations.SerializedName

class DataSuggestions(
        @field:SerializedName("type")
        var type: String?,
        @field:SerializedName("ids")
        var ids: List<Int>?
)
