package com.tokopedia.home_account.consentWithdrawal.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.TitleDividerUiModel
import com.tokopedia.home_account.databinding.ViewItemConsentWithdrawalTitleDividerBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class TitleDividerViewHolder(
    itemView: View
) : BaseViewHolder(itemView) {

    private val itemViewBinding by viewBinding(ViewItemConsentWithdrawalTitleDividerBinding::bind)

    fun onBind(item: TitleDividerUiModel) {
        itemViewBinding?.apply {
            itemDivider.showWithCondition(!item.isTitle)
            itemTitle.apply {
                text = item.title
            }.showWithCondition(item.isTitle)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_item_consent_withdrawal_title_divider
    }
}
