package com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.feature.inbox.presentation.adapter.TalkInboxAdapterTypeFactory

interface BaseTalkInboxUiModel : Visitable<TalkInboxAdapterTypeFactory> {

    override fun type(typeFactory: TalkInboxAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}