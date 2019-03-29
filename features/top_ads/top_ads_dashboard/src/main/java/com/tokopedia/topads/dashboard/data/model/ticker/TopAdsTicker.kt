package com.tokopedia.topads.dashboard.data.model.ticker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopAdsTicker (
    @SerializedName("data")
    @Expose
    var data: DataMessage,

    @SerializedName("header")
    @Expose
    var header: Header,

    @SerializedName("status")
    @Expose
    var status: Status,

    @SerializedName("errors")
    @Expose
    var errors: List<Any>? = null

)
