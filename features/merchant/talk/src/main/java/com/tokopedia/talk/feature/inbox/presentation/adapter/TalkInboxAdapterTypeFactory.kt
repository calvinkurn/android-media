package com.tokopedia.talk.feature.inbox.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder.TalkInboxLoadingViewHolder
import com.tokopedia.talk.feature.inbox.presentation.adapter.viewholder.TalkInboxViewHolder

class TalkInboxAdapterTypeFactory(
        private val isSellerView: Boolean
) : TalkInboxTypeFactory, BaseAdapterTypeFactory() {

    override fun type(talkInboxUiModel: TalkInboxUiModel): Int {
        return TalkInboxViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingMoreModel): Int {
        return TalkInboxLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TalkInboxViewHolder.LAYOUT -> TalkInboxViewHolder(parent, isSellerView)
            TalkInboxLoadingViewHolder.LAYOUT -> TalkInboxLoadingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}