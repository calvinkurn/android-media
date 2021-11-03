package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsSavedNumberEmptyStateBinding
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberEmptyDataView

class FavoriteNumberEmptyViewHolder(
        binding: ItemTopupBillsSavedNumberEmptyStateBinding
): AbstractViewHolder<TopupBillsFavNumberEmptyDataView>(binding.root) {

    override fun bind(element: TopupBillsFavNumberEmptyDataView?) {
        // do nothing
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_saved_number_empty_state
    }
}