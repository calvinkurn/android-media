package com.tokopedia.statistic.presentation.view.adapter.factory

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.presentation.model.DateRangeItem
import com.tokopedia.statistic.presentation.view.adapter.viewholder.DateRangeApplyViewHolder
import com.tokopedia.statistic.presentation.view.adapter.viewholder.DateRangeClickViewHolder
import com.tokopedia.statistic.presentation.view.adapter.viewholder.DateRangeDividerViewHolder
import com.tokopedia.statistic.presentation.view.adapter.viewholder.DateRangePickViewHolder

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateRangeAdapterFactoryImpl(
        private val listener: Listener,
        private val fm: FragmentManager
) : BaseAdapterTypeFactory(), DateRangeAdapterFactory {

    override fun type(item: DateRangeItem.Click): Int = DateRangeClickViewHolder.RES_LAYOUT

    override fun type(item: DateRangeItem.Pick): Int = DateRangePickViewHolder.RES_LAYOUT

    override fun type(item: DateRangeItem.ApplyButton): Int = DateRangeApplyViewHolder.RES_LAYOUT

    override fun type(divider: DateRangeItem.Divider): Int = DateRangeDividerViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            DateRangeClickViewHolder.RES_LAYOUT -> DateRangeClickViewHolder(parent) {
                listener.onItemDateRangeClick(it)
                listener.onApplyDateFilter()
            }
            DateRangePickViewHolder.RES_LAYOUT -> DateRangePickViewHolder(parent, fm) {
                listener.onItemDateRangeClick(it)
            }
            DateRangeApplyViewHolder.RES_LAYOUT -> DateRangeApplyViewHolder(parent) {
                listener.onApplyDateFilter()
            }
            DateRangeDividerViewHolder.RES_LAYOUT -> DateRangeDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    interface Listener {

        fun onItemDateRangeClick(model: DateRangeItem)

        fun onApplyDateFilter()
    }
}