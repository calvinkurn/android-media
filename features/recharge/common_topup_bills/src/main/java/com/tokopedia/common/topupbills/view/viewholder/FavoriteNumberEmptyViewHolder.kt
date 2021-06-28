package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberEmptyStateBinding
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberEmptyStateListener
import com.tokopedia.common.topupbills.view.model.FavoriteNumberEmptyDataView

class FavoriteNumberEmptyViewHolder(
        private val binding: ItemTopupBillsFavoriteNumberEmptyStateBinding,
        private val emptyStateListener: FavoriteNumberEmptyStateListener
): AbstractViewHolder<FavoriteNumberEmptyDataView>(binding.root) {

    override fun bind(element: FavoriteNumberEmptyDataView?) {
        binding.run {
            commonTopupbillsEmptyStateButton.setOnClickListener {
                emptyStateListener.onContinueClicked()
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_favorite_number_empty_state
    }
}