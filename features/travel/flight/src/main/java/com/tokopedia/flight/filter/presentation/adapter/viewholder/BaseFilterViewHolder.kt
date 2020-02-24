package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import kotlinx.android.synthetic.main.item_flight_filter_sort.view.*

/**
 * @author by jessica on 2020-02-24
 */

abstract class BaseFilterViewHolder<T : BaseFilterSortModel>(val view: View) : AbstractViewHolder<T>(view) {
    fun onResetChip() {
        itemView?.flight_sort_widget.onResetChip()
    }
}