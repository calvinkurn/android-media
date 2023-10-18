package com.tokopedia.minicart.chatlist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class MiniCartChatListAdapter(adapterTypeFactoryChat: MiniCartChatListAdapterTypeFactory) :
    BaseListAdapter<Visitable<*>, MiniCartChatListAdapterTypeFactory>(adapterTypeFactoryChat) {

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(MiniCartChatListDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }
}
