package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.voucherlist.model.BottomSheetMenuUiModel
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.MenuDividerViewHolder
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.MenuViewHolder

/**
 * Created By @ilhamsuaib on 18/04/20
 */

class MenuAdapterFactoryImpl(
        private val listener: MenuViewHolder.Listener
) : BaseAdapterTypeFactory(), MenuAdapterFactory {

    override fun type(model: BottomSheetMenuUiModel): Int {
        return when (model) {
            is BottomSheetMenuUiModel.ItemDivider -> MenuDividerViewHolder.RES_LAYOUT
            else -> MenuViewHolder.RES_LAYOUT
        }
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            MenuViewHolder.RES_LAYOUT -> MenuViewHolder(parent, listener)
            MenuDividerViewHolder.RES_LAYOUT -> MenuDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}