package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactBinding
import com.tokopedia.common.topupbills.view.adapter.TopupBillsContactListAdapter
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactDataView

class ContactListViewHolder(
    private val binding: ItemTopupBillsContactBinding,
    private val listener: TopupBillsContactListAdapter.ContactNumberClickListener
): AbstractViewHolder<TopupBillsContactDataView>(binding.root) {

    override fun bind(contact: TopupBillsContactDataView) {
        binding.run {
            commonTopupBillsContactName.text = contact.name
            commonTopupBillsContactNumber.text = contact.number
            commonTopupBillsInitial.text = contact.name[0].toString()
            commonTopupBillsContainerContactNumber.setOnClickListener {
                listener.onContactNumberClick(contact.name, contact.number)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_contact
    }
}