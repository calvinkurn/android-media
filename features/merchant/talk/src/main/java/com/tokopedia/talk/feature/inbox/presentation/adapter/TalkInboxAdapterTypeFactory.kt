package com.tokopedia.talk.feature.inbox.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.BaseTalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxOldUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder.TalkInboxLoadingViewHolder
import com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder.TalkInboxOldViewHolder
import com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder.TalkInboxUnifiedLoadingViewHolder
import com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder.TalkInboxViewHolder
import com.tokopedia.talk.feature.inbox.presentation.listener.TalkInboxViewHolderListener

class TalkInboxAdapterTypeFactory(
        private val talkInboxViewHolderListener: TalkInboxViewHolderListener,
        private val isNewView: Boolean
) : TalkInboxTypeFactory, BaseAdapterTypeFactory() {

    override fun type(baseTalkInboxUiModel: BaseTalkInboxUiModel): Int {
        if(isNewView) {
            return TalkInboxViewHolder.LAYOUT
        }
        return TalkInboxOldViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingMoreModel): Int {
        if(isNewView) {
            return TalkInboxUnifiedLoadingViewHolder.LAYOUT
        }
        return TalkInboxLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TalkInboxOldViewHolder.LAYOUT -> TalkInboxOldViewHolder(parent, talkInboxViewHolderListener)
            TalkInboxViewHolder.LAYOUT -> TalkInboxViewHolder(parent, talkInboxViewHolderListener)
            TalkInboxLoadingViewHolder.LAYOUT -> TalkInboxLoadingViewHolder(parent)
            TalkInboxUnifiedLoadingViewHolder.LAYOUT -> TalkInboxUnifiedLoadingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}