package com.tokopedia.content.common.comment

import com.tokopedia.content.common.comment.model.Comments
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel
import javax.inject.Inject

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentUiModelMapper @Inject constructor() {
    fun mapComments(comments: Comments) = CommentWidgetUiModel(
        cursor = comments.parent.lastCursor,
        list = comments.parent.comments.map(::mapComment)
    )

    fun mapComment(comment: Comments.CommentData): CommentUiModel {
        val username = comment.username.ifBlank { comment.firstName }
        return CommentUiModel.Item(
            id = comment.id,
            username = username,
            photo = comment.photo,
            repliesCount = comment.repliesCount,
            appLink = comment.id,
            content = comment.comment,
            createdTime = comment.createdTime,
        )
    }
}
