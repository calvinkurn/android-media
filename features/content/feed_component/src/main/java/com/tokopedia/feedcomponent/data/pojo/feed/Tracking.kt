package com.tokopedia.feedcomponent.data.pojo.feed

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.Data

data class Tracking (
        @SerializedName("id")
    var id: String = "",
        @SerializedName("topads")
        val topads: MutableList<Data> = ArrayList(),
        @SerializedName("type")
    var type: String = ""
)