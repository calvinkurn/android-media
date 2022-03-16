package com.tokopedia.common.topupbills.favoritecommon.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactPermissionBinding
import com.tokopedia.common.topupbills.favoritecommon.view.adapter.TopupBillsContactListAdapter
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactPermissionDataView

class ContactListPermissionViewHolder(
    private val binding: ItemTopupBillsContactPermissionBinding,
    private val listener: TopupBillsContactListAdapter.ContactPermissionListener
) : AbstractViewHolder<TopupBillsContactPermissionDataView>(binding.root){
    override fun bind(element: TopupBillsContactPermissionDataView) {
        binding.run {
            commonTopupbillsContactPermissionButton.setOnClickListener {
                listener.onSettingButtonClick()
            }
            commonTopupbillsContactPermissionImage.setImageUrl(CONTACT_PERMISSION_IMG_URL)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_contact_permission
        const val CONTACT_PERMISSION_IMG_URL = "https://images.tokopedia.net/img/https:/images.tokopedia.net/img/https:/images.tokopedia.net/img/android/digital/common_topup_bills/common_topup_ic_contact_permission.png"
    }
}