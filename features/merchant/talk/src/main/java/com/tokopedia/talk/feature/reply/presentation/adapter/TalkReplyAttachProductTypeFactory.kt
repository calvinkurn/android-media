package com.tokopedia.talk.feature.reply.presentation.adapter

import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyAttachProductShimmerModel
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyAttachProductUiModel

interface TalkReplyAttachProductTypeFactory {
    fun type(talkReplyAttachProductUiModel: TalkReplyAttachProductUiModel): Int
    fun type(talkReplyAttachProductShimmerModel: TalkReplyAttachProductShimmerModel): Int
}