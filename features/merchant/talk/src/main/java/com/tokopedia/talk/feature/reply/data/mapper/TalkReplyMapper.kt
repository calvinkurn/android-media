package com.tokopedia.talk.feature.reply.data.mapper

import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel
import com.tokopedia.talk.feature.reply.presentation.uimodel.TalkReplyHeaderModel

object TalkReplyMapper {

    fun mapDiscussionDataResponseToTalkReplyHeaderModel(discussionDataByQuestionIDResponseWrapper: DiscussionDataByQuestionIDResponseWrapper, isMyShop: Boolean): TalkReplyHeaderModel {
        discussionDataByQuestionIDResponseWrapper.discussionDataByQuestionID.question.apply {
            return TalkReplyHeaderModel(
                    createTimeFormatted,
                    content,
                    questionState.isFollowed,
                    questionState.allowFollow.and(!isMyShop),
                    questionState.allowReport,
                    questionState.allowDelete,
                    questionState.isMasked,
                    maskedContent)
        }
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