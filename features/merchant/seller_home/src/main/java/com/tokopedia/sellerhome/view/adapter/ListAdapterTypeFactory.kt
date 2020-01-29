package com.tokopedia.sellerhome.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.view.model.ListItemUiModel
import com.tokopedia.sellerhome.view.viewholder.ListItemViewHolder

class ListAdapterTypeFactory : BaseAdapterTypeFactory() {
    fun type(listItem: ListItemUiModel): Int {
        return ListItemViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ListItemViewHolder.RES_LAYOUT -> ListItemViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}