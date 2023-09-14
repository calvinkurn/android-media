package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MultiCheckoutButtons (
    @SerializedName("text")
    @Expose
    val text: String = "",
    @SerializedName("color")
    @Expose
    val color: String = "",
    @SerializedName("coachmark")
    @Expose
    val coachmark: String = "",
    @SerializedName("position")
    @Expose
    val position: String = "",
    @SerializedName("type")
    @Expose
    val type: String = "",
)
