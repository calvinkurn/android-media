package com.tokopedia.content.common.comment

import com.tokopedia.content.common.comment.model.Comments
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.content.common.types.ResultState
import java.time.Duration
import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentUiModelMapper @Inject constructor() {

    private val String.convertToCommentType: CommentType
        get() = if (this == "0") CommentType.Parent else CommentType.Child(this)

    @OptIn(ExperimentalStdlibApi::class)
    fun mapComments(comments: Comments) = CommentWidgetUiModel(
        cursor = comments.parent.lastCursor,
        list = buildList {
            comments.parent.comments.forEach {
                add(mapComment(it, comments.parent.parentId))
                if (it.hasReplies) add(
                    CommentUiModel.Expandable(
                        repliesCount = it.repliesCount,
                        commentType = it.id.convertToCommentType
                    )
                )
            }
        },
        commentType = comments.parent.parentId.convertToCommentType,
        state = ResultState.Success,
    )

    fun mapComment(comment: Comments.CommentData, parentId: String): CommentUiModel {
        val username = comment.username.ifBlank { comment.firstName }
        return CommentUiModel.Item(
            id = comment.id,
            username = username,
            photo = comment.photo,
            appLink = comment.id,
            content = comment.comment,
            createdTime = convertTime(comment.createdTime),
            commentType = parentId.convertToCommentType,
        )
    }

    private fun convertTime(date: String): String {
        val now = ZonedDateTime.now()
        val convert = ZonedDateTime.parse(date) //add try catch handle if null / empty
        val diff = Duration.between(now, convert)
        val minute = diff.toMinutes()
        val hour = diff.toHours()
        val day = diff.toDays()

        return if (minute < 1) "Beberapa detik yang lalu"
        else if (hour < 1) "23 menit"
        else if (hour < 24) "2 jam"
        else if (day in 1..5) "2 hari"
        else if (day > 5) "28 Agu"
        else if (day > 90) "Sep 2020"
        else ""
    }
}
