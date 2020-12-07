package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.topchat.chatroom.domain.pojo.review.ReviewCard
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class ReviewUiModel(
        reply: Reply,
        reviewCard: ReviewCard
) : BaseChatViewModel(
        messageId = reply.msgId.toString(),
        fromUid = reply.senderId.toString(),
        from = reply.senderName,
        fromRole = reply.role,
        attachmentId = reply.attachment.id,
        attachmentType = reply.attachment.type.toString(),
        replyTime = reply.replyTime,
        message = reply.msg,
        source = reply.source,
        replyId = reply.replyId
), Visitable<TopChatTypeFactory> {

    var reply: Reply = reply
        private set
    var reviewCard: ReviewCard = reviewCard
        private set

    val isSender: Boolean get() = !reply.isOpposite
    val isReviewed: Boolean get() = reviewCard.isReviewed
    val allowReview: Boolean get() = reviewCard.allowReview
    val ratingInt: Int get() = reviewCard.rating.toInt()

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

}