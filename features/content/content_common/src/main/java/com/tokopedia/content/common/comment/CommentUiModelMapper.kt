package com.tokopedia.content.common.comment

import com.tokopedia.content.common.comment.model.Comments
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import java.util.concurrent.TimeUnit
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
        nextRepliesCount = comments.parent.nextRepliesCountFmt,
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
            childCount = comment.repliesCountFmt,
        )
    }

    private fun convertTime(date: String): String {
        val convert = date.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS)
        val diff = DateUtil.getCurrentCalendar().time.time - convert.time
        val hour = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
        val day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

        return if (hour < 1) {
            LESS_THAN_1HOUR
        } else if (hour < 24) LESS_THAN_1DAY
        else if (day in 1..5) LESS_THAN_1DAY_5DAY
        else if (day in 6..89) MORE_THAN_5DAY
        else if (day > 90) MORE_THAN_90DAY
        else LESS_THAN_1MIN
    }

    companion object {
        private const val LESS_THAN_1MIN = "Beberapa detik yang lalu"
        private const val LESS_THAN_1HOUR = "23 menit"
        private const val LESS_THAN_1DAY = "2 jam"
        private const val LESS_THAN_1DAY_5DAY = "2 hari"
        private const val MORE_THAN_5DAY = "28 Agu"
        private const val MORE_THAN_90DAY = "Sep 2020"
    }
}
