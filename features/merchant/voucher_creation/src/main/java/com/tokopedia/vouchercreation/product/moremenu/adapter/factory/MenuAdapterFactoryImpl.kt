package com.tokopedia.vouchercreation.product.moremenu.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel
import com.tokopedia.vouchercreation.product.moremenu.presentation.viewholder.MenuDividerViewHolder
import com.tokopedia.vouchercreation.product.moremenu.presentation.viewholder.MoreMenuViewHolder

class MenuAdapterFactoryImpl(
        private val callback: (MoreMenuUiModel) -> Unit
) : BaseAdapterTypeFactory(), MenuAdapterFactory {

    override fun type(model: MoreMenuUiModel): Int {
        return when (model) {
            is MoreMenuUiModel.ItemDivider -> MenuDividerViewHolder.LAYOUT
            else -> MoreMenuViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            MoreMenuViewHolder.LAYOUT -> MoreMenuViewHolder(parent, callback)
            MenuDividerViewHolder.LAYOUT -> MenuDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}