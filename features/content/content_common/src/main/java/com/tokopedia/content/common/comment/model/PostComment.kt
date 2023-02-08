package com.tokopedia.content.common.comment.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 08/02/23
 */
data class PostComment(
    @SerializedName("feedsCommentPostComment")
    val parent: Parent,
) {
    data class Parent(
        @SerializedName("data")
        val data: Comments.CommentData,

        @SerializedName("error")
        val error: String,
    )
}
