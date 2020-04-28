package com.tokopedia.talk.feature.reply.presentation.uimodel

data class TalkReplyHeaderModel(
        val date: String,
        val question: String,
        val isFollowed: Boolean,
        val allowFollow: Boolean,
        val allowReport: Boolean,
        val allowDelete: Boolean,
        val isMasked: Boolean,
        val maskedContent: String
)