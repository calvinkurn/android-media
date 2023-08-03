package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
data class SetLikeStatusResponse(
    @SerializedName("productrevLikeReview")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("feedbackID")
        val feedbackID: String = "",

        @SerializedName("likeStatus")
        val likeStatus: Int = 0,

        @SerializedName("totalLike")
        val totalLike: Int = 0,

        @SerializedName("totalDislike")
        val totalDislike: Int = 0,
    )
}
