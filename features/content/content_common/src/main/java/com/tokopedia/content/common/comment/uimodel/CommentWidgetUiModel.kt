package com.tokopedia.content.common.comment.uimodel

import com.tokopedia.content.common.types.ResultState

/**
 * @author by astidhiyaa on 09/02/23
 */
data class CommentWidgetUiModel(
    val list: List<CommentUiModel>,
    val cursor: String,
    val commentType: CommentType,
    val state: ResultState,
) {
    companion object {
        val Empty
            get() = CommentWidgetUiModel(
                list = emptyList(),
                cursor = "",
                commentType = CommentType.Parent,
                state = ResultState.Loading,
            )
    }
}

val CommentWidgetUiModel.hasNextPage: Boolean
    get() = this.cursor.isNotBlank()
