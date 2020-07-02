package com.tokopedia.filter.bottomsheet

import android.view.View
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.bottomsheet.filter.FilterViewHolder
import com.tokopedia.filter.bottomsheet.filter.FilterViewListener
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewListener
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewHolder
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewModel
import com.tokopedia.filter.bottomsheet.sort.SortViewHolder
import com.tokopedia.filter.bottomsheet.sort.SortViewListener
import com.tokopedia.filter.bottomsheet.sort.SortViewModel

internal class SortFilterBottomSheetTypeFactoryImpl(
        private val sortViewListener: SortViewListener,
        private val filterViewListener: FilterViewListener,
        private val priceFilterViewListener: PriceFilterViewListener
): SortFilterBottomSheetTypeFactory {

    private val recycledViewPool = RecycledViewPool()

    override fun type(sortViewModel: SortViewModel): Int {
        return SortViewHolder.LAYOUT
    }

    override fun type(filterViewModel: FilterViewModel): Int {
        return FilterViewHolder.LAYOUT
    }

    override fun type(priceFilterViewModel: PriceFilterViewModel): Int {
        return PriceFilterViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when(viewType) {
            SortViewHolder.LAYOUT -> SortViewHolder(view, sortViewListener)
            FilterViewHolder.LAYOUT -> FilterViewHolder(view, recycledViewPool, filterViewListener)
            PriceFilterViewHolder.LAYOUT -> PriceFilterViewHolder(view, priceFilterViewListener)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        }
    }
}