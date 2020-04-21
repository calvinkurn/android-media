package com.tokopedia.talk.feature.reply.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyShimmerModel
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyUiModel
import com.tokopedia.talk.feature.reply.presentation.adapter.viewholder.TalkReplyShimmerViewHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.viewholder.TalkReplyViewHolder
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnAttachedProductCardClickedListener

class TalkReplyAdapterTypeFactory(
        private val onAttachedProductCardClickedListener: OnAttachedProductCardClickedListener
) : BaseAdapterTypeFactory(), TalkReplyTypeFactory {

    override fun type(talkReplyUiModel: TalkReplyUiModel): Int {
        return TalkReplyViewHolder.LAYOUT
    }

    override fun type(talkReplyShimmerModel: TalkReplyShimmerModel): Int {
        return TalkReplyShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TalkReplyViewHolder.LAYOUT -> TalkReplyViewHolder(parent, onAttachedProductCardClickedListener)
            TalkReplyShimmerViewHolder.LAYOUT -> TalkReplyShimmerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}