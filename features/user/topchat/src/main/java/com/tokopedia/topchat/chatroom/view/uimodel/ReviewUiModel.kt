package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.topchat.chatroom.domain.pojo.review.ReviewCard
import com.tokopedia.topchat.chatroom.domain.pojo.review.ReviewReminderAttribute
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class ReviewUiModel constructor(
        reply: Reply,
        reviewCard: ReviewCard
) : SendableViewModel(
        messageId = reply.msgId.toString(),
        fromUid = reply.senderId.toString(),
        from = reply.senderName,
        fromRole = reply.role,
        attachmentId = reply.attachment.id,
        attachmentType = reply.attachment.type.toString(),
        replyTime = reply.replyTime,
        message = reply.msg,
        source = reply.source,
        replyId = reply.replyId,
        isDummy = false,
        isRead = reply.isRead,
        isSender = !reply.isOpposite,
        startTime = reply.replyTime
), Visitable<TopChatTypeFactory>, DeferredAttachment {

    var reply: Reply = reply
        private set
    var reviewCard: ReviewCard = reviewCard
        private set

    val isReviewed: Boolean get() = reviewCard.isReviewed
    val allowReview: Boolean get() = reviewCard.allowReview
    val reputationId: Int get() = reviewCard.reputationId
    val ratingInt: Int
        get() {
            var rate = reviewCard.rating.toInt()
            if (rate > 5) {
                rate = 5
            }
            return rate
        }

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String get() = attachmentId

    override fun updateData(attribute: Any?) {
        if (attribute is ReviewReminderAttribute) {
            reviewCard = attribute.reviewCard
            this.isLoading = false
        }
    }

    override fun syncError() {
        this.isLoading = false
        this.isError = true
    }

    override fun finishLoading() {
        this.isLoading = false
        this.isError = false
    }

}