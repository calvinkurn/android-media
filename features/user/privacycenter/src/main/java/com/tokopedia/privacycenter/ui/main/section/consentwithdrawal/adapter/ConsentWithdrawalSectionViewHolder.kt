package com.tokopedia.privacycenter.ui.main.section.consentwithdrawal.adapter

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.privacycenter.data.ConsentGroupDataModel
import com.tokopedia.privacycenter.databinding.BaseItemPrivacyCenterBinding
import com.tokopedia.utils.view.binding.viewBinding

class ConsentWithdrawalSectionViewHolder(
    itemView: View,
    private val listener: ConsentWithdrawalSectionAdapter.Listener
) : BaseViewHolder(itemView) {

    private val itemViewBinding by viewBinding<BaseItemPrivacyCenterBinding>()

    fun onBind(item: ConsentGroupDataModel) {
        itemViewBinding?.itemViewPrivacyCenter?.apply {
            title = item.groupTitle
            description = item.groupSubtitle
            setIcon(item.groupImage)

            rootView.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }
}
