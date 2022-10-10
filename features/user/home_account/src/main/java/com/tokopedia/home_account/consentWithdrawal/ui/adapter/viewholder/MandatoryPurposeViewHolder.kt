package com.tokopedia.home_account.consentWithdrawal.ui.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.Utils.removeUrlLine
import com.tokopedia.home_account.consentWithdrawal.common.TransactionType
import com.tokopedia.home_account.consentWithdrawal.data.ConsentPurposeItemDataModel
import com.tokopedia.home_account.consentWithdrawal.ui.ConsentWithdrawalListener
import com.tokopedia.home_account.databinding.ViewItemConsentWithdrawalPurposeMandatoryBinding
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class MandatoryPurposeViewHolder(
    itemView: View,
    private val listener: ConsentWithdrawalListener.Mandatory
) : BaseViewHolder(itemView) {

    private val itemViewBinding by viewBinding(ViewItemConsentWithdrawalPurposeMandatoryBinding::bind)

    fun onBind(item: ConsentPurposeItemDataModel) {
        itemViewBinding?.apply {
            val isActive = item.consentStatus == TransactionType.OPT_IN.alias
            itemTitle.text = item.consentTitle
            itemDesc.apply {
                text = item.consentSubtitle.parseAsHtml()
                movementMethod = LinkMovementMethod.getInstance()
            }.also {
                it.removeUrlLine()
            }

            itemTextButton.text = if (isActive) TEXT_ACTIVE else TEXT_NON_ACTIVE
            itemButtonLayout.setOnClickListener {
                listener.onActivationButtonClicked(layoutPosition, isActive, item)
            }
        }
    }

    companion object {
        private const val TEXT_ACTIVE = "Aktif"
        private const val TEXT_NON_ACTIVE = "Nonaktif"

        @LayoutRes
        val LAYOUT = R.layout.view_item_consent_withdrawal_purpose_mandatory
    }
}
