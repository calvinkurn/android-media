package com.tokopedia.cart.old.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class Pinned(
        @SerializedName("is_pinned")
        val isPinned: Boolean = false,
        @SerializedName("coachmark_message")
        val coachmarkMessage: String = ""
)