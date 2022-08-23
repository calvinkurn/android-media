package com.tokopedia.topchat.chatroom.domain.pojo.review

import com.google.gson.annotations.SerializedName

data class ReviewReminderAttribute(
        @SerializedName("review_card")
        val reviewCard: ReviewCard = ReviewCard()
)