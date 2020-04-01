package com.tokopedia.filter.newdynamicfilter.adapter.typefactory

import android.view.View

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.BottomSheetExpandableItemViewHolder
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterItemPriceViewHolder
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterItemToggleViewHolder
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterNoViewHolder
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterViewHolder
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetDynamicFilterView

class BottomSheetDynamicFilterTypeFactoryImpl(private val filterView: BottomSheetDynamicFilterView) : DynamicFilterTypeFactory {

    override fun type(filter: Filter): Int {
        return if (filter.isSeparator) {
            R.layout.dynamic_filter_item_separator
        } else if (filter.isPriceFilter) {
            R.layout.dynamic_filter_item_price
        } else if (filter.isExpandableFilter) {
            R.layout.dynamic_filter_expandable_item
        } else {
            R.layout.dynamic_filter_item_toggle
        }
    }

    override fun createViewHolder(view: View, viewType: Int): DynamicFilterViewHolder {
        return if (viewType == R.layout.dynamic_filter_item_price) {
            DynamicFilterItemPriceViewHolder(view, filterView)
        } else if (viewType == R.layout.dynamic_filter_item_toggle) {
            DynamicFilterItemToggleViewHolder(view, filterView)
        } else if (viewType == R.layout.dynamic_filter_expandable_item) {
            BottomSheetExpandableItemViewHolder(view, filterView)
        } else {
            DynamicFilterNoViewHolder(view)
        }
    }
}
