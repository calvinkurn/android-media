package com.tokopedia.talk.feature.reply.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.factory.TalkReplyAdapterTypeFactory

class TalkReplyProductHeaderModel(
        val productName: String,
        val thumbnail: String,
        val stockValue: Int,
        val stockText: String,
        val isSellerView: Boolean,
        val impressHolder: ImpressHolder = ImpressHolder()
) : Visitable<TalkReplyAdapterTypeFactory> {

    override fun type(typeFactory: TalkReplyAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}