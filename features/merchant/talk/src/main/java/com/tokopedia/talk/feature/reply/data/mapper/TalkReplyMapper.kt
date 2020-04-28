package com.tokopedia.talk.feature.reply.data.mapper

import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel
import com.tokopedia.talk.feature.reply.presentation.uimodel.TalkReplyHeaderModel

object TalkReplyMapper {

    fun mapDiscussionDataResponseToTalkReplyHeaderModel(discussionDataByQuestionIDResponseWrapper: DiscussionDataByQuestionIDResponseWrapper, isMyShop: Boolean): TalkReplyHeaderModel {
        return TalkReplyHeaderModel(
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.createTimeFormatted,
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.content,
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.questionState.isFollowed,
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.questionState.allowFollow.and(!isMyShop),
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.questionState.allowReport,
                discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.questionState.allowDelete
        )
    }

    fun mapDiscussionDataResponseToTalkReplyModels(discussionDataByQuestionIDResponseWrapper: DiscussionDataByQuestionIDResponseWrapper): List<TalkReplyUiModel> {
        val result = mutableListOf<TalkReplyUiModel>()
        discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.answer.forEach {
            result.add(TalkReplyUiModel(it))
        }
        return result
    }

    fun mapResultProductsToAttachedProducts(resultProducts: List<ResultProduct>): MutableList<AttachedProduct> {
        val result = mutableListOf<AttachedProduct>()
        resultProducts.forEach {
            result.add(AttachedProduct(it.productId.toString(), it.productImageThumbnail, it.name, it.productUrl, it.price ))
        }
        return result
    }

}