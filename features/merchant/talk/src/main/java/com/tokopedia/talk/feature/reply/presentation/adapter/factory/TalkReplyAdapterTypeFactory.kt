package com.tokopedia.talk.feature.reply.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyShimmerModel
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel
import com.tokopedia.talk.feature.reply.presentation.adapter.viewholder.TalkReplyShimmerViewHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.viewholder.TalkReplyViewHolder
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.AttachedProductCardListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.ThreadListener

class TalkReplyAdapterTypeFactory(
        private val attachedProductCardListener: AttachedProductCardListener,
        private val onKebabClickedListener: OnKebabClickedListener,
        private val threadListener: ThreadListener
) : BaseAdapterTypeFactory(), TalkReplyTypeFactory {

    override fun type(talkReplyUiModel: TalkReplyUiModel): Int {
        return TalkReplyViewHolder.LAYOUT
    }

    override fun type(talkReplyShimmerModel: TalkReplyShimmerModel): Int {
        return TalkReplyShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TalkReplyViewHolder.LAYOUT -> TalkReplyViewHolder(parent, attachedProductCardListener, onKebabClickedListener, threadListener)
            TalkReplyShimmerViewHolder.LAYOUT -> TalkReplyShimmerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}