package com.tokopedia.common.topupbills.favoritecommon.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactNotFoundBinding
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactNotFoundDataView
import com.tokopedia.media.loader.loadImage

class ContactListNotFoundViewHolder(
    private val binding: ItemTopupBillsContactNotFoundBinding,
): AbstractViewHolder<TopupBillsContactNotFoundDataView>(binding.root) {
    override fun bind(element: TopupBillsContactNotFoundDataView?) {
        binding.commonTopupbillsContactNotFoundStateImage
            .loadImage(NOT_FOUND_STATE_IMG_URL)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_contact_not_found
        const val NOT_FOUND_STATE_IMG_URL = "https://images.tokopedia.net/img/common_topup_ic_illustration_not_found.png"
    }
}
