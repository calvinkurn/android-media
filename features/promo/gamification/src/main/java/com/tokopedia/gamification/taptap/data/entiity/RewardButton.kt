package com.tokopedia.gamification.taptap.data.entiity

import com.google.gson.annotations.SerializedName

data class RewardButton(
        @SerializedName("applink")
        val applink: String? = null,

        @SerializedName("backgroundColor")
        val backgroundColor: String? = null,

        @SerializedName("isDisable")
        val isDisable: Boolean = false,

        @SerializedName("text")
        val text: String? = null,

        @SerializedName("type")
        val type: String? = null,

        @SerializedName("url")
        val url: String? = null

)