package com.tokopedia.talk.inboxtalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.EmptyInboxTalkViewHolder
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel

/**
 * @author by nisie on 8/29/18.
 */

class InboxTalkTypeFactoryImpl(private val talkItemListener: InboxTalkItemViewHolder.TalkItemListener) :
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
            InboxTalkItemViewHolder.LAYOUT -> InboxTalkItemViewHolder(view, talkItemListener)
            EmptyInboxTalkViewHolder.LAYOUT -> EmptyInboxTalkViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }
    }

}