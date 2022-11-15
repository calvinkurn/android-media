package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CountdownAttr(
    @SerializedName("showTimer")
    @Expose
    var isShowTimer: Boolean = false,
    @SerializedName("expiredCountDown")
    @Expose
    var expiredCountDown: Long = 0,
)

