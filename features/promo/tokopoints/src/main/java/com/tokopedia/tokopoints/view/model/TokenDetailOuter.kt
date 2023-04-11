package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenDetailOuter(
    @Expose
    @SerializedName("luckyegg")
    var tokenDetail: LuckyEggEntity = LuckyEggEntity()
)
