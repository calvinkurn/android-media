package com.tokopedia.talk_old.addtalk.view.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * @author by StevenFredian on 07/06/18.
 */

class QuickReplyTypeFactoryImpl(private val listener: QuickReplyTalkViewHolder.PasteTemplateListener) : BaseAdapterTypeFactory(), QuickReplyTypeFactory {

    override fun type(groupChatQuickReplyViewModel: TalkQuickReplyItemViewModel): Int {
        return QuickReplyTalkViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>

        if (type == QuickReplyTalkViewHolder.LAYOUT) {
            viewHolder = QuickReplyTalkViewHolder(parent, listener)
        } else {
            viewHolder = super.createViewHolder(parent, type)
        }

        return viewHolder
    }

}
