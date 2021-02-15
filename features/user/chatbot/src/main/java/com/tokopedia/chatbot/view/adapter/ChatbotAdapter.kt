package com.tokopedia.chatbot.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.BaseChatAdapter

/**
 * @author by nisie on 27/11/18.
 */

class ChatbotAdapter(private val adapterTypeFactory: ChatbotTypeFactoryImpl)
    : BaseChatAdapter(adapterTypeFactory) {

    override fun getItemViewType(position: Int): Int {
        val default = super.getItemViewType(position)
        return adapterTypeFactory.getItemViewType(visitables, position, default)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return adapterTypeFactory.createViewHolder(parent, viewType)
    }
}