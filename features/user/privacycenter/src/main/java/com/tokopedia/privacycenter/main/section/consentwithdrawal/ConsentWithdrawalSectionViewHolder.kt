package com.tokopedia.privacycenter.main.section.consentwithdrawal

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentGroupDataModel
import com.tokopedia.privacycenter.databinding.BaseItemPrivacyCenterBinding
import com.tokopedia.utils.view.binding.viewBinding

class ConsentWithdrawalSectionViewHolder(
    itemView: View,
    private val listener: ConsentWithdrawalSectionAdapter.Listener
): BaseViewHolder(itemView) {

    private val itemViewBinding by viewBinding<BaseItemPrivacyCenterBinding>()

    fun onBind(item: ConsentGroupDataModel) {
        itemViewBinding?.itemViewPrivacyCenter?.apply {
            title = item.groupTitle
            description = item.groupSubtitle
            setIcon(item.groupImage)

            onNavigationButtonClicked {
                listener.onItemClicked(item)
            }
        }
    }
}
