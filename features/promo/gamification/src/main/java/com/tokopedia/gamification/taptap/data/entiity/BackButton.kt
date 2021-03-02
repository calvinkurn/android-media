package com.tokopedia.gamification.taptap.data.entiity

import com.google.gson.annotations.SerializedName

data class BackButton(
        @SerializedName("cancelText")
        val cancelText: String,

        @SerializedName("imageURL")
        val imageURL: String?,

        @SerializedName("isShow")
        val isShow: Boolean,

        @SerializedName("text")
        val text: String,

        @SerializedName("title")
        val title: String,

        @SerializedName("yesText")
        val yesText: String

)
