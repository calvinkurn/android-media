package com.tokopedia.content.common.comment

import com.tokopedia.content.common.comment.model.Comments
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentUiModelMapper {
    fun mapComments(comments: Comments) = CommentWidgetUiModel(
        cursor = comments.parent.lastCursor,
        list = comments.parent.comments.map {
            val username = it.username.ifBlank { it.firstName }
            CommentUiModel.Item(
                id = it.id,
                username = username,
                photo = it.photo,
                repliesCount = it.repliesCount,
                appLink = it.id
            )
        }
    )
}
