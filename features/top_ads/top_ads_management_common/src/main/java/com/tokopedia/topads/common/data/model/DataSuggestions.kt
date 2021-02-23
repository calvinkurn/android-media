package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Pika on 9/10/20.
 */
class DataSuggestions(
        @field:SerializedName("type")
        var type: String?,
        @field:SerializedName("ids")
        var ids: List<Long>?
)
