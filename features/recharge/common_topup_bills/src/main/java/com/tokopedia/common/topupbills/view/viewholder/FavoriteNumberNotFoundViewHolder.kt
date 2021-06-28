package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberNotFoundBinding
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberEmptyStateListener
import com.tokopedia.common.topupbills.view.model.FavoriteNumberNotFoundDataView

class FavoriteNumberNotFoundViewHolder(
        private val binding: ItemTopupBillsFavoriteNumberNotFoundBinding,
        private val emptyStateListener: FavoriteNumberEmptyStateListener
): AbstractViewHolder<FavoriteNumberNotFoundDataView>(binding.root) {

    override fun bind(element: FavoriteNumberNotFoundDataView?) {
        binding.run {
            commonTopupbillsEmptyStateButton.setOnClickListener {
                emptyStateListener.onContinueClicked()
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_favorite_number_not_found
    }
}