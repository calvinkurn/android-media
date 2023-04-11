package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LuckyEggEntity(
    @Expose @SerializedName("resultStatus")
    var resultStatus: ResultStatusEntity = ResultStatusEntity(),
    @Expose
    @SerializedName("offFlag")
    var offFlag:Boolean = false,
        @Expose
    @SerializedName("sumToken")
    var sumToken:Int = 0,
        @Expose
    @SerializedName("sumTokenStr")
    var sumTokenStr: String = "",
        @Expose
    @SerializedName("floating")
    var floating: LuckyEggFloatingEntity = LuckyEggFloatingEntity(),
)
