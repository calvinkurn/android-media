package com.tokopedia.tokopedianow.datefilter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.datefilter.presentation.uimodel.DateFilterUiModel
import com.tokopedia.tokopedianow.datefilter.presentation.viewholder.DateFilterViewHolder

class DateFilterAdapterTypeFactory(val listener: DateFilterViewHolder.DateFilterViewHolderListener) : BaseAdapterTypeFactory(),
    DateFilterTypeFactory {

    override fun type(uiModel: DateFilterUiModel): Int = DateFilterViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            DateFilterViewHolder.LAYOUT -> DateFilterViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        }
    }
}