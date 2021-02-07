package com.tokopedia.talk.feature.reply.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAdapterTypeFactory

data class TalkReplyHeaderModel(
        val date: String,
        val question: String,
        val isFollowed: Boolean,
        val allowFollow: Boolean,
        val allowReport: Boolean,
        val allowDelete: Boolean,
        val allowUnmask: Boolean,
        val isMasked: Boolean,
        val maskedContent: String,
        val userThumbnail: String,
        val userName: String,
        val userId: Int,
        val isMyQuestion: Boolean,
        val isSeller: Boolean
) : Visitable<TalkReplyAdapterTypeFactory> {

    override fun type(typeFactory: TalkReplyAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}