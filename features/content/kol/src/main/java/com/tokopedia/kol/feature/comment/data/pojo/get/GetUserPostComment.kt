package com.tokopedia.kol.feature.comment.data.pojo.get

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class GetUserPostComment(
    @SerializedName("postKol")
    val postKol: PostKol = PostKol(),
    @SerializedName("comments")
    val comments: List<Comment> = emptyList(),
    @SerializedName("lastCursor")
    val lastCursor: String = "",
    @SerializedName("error")
    val error: String = ""
)
