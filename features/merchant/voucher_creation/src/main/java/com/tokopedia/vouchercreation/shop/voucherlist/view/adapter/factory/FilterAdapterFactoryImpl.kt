package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseFilterUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseFilterUiModel.FilterGroup
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseFilterUiModel.FilterItem
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder.FilterDividerViewHolder
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder.FilterGroupViewHolder
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder.FilterViewHolder

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class FilterAdapterFactoryImpl(
        private val onItemClick: (String) -> Unit
) : BaseAdapterTypeFactory(), FilterAdapterFactory {

    override fun type(model: BaseFilterUiModel): Int {
        return when (model) {
            is FilterItem -> FilterViewHolder.RES_LAYOUT
            is FilterGroup -> FilterGroupViewHolder.RES_LAYOUT
            else -> FilterDividerViewHolder.RES_LAYOUT
        }
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FilterViewHolder.RES_LAYOUT -> FilterViewHolder(parent, onItemClick)
            FilterGroupViewHolder.RES_LAYOUT -> FilterGroupViewHolder(parent)
            FilterDividerViewHolder.RES_LAYOUT -> FilterDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}