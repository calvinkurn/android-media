package com.tokopedia.shop.settings.common.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.shop.settings.common.view.adapter.delegate.MenuDelegate
import com.tokopedia.shop.settings.common.view.adapter.viewholder.MenuViewHolder

class MenuAdapter(listener: MenuViewHolder.ItemMenuListener?): BaseDiffUtilAdapter<String>() {

    init {
        delegatesManager.addDelegate(MenuDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == oldItem
}