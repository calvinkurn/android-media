package com.tokopedia.statistic.presentation.view.adapter.factory

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.presentation.model.DateRangeItem
import com.tokopedia.statistic.presentation.view.adapter.viewholder.DateRangeApplyViewHolder
import com.tokopedia.statistic.presentation.view.adapter.viewholder.DateRangeDefaultViewHolder
import com.tokopedia.statistic.presentation.view.adapter.viewholder.DateRangeSingleViewHolder

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateRangeAdapterFactoryImpl(
        private val listener: Listener,
        private val fm: FragmentManager
) : BaseAdapterTypeFactory(), DateRangeAdapterFactory {

    override fun type(item: DateRangeItem.Default): Int = DateRangeDefaultViewHolder.RES_LAYOUT

    override fun type(item: DateRangeItem.Single): Int = DateRangeSingleViewHolder.RES_LAYOUT

    override fun type(item: DateRangeItem.ApplyButton): Int = DateRangeApplyViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            DateRangeDefaultViewHolder.RES_LAYOUT -> DateRangeDefaultViewHolder(parent) {
                listener.onItemDateRangeClick(it)
                listener.onApplyDateFilter()
            }
            DateRangeSingleViewHolder.RES_LAYOUT -> DateRangeSingleViewHolder(parent, fm, listener::showApplyButton) {
                listener.onItemDateRangeClick(it)
            }
            DateRangeApplyViewHolder.RES_LAYOUT -> DateRangeApplyViewHolder(parent) {
                listener.onApplyDateFilter()
            }
            else -> super.createViewHolder(parent, type)
        }
    }

    interface Listener {

        fun onItemDateRangeClick(model: DateRangeItem)

        fun showApplyButton(isShown: Boolean)

        fun onApplyDateFilter()
    }
}