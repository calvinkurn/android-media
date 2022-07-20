package com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.presentation.viewholder.MoreMenuDividerViewHolder
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.presentation.viewholder.MoreMenuViewHolder

class MoreMenuAdapterFactoryImpl(
        private val callback: (MoreMenuUiModel) -> Unit
) : BaseAdapterTypeFactory(), MoreMenuAdapterFactory {

    override fun type(model: MoreMenuUiModel): Int {
        return when (model) {
            is MoreMenuUiModel.ItemDivider -> MoreMenuDividerViewHolder.LAYOUT
            else -> MoreMenuViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            MoreMenuViewHolder.LAYOUT -> MoreMenuViewHolder(parent, callback)
            MoreMenuDividerViewHolder.LAYOUT -> MoreMenuDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}