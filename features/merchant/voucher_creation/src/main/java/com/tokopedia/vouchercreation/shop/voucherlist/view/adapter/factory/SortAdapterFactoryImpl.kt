package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.SortUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder.SortViewHolder

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class SortAdapterFactoryImpl(
        private val onItemClick: (sort: SortUiModel) -> Unit
) : BaseAdapterTypeFactory(), SortAdapterFactory {

    override fun type(model: SortUiModel): Int = SortViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SortViewHolder.RES_LAYOUT -> SortViewHolder(parent, onItemClick)
            else -> super.createViewHolder(parent, type)
        }
    }
}