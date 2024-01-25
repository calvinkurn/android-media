package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankInstantHeaderBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.InstantHeaderUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_PLUS_BADGE
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.thankyou_native.helper.radiusClip
import com.tokopedia.thankyou_native.presentation.views.listener.HeaderListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat
import com.tokopedia.utils.view.binding.viewBinding
import org.json.JSONArray

class InstantHeaderViewHolder(
    view: View,
    private val listener: HeaderListener
): AbstractViewHolder<InstantHeaderUiModel>(view) {

    private val binding: ThankInstantHeaderBinding? by viewBinding()

    override fun bind(data: InstantHeaderUiModel?) {
        if (data == null) return

        binding?.headerIcon?.setImageUrl(data.gatewayImage)
        binding?.headerTitle?.text = data.title
        binding?.headerDescription?.text = data.description
        binding?.headerTips?.shouldShowWithAction(data.note.isNotEmpty()) {
            binding?.headerTips?.description = data.note.joinToString("\n")
        }
        binding?.primaryButton?.shouldShowWithAction(!data.shouldHidePrimaryButton) {
            binding?.primaryButton?.text = data.primaryButtonText
        }
        binding?.secondaryButton?.shouldShowWithAction(!data.shouldHideSecondaryButton) {
            binding?.secondaryButton?.text = data.secondaryButtonText
        }
        binding?.cardContainer?.setOnClickListener {
            listener.onSeeDetailInvoice()
        }
        binding?.primaryButton?.setOnClickListener {
            listener.onPrimaryButtonClick()
        }
        binding?.secondaryButton?.setOnClickListener {
            listener.onSecondaryButtonClick()
        }
        setUpTotalDeduction(data)
    }

    private fun setUpTotalDeduction(data: InstantHeaderUiModel?) {
        val jsonArray = JSONArray(data?.promoFlags)
        val stringList = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            val element = jsonArray.getString(i)
            stringList.add(element)
        }

        binding?.shimmerView?.shouldShowWithAction(stringList.isNotEmpty() && data?.totalDeduction != 0f) {
            binding?.shimmerView?.radiusClip(8.toPx().toFloat(), 8.toPx().toFloat(), 8.toPx().toFloat(), 8.toPx().toFloat())
            binding?.plusBenefitIcon?.shouldShowWithAction(stringList.contains(PLUS)) {
                binding?.plusBenefitIcon?.loadImageWithoutPlaceholder(IMG_PLUS_BADGE)
            }
            binding?.etcBenefitText?.showWithCondition(stringList.contains(PLUS) && stringList.size > Int.ONE)
            itemView.context?.let {
                binding?.totalDeductionText?.text = HtmlLinkHelper(
                    it,
                    String.format(
                        it.getString(if (stringList.contains(PLUS)) R.string.thank_benefit_with_plus else R.string.thank_benefit_without_plus),
                        convertPriceValueToIdrFormat(data?.totalDeduction?.toLong() ?: 0, false)
                    )).spannedString
            }
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_instant_header
        private const val PLUS = "plus"
    }
}
