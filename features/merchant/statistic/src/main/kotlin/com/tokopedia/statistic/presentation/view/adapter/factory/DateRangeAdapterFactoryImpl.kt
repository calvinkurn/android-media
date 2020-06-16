package com.tokopedia.statistic.presentation.view.adapter.factory

import android.view.View
import androidx.fragment.app.FragmentManager
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
        private val listener: Listener,
        private val fm: FragmentManager
) : BaseAdapterTypeFactory(), DateRangeAdapterFactory {

    override fun type(item: DateRangeItem.Default): Int = DateRangeDefaultViewHolder.RES_LAYOUT

    override fun type(item: DateRangeItem.Custom): Int = DateRangeCustomViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            DateRangeDefaultViewHolder.RES_LAYOUT -> DateRangeDefaultViewHolder(parent) {
                listener.onApplyDateFilter(it)
                listener.onItemDateRangeClick(it)
            }
            DateRangeCustomViewHolder.RES_LAYOUT -> DateRangeCustomViewHolder(parent, fm, listener::onApplyDateFilter) {
                listener.onItemDateRangeClick(it)
            }
            else -> super.createViewHolder(parent, type)
        }
    }

    interface Listener {

        fun onItemDateRangeClick(model: DateRangeItem)

        fun onApplyDateFilter(model: DateRangeItem)
    }
}