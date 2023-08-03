package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 12, 2023
 */
data class GetUserReviewListRequest(
    @SerializedName("userID")
    val userID: String,

    @SerializedName("limit")
    val limit: Int,

    @SerializedName("page")
    val page: Int,
)
