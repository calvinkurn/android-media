package com.tokopedia.common.topupbills.favoritepage.view.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsSavedNumberEmptyStateBinding
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsPersoFavNumberEmptyDataView
import com.tokopedia.media.loader.loadImage

class PersoFavoriteNumberEmptyViewHolder(
    private val binding: ItemTopupBillsSavedNumberEmptyStateBinding
): AbstractViewHolder<TopupBillsPersoFavNumberEmptyDataView>(binding.root) {

    override fun bind(element: TopupBillsPersoFavNumberEmptyDataView?) {
        binding.commonTopupbillsSavedNumEmptyStateImage.loadImage(EMPTY_STATE_IMG_URL)
    }

    companion object {
        // reuse seamless favorite number layout
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_saved_number_empty_state
        const val EMPTY_STATE_IMG_URL = TokopediaImageUrl.NOT_FOUND_STATE_IMG_URL
    }
}