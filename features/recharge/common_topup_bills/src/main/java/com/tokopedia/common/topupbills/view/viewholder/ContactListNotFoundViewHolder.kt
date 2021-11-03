package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactNotFoundBinding
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactNotFoundDataView

class ContactListNotFoundViewHolder(
    private val binding: ItemTopupBillsContactNotFoundBinding,
): AbstractViewHolder<TopupBillsContactNotFoundDataView>(binding.root) {
    override fun bind(element: TopupBillsContactNotFoundDataView?) {
        // Do nothing
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_contact_not_found
    }
}