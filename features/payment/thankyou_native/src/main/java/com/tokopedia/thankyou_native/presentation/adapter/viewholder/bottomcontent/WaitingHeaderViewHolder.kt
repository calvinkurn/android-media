package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankWaitingHeaderBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.WaitingHeaderUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.thankyou_native.domain.model.ThankPageTopTickerData
import com.tokopedia.thankyou_native.presentation.fragment.DeferredPaymentFragment
import com.tokopedia.thankyou_native.presentation.views.listener.HeaderListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class WaitingHeaderViewHolder(
    private val view: View,
    private val listener: HeaderListener
): AbstractViewHolder<WaitingHeaderUiModel>(view) {

    private val binding: ThankWaitingHeaderBinding? by viewBinding()

    override fun bind(data: WaitingHeaderUiModel?) {
        if (data == null) return

        binding?.headerTitle?.text = data.title
        binding?.headerDescription?.text = data.description
        val cal = Calendar.getInstance()
        cal.add(Calendar.SECOND, data.timeRemaining.toInt())
        binding?.headerTimer?.targetDate = cal
        binding?.accountIdLabel?.text = data.accountIdLabel
        binding?.accountId?.text = data.accountId
        binding?.bankBranch?.shouldShowWithAction(data.bankBranch.isNotEmpty()) {
            binding?.bankBranch?.text = HtmlLinkHelper(view.context, getString(R.string.thank_bank_branch, data.bankBranch)).spannedString
        }
        binding?.accountImage?.setImageUrl(data.accountImage)
        binding?.amountLabel?.text = data.amountLabel
        if (data.highlightLastThreeDigits) {
            highlightLastThreeDigits(CurrencyFormatUtil.convertPriceValueToIdrFormat(data.amount, false))
        } else {
            binding?.amount?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.amount, false)
        }
        binding?.info?.setText(data.note)
        binding?.primaryButton?.shouldShowWithAction(!data.shouldHidePrimaryButton) {
            binding?.primaryButton?.text = data.primaryButtonText
        }
        binding?.secondaryButton?.shouldShowWithAction(!data.shouldHideSecondaryButton) {
            binding?.secondaryButton?.text = data.secondaryButtonText
        }
        binding?.amountCopyIcon?.setOnClickListener {
            listener.onCopyAmount(data.amount.toString())
        }
        binding?.accountIdCopyIcon?.setOnClickListener {
            listener.onCopyAccountId(data.accountId)
        }
        binding?.primaryButton?.setOnClickListener {
            listener.onButtonClick(data.primaryButtonApplink, data.primaryButtonType, true, data.primaryButtonText)
        }
        binding?.secondaryButton?.setOnClickListener {
            listener.onButtonClick(data.secondaryButtonApplink, data.secondaryButtonType, false, data.secondaryButtonText)
        }
        binding?.seeDetailBtn?.setOnClickListener {
            listener.onSeeDetailInvoice()
        }
        binding?.headerTicker?.apply {
            shouldShowWithAction(data.tickerData.isNotEmpty()) {
                val tickerViewPagerAdapter = TickerPagerAdapter(context, data.tickerData)
                addPagerView(tickerViewPagerAdapter, data.tickerData)
                tickerViewPagerAdapter.setPagerDescriptionClickEvent(object :
                    TickerPagerCallback {
                    override fun onPageDescriptionViewClick(
                        linkUrl: CharSequence,
                        itemData: Any?
                    ) {
                        if (itemData is ThankPageTopTickerData) {
                            if (itemData.isAppLink()) {
                                listener.openApplink(linkUrl.toString())
                            } else {
                                listener.openApplink(linkUrl.toString())
                            }
                        }
                    }
                })
            }
        }
    }

    private fun highlightLastThreeDigits(amountStr: String) {
        view.context?.let {
            binding?.amount?.setTextColor(
                ContextCompat.getColor(
                    it,
                    unifyprinciplesR.color.Unify_NN950_96
                )
            )
            val spannable =
                SpannableString(amountStr)
            if (amountStr.length > DeferredPaymentFragment.HIGHLIGHT_DIGIT_COUNT) {
                val startIndex = spannable.length - DeferredPaymentFragment.HIGHLIGHT_DIGIT_COUNT
                spannable.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            it,
                            unifyprinciplesR.color.Unify_GN500
                        )
                    ),
                    startIndex, spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            binding?.amount?.text = spannable
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_waiting_header
    }
}
