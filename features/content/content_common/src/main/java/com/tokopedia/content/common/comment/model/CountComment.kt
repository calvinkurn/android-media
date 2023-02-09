package com.tokopedia.content.common.comment.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 08/02/23
 */
data class CountComment(
    @SerializedName("feedsCommentCountComments")
    val parent: Parent,
) {
    data class Parent(
        @SerializedName("data")
        val child: Child,

        @SerializedName("error")
        val error: String,
    )

    data class Child(
        @SerializedName("countCommentsData")
        val data: List<Data>,
    )

    data class Data(
        val contentId: String = "",
        val count: String = "",
        val countFmt: String = "",
        val shouldShow: Boolean = false,
    )
}
