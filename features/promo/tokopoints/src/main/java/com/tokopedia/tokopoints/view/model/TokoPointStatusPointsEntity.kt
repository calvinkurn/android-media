package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoPointStatusPointsEntity(
    @Expose
    @SerializedName("loyalty")
    var loyalty: Int = 0,
    @Expose
    @SerializedName("reward")
    var reward:Int = 0,
    @Expose
    @SerializedName("loyaltyExpiryInfo")
    var loyaltyExpiryInfo: String = "",
    @Expose
    @SerializedName("rewardExpiryInfo")
    var rewardExpiryInfo: String = "",
    @Expose
    @SerializedName("rewardStr")
    var rewardStr: String = "",
)
