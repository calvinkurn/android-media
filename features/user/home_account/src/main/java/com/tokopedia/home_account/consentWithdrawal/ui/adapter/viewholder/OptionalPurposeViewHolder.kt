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
import com.tokopedia.home_account.databinding.ViewItemConsentWithdrawalPurposeOptionalBinding
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class OptionalPurposeViewHolder(
    itemView: View,
    private val listener: ConsentWithdrawalListener.Optional
) : BaseViewHolder(itemView) {

    private val itemViewBinding by viewBinding(ViewItemConsentWithdrawalPurposeOptionalBinding::bind)

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

            itemSwitch.apply {
                isChecked = isActive
                setOnClickListener {
                    itemSwitch.isChecked = !itemSwitch.isChecked
                    listener.onToggleClicked(layoutPosition, isActive, item)
                }
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_item_consent_withdrawal_purpose_optional
    }
}
