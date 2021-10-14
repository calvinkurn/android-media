package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topchat.chatroom.domain.pojo.review.ReviewCard
import com.tokopedia.topchat.chatroom.domain.pojo.review.ReviewReminderAttribute
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class ReviewUiModel private constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory>, DeferredAttachment {

    var reply: Reply = builder.reply
        private set
    var reviewCard: ReviewCard = builder.reviewCard
        private set

    val impressHolder = ImpressHolder()

    val isReviewed: Boolean get() = reviewCard.isReviewed
    val allowReview: Boolean get() = reviewCard.allowReview
    val reputationId: Long get() = reviewCard.reputationId
    val ratingInt: Int
        get() {
            var rate = reviewCard.rating.toInt()
            if (rate > 5) {
                rate = 5
            }
            return rate
        }

    private var sourceTrack: String? = null
    private var trackId: String? = null

    init {
        val split = reply.source.split("|")
        sourceTrack = split.getOrNull(0)
        trackId = split.getOrNull(1)
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

    fun waitingForReview(): Boolean {
        return !isReviewed && allowReview
    }

    fun hasExpired(): Boolean {
        return !isReviewed && !allowReview
    }

    fun shouldShowStar(): Boolean {
        return !hasExpired()
    }

    fun getEventLabel(isSeller: Boolean): String {
        return "$trackId - $sourceTrack - ${getEventType(isSeller)} - ${getEventStatus()}"
    }

    private fun getEventStatus(): String {
        return when {
            waitingForReview() -> "menunggu diulas"
            isReviewed -> "sudah diulas"
            hasExpired() -> "tidak bisa diulas"
            else -> ""
        }
    }

    private fun getEventType(isSeller: Boolean): String {
        return if (isSeller) {
            "seller"
        } else {
            "buyer"
        }
    }

    class Builder : SendableUiModel.Builder<Builder, ReviewUiModel>() {

        internal var reply: Reply = Reply()
        internal var reviewCard: ReviewCard = ReviewCard()

        fun withReply(reply: Reply): Builder {
            this.reply = reply
            return self()
        }

        fun withReviewCard(reviewCard: ReviewCard): Builder {
            this.reviewCard = reviewCard
            return self()
        }

        override fun build(): ReviewUiModel {
            return ReviewUiModel(this)
        }
    }
}