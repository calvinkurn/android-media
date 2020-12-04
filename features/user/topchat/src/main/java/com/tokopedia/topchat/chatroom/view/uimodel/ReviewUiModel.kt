package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.topchat.chatroom.domain.pojo.review.ReviewCard
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

data class ReviewUiModel(
        val reply: Reply = Reply(),
        val reviewCard: ReviewCard = ReviewCard()
) : Visitable<TopChatTypeFactory> {

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

}