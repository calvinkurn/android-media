package com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.ConsentWithdrawalTitleDividerItemViewBinding
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel.TitleDividerUiModel
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class TitleDividerViewHolder(
    itemView: View
) : BaseViewHolder(itemView) {

    private val itemViewBinding by viewBinding(ConsentWithdrawalTitleDividerItemViewBinding::bind)

    fun onBind(item: TitleDividerUiModel) {
        itemViewBinding?.apply {
            itemLargeDivider.showWithCondition(item.isDivider && !item.isSmallDivider)
            itemSmallDivider.showWithCondition(item.isDivider && item.isSmallDivider)

            itemTitle.apply {
                text = item.title
            }.showWithCondition(!item.isDivider)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.consent_withdrawal_title_divider_item_view
    }
}
