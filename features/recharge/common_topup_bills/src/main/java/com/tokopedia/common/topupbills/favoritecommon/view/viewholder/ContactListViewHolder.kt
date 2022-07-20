package com.tokopedia.common.topupbills.favoritecommon.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactBinding
import com.tokopedia.common.topupbills.favoritecommon.view.adapter.TopupBillsContactListAdapter
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class ContactListViewHolder(
    private val binding: ItemTopupBillsContactBinding,
    private val listener: TopupBillsContactListAdapter.ContactNumberClickListener
): AbstractViewHolder<TopupBillsContactDataView>(binding.root) {

    override fun bind(contact: TopupBillsContactDataView) {
        binding.run {
            if (contact.name.isNotEmpty() && contact.name != contact.number) {
                commonTopupBillsContactNumber.show()
                commonTopupBillsContactName.text = contact.name
                commonTopupBillsContactNumber.text = contact.number
                commonTopupBillsContainerContactNumber.setOnClickListener {
                    listener.onContactNumberClick(contact.name, contact.number)
                }
            } else {
                commonTopupBillsContactNumber.hide()
                commonTopupBillsContactName.text = contact.number
                commonTopupBillsContainerContactNumber.setOnClickListener {
                    listener.onContactNumberClick(
                        getString(R.string.common_topup_contact_name_default),
                        contact.number
                    )
                }
            }
            commonTopupBillsInitial.text = contact.name[0].toString()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_contact
    }
}