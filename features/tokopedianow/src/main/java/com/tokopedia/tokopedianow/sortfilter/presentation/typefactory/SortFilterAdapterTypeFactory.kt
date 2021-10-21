package com.tokopedia.tokopedianow.sortfilter.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.sortfilter.presentation.uimodel.SortFilterUiModel
import com.tokopedia.tokopedianow.sortfilter.presentation.viewholder.SortFilterViewHolder

class SortFilterAdapterTypeFactory(val listener: SortFilterViewHolder.SortFilterViewHolderListener) : BaseAdapterTypeFactory(), SortFilterTypeFactory {

    override fun type(uiModel: SortFilterUiModel): Int = SortFilterViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SortFilterViewHolder.LAYOUT -> SortFilterViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        }
    }
}