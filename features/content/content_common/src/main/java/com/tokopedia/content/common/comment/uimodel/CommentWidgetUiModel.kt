package com.tokopedia.content.common.comment.uimodel

/**
 * @author by astidhiyaa on 09/02/23
 */
data class CommentWidgetUiModel(
    val list: List<CommentUiModel>,
    val cursor: String,
    val commentType: CommentType,
)

val CommentWidgetUiModel.hasNextPage : Boolean
    get() = this.cursor.isNotBlank()
