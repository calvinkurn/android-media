package com.tokopedia.talk.feature.reply.data.mapper

import com.tokopedia.talk.feature.reply.data.model.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.presentation.uimodel.TalkReplyHeaderModel

object TalkReplyMapper {

    fun mapDiscussionDataResponseToTalkReplyHeaderModel(discussionDataByQuestionIDResponseWrapper: DiscussionDataByQuestionIDResponseWrapper): TalkReplyHeaderModel {
        return TalkReplyHeaderModel(
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.createTimeFormatted,
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.content,
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.state.isFollowed
        )
    }

}