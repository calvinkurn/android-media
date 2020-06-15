package com.tokopedia.statistic.presentation.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.presentation.model.DateRangeItem
import com.tokopedia.statistic.presentation.view.adapter.viewholder.DateRangeCustomViewHolder
import com.tokopedia.statistic.presentation.view.adapter.viewholder.DateRangeDefaultViewHolder

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateRangeAdapterFactoryImpl(
        private val listener: Listener
) : BaseAdapterTypeFactory(), DateRangeAdapterFactory {

    override fun type(item: DateRangeItem.Default): Int = DateRangeDefaultViewHolder.RES_LAYOUT

    override fun type(item: DateRangeItem.Custom): Int = DateRangeCustomViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            DateRangeDefaultViewHolder.RES_LAYOUT -> DateRangeDefaultViewHolder(parent, listener::onApplyDateFilter)
            DateRangeCustomViewHolder.RES_LAYOUT -> DateRangeCustomViewHolder(parent, listener::onApplyDateFilter)
            else -> super.createViewHolder(parent, type)
        }
    }

    interface Listener {

        fun onApplyDateFilter(model: DateRangeItem)
    }
}