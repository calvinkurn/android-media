package com.tokopedia.talk.feature.reply.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAdapterTypeFactory

class TalkReplyShimmerModel : Visitable<TalkReplyAdapterTypeFactory> {

    override fun type(typeFactory: TalkReplyAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}