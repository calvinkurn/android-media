package com.tokopedia.content.common.comment.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 08/02/23
 */
data class DeleteComment(
    @SerializedName("feedsCommentDeleteComment")
    val data: Data,
) {
    data class Data(
        @SerializedName("data")
        val status: Status,

        @SerializedName("error")
        val error: String,
    )

    data class Status(
        @SerializedName("data")
        val success: Long,
    )
}
