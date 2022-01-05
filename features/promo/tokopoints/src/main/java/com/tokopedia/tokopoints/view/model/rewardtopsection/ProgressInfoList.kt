package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class ProgressInfoList(
    @SerializedName("currentAmount")
    val currentAmount: Int? = null,

    @SerializedName("iconImageURL")
    val iconImageURL: String? = null,

    @SerializedName("tierID")
    val tierID: Int? = null,

    @SerializedName("tierLevel")
    val tierLevel: Int? = null,
)