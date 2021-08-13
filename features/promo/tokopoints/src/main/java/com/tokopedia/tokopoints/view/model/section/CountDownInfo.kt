package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.SerializedName

data class CountDownInfo(

        @SerializedName("isShown")
        val isShown: Boolean? = null,

        @SerializedName("type")
        val type: Int? = null,

        @SerializedName("label")
        val label: String? = null,

        @SerializedName("countdownUnix")
        var countdownUnix: Long? = null,

        @SerializedName("countdownStr")
        val countdownStr: String? = null,

        @SerializedName("textColor")
        val textColor: String? = null,

        @SerializedName("backgroundColor")
        val backgroundColor: String? = null
)