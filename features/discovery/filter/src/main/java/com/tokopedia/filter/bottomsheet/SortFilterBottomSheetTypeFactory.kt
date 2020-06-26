package com.tokopedia.filter.bottomsheet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewModel
import com.tokopedia.filter.bottomsheet.sort.SortViewModel

internal interface SortFilterBottomSheetTypeFactory {

    fun type(sortViewModel: SortViewModel): Int

    fun type(filterViewModel: FilterViewModel): Int

    fun type(priceFilterViewModel: PriceFilterViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}