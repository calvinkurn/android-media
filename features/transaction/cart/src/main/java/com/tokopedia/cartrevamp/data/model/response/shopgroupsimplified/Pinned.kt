package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class Pinned(
    @SerializedName("is_pinned")
    val isPinned: Boolean = false,
    @SerializedName("coachmark_message")
    val coachmarkMessage: String = ""
)
