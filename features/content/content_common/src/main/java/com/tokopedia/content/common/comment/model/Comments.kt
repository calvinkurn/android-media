package com.tokopedia.content.common.comment.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 08/02/23
 */
data class Comments(
    @SerializedName("feedsCommentGetComments")
    val parent: Parent = Parent(),
    ) {
    data class Parent(
        @SerializedName("comments")
        val comments: List<CommentData> = emptyList(),

        @SerializedName("nextRepliesCount")
        val nextRepliesCount: String = "",

        @SerializedName("nextRepliesCountFmt")
        val nextRepliesCountFmt: String = "",

        @SerializedName("isReplyAsShop")
        val isReplyAsShop: String = "",

        @SerializedName("totalData")
        val totalData: String = "",

        @SerializedName("lastCursor")
        val lastCursor: String = "",

        @SerializedName("error")
        val error: String = "",
    )

    data class CommentData(
        @SerializedName("isKol")
        val isKOL: Boolean,

        @SerializedName("id")
        val id: String,

        @SerializedName("linkDetail")
        val linkDetail: LinkDetail,

        @SerializedName("name")
        val name: String,

        @SerializedName("firstName")
        val firstName: String = "",

        @SerializedName("username")
        val username: String = "",

        @SerializedName("photo")
        val photo: String = "",

        @SerializedName("userID")
        val userId: String,

        @SerializedName("fullName")
        val fullName: String = "",

        @SerializedName("badge")
        val badge: String = "",

        @SerializedName("allowReport")
        val allowReport: Boolean = false,

        @SerializedName("isCommentOwner")
        val isCommentOwner: Boolean = false,

        @SerializedName("createTime")
        val createdTime: String = "",

        @SerializedName("comment")
        val comment: String = "",

        @SerializedName("isShop")
        val isShop: Boolean = false,

        @SerializedName("hasReplies")
        val hasReplies: Boolean = false,

        @SerializedName("repliesCount")
        val repliesCount: String = "",

        @SerializedName("repliesCountFmt")
        val repliesCountFmt: String = "",
    )

    data class LinkDetail(
        @SerializedName("webLink")
        val webLink: String = "",

        @SerializedName("appLink")
        val appLink: String = "",

        @SerializedName("desktopLink")
        val desktopLink: String = "",
    )
}
