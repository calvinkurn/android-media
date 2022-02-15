package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class ProgressInfoList(
    @SerializedName("currentAmount")
    val currentAmount: Int? = 0,

    @SerializedName("nextAmount")
    val nextAmount: Int? = 0,

    @SerializedName("currentAmountStr")
    val currentAmountStr: String? = "",

    @SerializedName("nextAmountStr")
    val nextAmountStr: String? = "",

    @SerializedName("iconImageURL")
    val iconImageURL: String? = "",

    @SerializedName("tierID")
    val tierID: Int? = 0,

    @SerializedName("tierLevel")
    val tierLevel: Int? = 0,

    @SerializedName("currentTierName")
    val currentTierName: String? = "",

    @SerializedName("currentTierNameDesc")
    val currentTierNameDesc: String? = "",

    @SerializedName("nextTierName")
    val nextTierName: String? = "",

    @SerializedName("nextTierNameDesc")
    val nextTierNameDesc: String? = "",

    @SerializedName("nextTierIconImageURL")
    val nextTierIconImageURL: String? = "",

    var isNextTier:Boolean = false

)