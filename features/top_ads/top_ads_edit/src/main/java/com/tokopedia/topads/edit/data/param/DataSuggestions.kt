package com.tokopedia.topads.edit.data.param

import com.google.gson.annotations.SerializedName

/**
 * Created by Pika on 13/4/20.
 */
class DataSuggestions(
        @field:SerializedName("type")
        var type: String?,
        @field:SerializedName("ids")
        var ids: List<Int>?
)
