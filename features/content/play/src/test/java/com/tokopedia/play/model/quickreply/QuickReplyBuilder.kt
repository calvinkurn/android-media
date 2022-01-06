package com.tokopedia.play.model.quickreply

import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel

interface QuickReplyBuilder {

    fun buildQuickReply(
        quickReplyList: List<String> = List(5) { "abc" }
    ): PlayQuickReplyInfoUiModel
}