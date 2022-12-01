package com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.viewholder

import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.ConsentWithdrawalPurposeItemViewBinding
import com.tokopedia.privacycenter.ui.consentwithdrawal.ConsentWithdrawalConst
import com.tokopedia.privacycenter.ui.consentwithdrawal.ConsentWithdrawalListener
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel.PurposeUiModel
import com.tokopedia.unifyprinciples.Typography
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
            itemDesc.apply {
                text = item.data.consentSubtitle.parseAsHtml()
                movementMethod = LinkMovementMethod.getInstance()
            }.also {
                it.removeUrlLine()
            }

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

    private fun Typography.removeUrlLine() {
        try {
            val spannable = SpannableString(text)
            val stringUrl =
                spannable.getSpans(0, spannable.length, URLSpan::class.java).firstOrNull()
            spannable.setSpan(
                object : URLSpan(stringUrl?.url) {
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                        ds.typeface = Typeface.DEFAULT_BOLD
                        ds.color = ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_G500
                        )
                    }
                },
                spannable.getSpanStart(stringUrl),
                spannable.getSpanEnd(stringUrl),
                0
            )
            text = spannable
        } catch (e: Exception) { }
    }

    companion object {
        private const val TEXT_ACTIVE = "Aktif"
        private const val TEXT_NON_ACTIVE = "Nonaktif"

        @LayoutRes
        val LAYOUT = R.layout.consent_withdrawal_purpose_item_view
    }
}
