package com.tokopedia.gamification.taptap.data.entiity

import com.google.gson.annotations.SerializedName

data class TokensUser(
        @SerializedName("campaignID")
        val campaignID: Long = 0,

        @SerializedName("state")
        val state: String? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("text")
        val text: String? = null,

        @SerializedName("desc")
        val desc: String? = null,

        @SerializedName("tokenUserIDstr")
        val tokenUserID: String? = null,

        )