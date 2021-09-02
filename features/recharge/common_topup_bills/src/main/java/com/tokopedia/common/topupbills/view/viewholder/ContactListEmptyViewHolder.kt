package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactEmptyBinding
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactEmptyDataView

class ContactListEmptyViewHolder(
    private val binding: ItemTopupBillsContactEmptyBinding,
): AbstractViewHolder<TopupBillsContactEmptyDataView>(binding.root) {
    override fun bind(element: TopupBillsContactEmptyDataView?) {
        // Do nothing
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_contact_empty
    }
}