package com.tokopedia.gamification.taptap.data.entiity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.gamification.taptap.utils.TapTapConstants

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

        @Expose(serialize = false, deserialize = false)
        val isEmptyState: Boolean = TapTapConstants.TokenState.STATE_EMPTY.equals(state, ignoreCase = true)
)