package com.tokopedia.statistic.presentation.view.adapter.factory

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.presentation.view.model.DateFilterItem
import com.tokopedia.statistic.presentation.view.adapter.viewholder.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterAdapterFactoryImpl(
        private val listener: Listener,
        private val fm: FragmentManager
) : BaseAdapterTypeFactory(), DateFilterAdapterFactory {

    override fun type(item: DateFilterItem.Click): Int = DateFilterClickViewHolder.RES_LAYOUT

    override fun type(item: DateFilterItem.Pick): Int = DateFilterPickViewHolder.RES_LAYOUT

    override fun type(item: DateFilterItem.ApplyButton): Int = DateFilterApplyViewHolder.RES_LAYOUT

    override fun type(divider: DateFilterItem.Divider): Int = DateFilterDividerViewHolder.RES_LAYOUT

    override fun type(item: DateFilterItem.MonthPickerItem): Int = MonthPickerViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            DateFilterClickViewHolder.RES_LAYOUT -> DateFilterClickViewHolder(parent, listener::onItemDateRangeClick)
            DateFilterApplyViewHolder.RES_LAYOUT -> DateFilterApplyViewHolder(parent, listener::onApplyDateFilter)
            DateFilterPickViewHolder.RES_LAYOUT -> DateFilterPickViewHolder(parent, fm, listener::onItemDateRangeClick)
            MonthPickerViewHolder.RES_LAYOUT -> MonthPickerViewHolder(parent, fm, listener::onItemDateRangeClick)
            DateFilterDividerViewHolder.RES_LAYOUT -> DateFilterDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    interface Listener {

        fun onItemDateRangeClick(model: DateFilterItem)

        fun onApplyDateFilter()
    }
}