package com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.viewholder

import android.graphics.Typeface
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.ConsentWithdrawalPurposeItemViewBinding
import com.tokopedia.privacycenter.ui.consentwithdrawal.ConsentWithdrawalConst
import com.tokopedia.privacycenter.ui.consentwithdrawal.ConsentWithdrawalListener
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel.PurposeUiModel
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class PurposeViewHolder(
    itemView: View,
    private val listener: ConsentWithdrawalListener.Mandatory
) : BaseViewHolder(itemView) {

    private val itemViewBinding by viewBinding(ConsentWithdrawalPurposeItemViewBinding::bind)

    fun onBind(item: PurposeUiModel) {
        itemViewBinding?.apply {
            val isActive = item.data.consentStatus == ConsentWithdrawalConst.OPT_IN
            itemTitle.text = item.data.consentTitle
            itemDesc.setClickableUrlHtml(htmlText = item.data.consentSubtitle,
                onUrlClicked = { link, _ ->
                    RouteManager.route(
                        itemView.context,
                        "${ApplinkConst.WEBVIEW}?url=${link.removeSurrounding("“", "”")}"
                    )
                }, applyCustomStyling = {
                    isUnderlineText = false
                    typeface = Typeface.DEFAULT_BOLD
                    color = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                }
            )
            itemTextButton.text = if (isActive) TEXT_ACTIVE else TEXT_NON_ACTIVE
            itemButtonLayout.setOnClickListener {
                listener.onActivationButtonClicked(layoutPosition, isActive, item.data)
            }

            itemTitle.setOnClickListener {
                listener.onActivationButtonClicked(layoutPosition, isActive, item.data)
            }

            itemSwitch.apply {
                isChecked = isActive
                setOnClickListener {
                    itemSwitch.isChecked = !itemSwitch.isChecked
                    listener.onToggleClicked(layoutPosition, isActive, item.data)
                }
            }

            itemButtonLayout.showWithCondition(item.isMandatoryPurpose)
            itemStichLayout.showWithCondition(!item.isMandatoryPurpose)
        }
    }

    companion object {
        private const val TEXT_ACTIVE = "Aktif"
        private const val TEXT_NON_ACTIVE = "Nonaktif"

        @LayoutRes
        val LAYOUT = R.layout.consent_withdrawal_purpose_item_view
    }
}
