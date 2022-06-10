package com.tokopedia.topchat.chatroom.view.adapter.typefactory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.filter.ChatFilterViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.ChatFilterUiModel

class ChatFilterTypeFactoryImpl(

) : BaseAdapterTypeFactory(), ChatFilterTypeFactory {

    override fun type(chatFilterUiModel: ChatFilterUiModel): Int {
        return ChatFilterViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
            adapterListener: Any
    ): AbstractViewHolder<out Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            ChatFilterViewHolder.LAYOUT -> ChatFilterViewHolder(
                    view, adapterListener as? ChatFilterViewHolder.Listener
            )
            else -> createViewHolder(view, viewType)
        }
    }

}