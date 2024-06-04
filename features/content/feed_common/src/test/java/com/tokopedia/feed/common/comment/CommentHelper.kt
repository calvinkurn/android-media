package com.tokopedia.feed.common.comment

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feed.common.comment.uimodel.CommentType
import com.tokopedia.feed.common.comment.uimodel.CommentUiModel
import com.tokopedia.feed.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.feed.common.comment.uimodel.UserType

/**
 * @author by astidhiyaa on 21/03/23
 */
class CommentHelper {
    fun buildCommentWidget(
        list: List<CommentUiModel> = buildCommentList(),
        cursor: String = "",
        nextRepliesCount: String = "",
        commentType: CommentType = CommentType.Parent,
        state: ResultState = ResultState.Success,
        commenterType: UserType = UserType.People
    ) = CommentWidgetUiModel(
        list,
        cursor,
        nextRepliesCount,
        commentType,
        state,
        commenterType
    )

    fun buildCommentList(list: List<CommentUiModel> = emptyList()) = list

    fun buildItemComment(
        id: String = "123",
        username: String = "yahoo",
        content: String = "Ini comment ya",
        createdTime: String = "2023-05-08T10:24:04+07:00",
        photo: String = "blob.com",
        appLink: String = "",
        commentType: CommentType = CommentType.Parent,
        childCount: String = "0",
        isOwner: Boolean = false,
        isReportAllowed: Boolean = true,
        userId: String = "1",
        userType: UserType = UserType.People
    ) = CommentUiModel.Item(
        id,
        username,
        content,
        createdTime,
        photo,
        appLink,
        commentType,
        childCount,
        isOwner,
        isReportAllowed,
        userId,
        userType
    )
}
