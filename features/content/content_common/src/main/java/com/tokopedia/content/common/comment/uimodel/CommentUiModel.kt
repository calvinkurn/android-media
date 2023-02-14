package com.tokopedia.content.common.comment.uimodel

/**
 * @author by astidhiyaa on 09/02/23
 */

sealed interface CommentUiModel {
    data class Item(
        val id: String,
        val username: String,
        val content: String,
        val createdTime: String,
        val photo: String,
        val repliesCount: String,
        val appLink: String,
        val isExpanded: Boolean = false,
        val commentType: CommentType,
    ) : CommentUiModel

    object Empty : CommentUiModel
    object Shimmer : CommentUiModel
}

sealed class CommentType {
    open val parentId = "0"

    object Parent : CommentType()

    data class Child(val mParentId: String) : CommentType() {
        override val parentId: String
            get() = mParentId
    }
}

data class CommentParam(
    val commentType: CommentType = CommentType.Parent,
    val lastParentCursor: String = "",
    val lastChildCursor: String = "",
    val needToRefresh: Boolean = false,
)
