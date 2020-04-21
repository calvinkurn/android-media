package com.tokopedia.talk.feature.reply.data.mapper

import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel
import com.tokopedia.talk.feature.reply.presentation.uimodel.TalkReplyHeaderModel

object TalkReplyMapper {

    fun mapDiscussionDataResponseToTalkReplyHeaderModel(discussionDataByQuestionIDResponseWrapper: DiscussionDataByQuestionIDResponseWrapper): TalkReplyHeaderModel {
        return TalkReplyHeaderModel(
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.createTimeFormatted,
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.content,
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.questionState.isFollowed
        )
    }

    fun mapDiscussionDataResponseToTalkReplyModels(discussionDataByQuestionIDResponseWrapper: DiscussionDataByQuestionIDResponseWrapper): List<TalkReplyUiModel> {
        val result = mutableListOf<TalkReplyUiModel>()
        discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.answer.forEach {
            result.add(TalkReplyUiModel(it))
        }
        return result
    }

}