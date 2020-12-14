package com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel

import com.tokopedia.talk.feature.inbox.data.DiscussionInboxDetail
import com.tokopedia.talk.feature.inbox.presentation.adapter.TalkInboxAdapterTypeFactory

class TalkInboxOldUiModel (
        val inboxDetail: DiscussionInboxDetail,
        val isSellerView: Boolean
) : BaseTalkInboxUiModel {

    override fun type(typeFactory: TalkInboxAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}