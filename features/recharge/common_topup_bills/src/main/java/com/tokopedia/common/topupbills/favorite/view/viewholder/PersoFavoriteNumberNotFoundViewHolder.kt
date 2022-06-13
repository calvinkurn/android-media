package com.tokopedia.common.topupbills.favorite.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberNotFoundBinding
import com.tokopedia.common.topupbills.favorite.view.listener.PersoFavoriteNumberNotFoundStateListener
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberNotFoundDataView

class PersoFavoriteNumberNotFoundViewHolder(
    private val binding: ItemTopupBillsFavoriteNumberNotFoundBinding,
    private val notFoundStateListener: PersoFavoriteNumberNotFoundStateListener
): AbstractViewHolder<TopupBillsPersoFavNumberNotFoundDataView>(binding.root) {

    override fun bind(element: TopupBillsPersoFavNumberNotFoundDataView?) {
        binding.run {
            commonTopupbillsNotFoundStateButton.setOnClickListener {
                notFoundStateListener.onContinueClicked()
            }
        }
    }

    companion object {
        // reuse seamless favorite number layout
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_favorite_number_not_found
    }
}