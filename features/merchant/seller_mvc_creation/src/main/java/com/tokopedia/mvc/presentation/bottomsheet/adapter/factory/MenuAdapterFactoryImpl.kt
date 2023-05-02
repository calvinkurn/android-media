package com.tokopedia.mvc.presentation.bottomsheet.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.mvc.presentation.bottomsheet.adapter.viewHolder.MenuDividerViewHolder
import com.tokopedia.mvc.presentation.bottomsheet.adapter.viewHolder.MenuViewHolder
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel

class MenuAdapterFactoryImpl(private val callback: (MoreMenuUiModel) -> Unit) : BaseAdapterTypeFactory(), MenuAdapterFactory {

    override fun type(model: MoreMenuUiModel): Int {
        return when (model) {
            is MoreMenuUiModel.ItemDivider -> MenuDividerViewHolder.RES_LAYOUT
            else -> MenuViewHolder.RES_LAYOUT
        }
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            MenuViewHolder.RES_LAYOUT -> MenuViewHolder(parent, callback)
            MenuDividerViewHolder.RES_LAYOUT -> MenuDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
