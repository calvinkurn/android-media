package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.SerializedName

data class CountDownInfo(

        @SerializedName("isShown")
        val isShown: Boolean = false,

        @SerializedName("type")
        val type: Int? = 0,

        @SerializedName("label")
        val label: String = "",

        @SerializedName("countdownUnix")
        var countdownUnix: Long = 0,

        @SerializedName("countdownStr")
        val countdownStr: String = "",

        @SerializedName("textColor")
        val textColor: String = "",

        @SerializedName("backgroundColor")
        val backgroundColor: String = ""
)
