package com.tokopedia.talk.feature.sellersettings.template.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatTemplate(
        @SerializedName("isEnable")
        @Expose
        val isEnable: Boolean = false,
        @SerializedName("isEnableSmartReply")
        @Expose
        val isEnableSmartReply: Boolean = false,
        @SerializedName("isSeller")
        @Expose
        val isSeller: Boolean = false,
        @SerializedName("templates")
        @Expose
        val templates: List<String> = listOf()
)