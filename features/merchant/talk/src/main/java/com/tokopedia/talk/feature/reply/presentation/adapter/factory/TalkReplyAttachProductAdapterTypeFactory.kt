package com.tokopedia.talk.feature.reply.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyAttachProductShimmerModel
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.TalkReplyAttachProductUiModel
import com.tokopedia.talk.feature.reply.presentation.adapter.viewholder.TalkReplyAttachProductShimmerViewHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.viewholder.TalkReplyAttachProductViewHolder

class TalkReplyAttachProductAdapterTypeFactory : BaseAdapterTypeFactory(), TalkReplyAttachProductTypeFactory {

    override fun type(talkReplyAttachProductUiModel: TalkReplyAttachProductUiModel): Int {
        return TalkReplyAttachProductViewHolder.LAYOUT
    }

    override fun type(talkReplyAttachProductShimmerModel: TalkReplyAttachProductShimmerModel): Int {
        return TalkReplyAttachProductShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TalkReplyAttachProductViewHolder.LAYOUT -> TalkReplyAttachProductViewHolder(parent)
            TalkReplyAttachProductShimmerViewHolder.LAYOUT -> TalkReplyAttachProductShimmerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}