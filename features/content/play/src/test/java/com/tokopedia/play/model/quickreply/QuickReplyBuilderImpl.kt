package com.tokopedia.play.model.quickreply

import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel

class QuickReplyBuilderImpl : QuickReplyBuilder {

    override fun buildQuickReply(
        quickReplyList: List<String>
    ) = PlayQuickReplyInfoUiModel(quickReplyList = quickReplyList)
}