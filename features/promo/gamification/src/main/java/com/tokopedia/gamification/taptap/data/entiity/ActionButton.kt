package com.tokopedia.gamification.taptap.data.entiity

import com.google.gson.annotations.SerializedName

data class ActionButton(
        @SerializedName("applink")
        val applink: String,

        @SerializedName("backgroundColor")
        val backgroundColor: String,

        @SerializedName("isDisable")
        val isDisable: Boolean,

        @SerializedName("text")
        val text: String,

        @SerializedName("type")
        val type: String,

        @SerializedName("url")
        val url: String

)