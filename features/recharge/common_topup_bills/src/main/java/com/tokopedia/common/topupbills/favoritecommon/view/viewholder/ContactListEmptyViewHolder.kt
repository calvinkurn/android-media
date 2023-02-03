package com.tokopedia.common.topupbills.favoritecommon.view.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsSavedNumberEmptyStateBinding
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactEmptyDataView
import com.tokopedia.media.loader.loadImage

class ContactListEmptyViewHolder(
    private val binding: ItemTopupBillsSavedNumberEmptyStateBinding
): AbstractViewHolder<TopupBillsContactEmptyDataView>(binding.root) {

    override fun bind(element: TopupBillsContactEmptyDataView?) {
        binding.commonTopupbillsSavedNumEmptyStateImage
            .loadImage(EMPTY_STATE_IMG_URL)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_saved_number_empty_state
        const val EMPTY_STATE_IMG_URL = TokopediaImageUrl.NOT_FOUND_STATE_IMG_URL
    }
}