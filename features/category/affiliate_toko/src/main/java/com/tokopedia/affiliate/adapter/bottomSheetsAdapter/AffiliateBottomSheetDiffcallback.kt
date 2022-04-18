package com.tokopedia.affiliate.adapter.bottomSheetsAdapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateRangePickerModel

class AffiliateBottomSheetDiffcallback: DiffUtil.ItemCallback<Visitable<*>>() {
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        var itemAreSame = false
        if(oldItem is AffiliateDateRangePickerModel && newItem is AffiliateDateRangePickerModel) {
            itemAreSame = oldItem == newItem
        }
        return itemAreSame
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        var contentsAreSame = false
        if(oldItem is AffiliateDateRangePickerModel && newItem is AffiliateDateRangePickerModel) {
            contentsAreSame = oldItem.dateRange.isSelected == newItem.dateRange.isSelected
        }
        return contentsAreSame
    }
}