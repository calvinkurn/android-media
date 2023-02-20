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
        val data: NewComment,

        @SerializedName("error")
        val error: String,
    ) {
        data class NewComment(
            @SerializedName("commentID")
            val id: String,

            @SerializedName("commentParentID")
            val parentId: String,

            @SerializedName("createTime")
            val createdTime: String = "",

            @SerializedName("comment")
            val comment: String = "",

            @SerializedName("user")
            val userInfo: UserComment = UserComment()
        )

        data class UserComment(
            @SerializedName("isKol")
            val isKOL: Boolean = false,

            @SerializedName("linkDetail")
            val linkDetail: Comments.LinkDetail = Comments.LinkDetail(),

            @SerializedName("name")
            val name: String = "",

            @SerializedName("firstName")
            val firstName: String = "",

            @SerializedName("username")
            val username: String = "",

            @SerializedName("photo")
            val photo: String = "",

            @SerializedName("userID")
            val userId: String = "",

            @SerializedName("fullName")
            val fullName: String = "",
        )
    }
}
