package com.tokopedia.talk.feature.reply.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAttachProductAdapterTypeFactory

class TalkReplyAttachProductUiModel(

) : Visitable<TalkReplyAttachProductAdapterTypeFactory> {

    override fun type(typeFactory: TalkReplyAttachProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}