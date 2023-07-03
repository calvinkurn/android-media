package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
data class SetLikeStatusRequest(
    @SerializedName("feedbackID")
    val feedbackID: String,

    @SerializedName("likeStatus")
    val isLike: Boolean,
)
