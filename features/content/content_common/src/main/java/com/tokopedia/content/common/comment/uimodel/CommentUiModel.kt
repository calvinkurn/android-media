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
        val appLink: String,
        val commentType: CommentType,
        val childCount: String,
        val isOwner: Boolean,
        val isReportAllowed: Boolean,
        val userId: String,
        val userType: UserType,
    ) : CommentUiModel {
        companion object {
            val Empty = Item(
                id = "",
                username = "",
                content = "",
                createdTime = "",
                photo = "",
                appLink = "",
                commentType = CommentType.Parent,
                childCount = "",
                isOwner = false,
                isReportAllowed = false,
                userId = "",
                userType = UserType.People,
            )
        }
    }

    object Empty : CommentUiModel
    object Shimmer : CommentUiModel

    data class Expandable(
        val commentType: CommentType,
        val repliesCount: String,
        val isExpanded: Boolean = false,
    ) : CommentUiModel
}

sealed class CommentType {
    open val parentId = "0"

    object Parent : CommentType()

    data class Child(val mParentId: String) : CommentType() {
        override val parentId: String
            get() = mParentId
    }
}

val CommentType.isParent : Boolean
    get() = this == CommentType.Parent

val CommentType.isChild : Boolean
    get() = this is CommentType.Child

data class CommentParam(
    val commentType: CommentType = CommentType.Parent,
    val lastParentCursor: String = "",
    val lastChildCursor: String = "",
    val needToRefresh: Boolean = true,
)

enum class UserType(val value: String) {
    Shop("shop"), People("user"), Unknown("");

    companion object {
        private val values = values()

        fun getByValue(value: String): UserType {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return Unknown
        }
    }
}
