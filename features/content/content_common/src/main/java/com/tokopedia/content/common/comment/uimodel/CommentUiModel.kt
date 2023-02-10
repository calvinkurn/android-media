package com.tokopedia.content.common.comment.uimodel

/**
 * @author by astidhiyaa on 09/02/23
 */

sealed interface CommentUiModel {
    data class Item(
        val id: String,
        val username: String,
        val content: String,
        val createdTime: String, //format in map
        val photo: String,
        val repliesCount: String,
        val appLink: String,
        val isExpanded: Boolean = false,
    ) : CommentUiModel

    object Shimmer : CommentUiModel
}
