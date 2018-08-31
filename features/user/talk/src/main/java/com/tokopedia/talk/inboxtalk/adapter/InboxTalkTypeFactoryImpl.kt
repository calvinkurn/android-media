package com.tokopedia.talk.inboxtalk.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.inboxtalk.adapter.viewholder.EmptyInboxTalkViewHolder
import com.tokopedia.talk.inboxtalk.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk.inboxtalk.viewmodel.EmptyInboxTalkViewModel
import com.tokopedia.talk.inboxtalk.viewmodel.InboxTalkItemViewModel

/**
 * @author by nisie on 8/29/18.
 */

class InboxTalkTypeFactoryImpl() :
        BaseAdapterTypeFactory(),
        InboxTalkTypeFactory {

    override fun type(inboxTalkItemViewModel: InboxTalkItemViewModel): Int {
        return InboxTalkItemViewHolder.LAYOUT
    }

    override fun type(emptyInboxTalkViewModel: EmptyInboxTalkViewModel): Int {
        return EmptyInboxTalkViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            InboxTalkItemViewHolder.LAYOUT -> InboxTalkItemViewHolder(view)
            EmptyInboxTalkViewHolder.LAYOUT -> EmptyInboxTalkViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }
    }

}