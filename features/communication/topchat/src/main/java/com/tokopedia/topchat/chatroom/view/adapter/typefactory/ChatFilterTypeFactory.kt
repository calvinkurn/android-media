package com.tokopedia.topchat.chatroom.view.adapter.typefactory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.ChatFilterUiModel

interface ChatFilterTypeFactory : AdapterTypeFactory {
    fun type(chatFilterUiModel: ChatFilterUiModel): Int

    /**
     * use to pass interface implmented on adapter to viewholder
     * @param any can be used to pass several interfaces without the need
     * to call `this` multiple times
     */
    fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int, adapterListener: Any
    ): AbstractViewHolder<out Visitable<*>>
}