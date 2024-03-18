package com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankPurchaseDetailNormalTextBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.NormalTextUiModel
import com.tokopedia.thankyou_native.presentation.fragment.ToolTipInfoBottomSheet
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class NormalTextViewHolder(val view: View) : AbstractViewHolder<NormalTextUiModel>(view) {

    private val binding: ThankPurchaseDetailNormalTextBinding? by viewBinding()

    override fun bind(element: NormalTextUiModel) {
        binding?.pdName?.shouldShowWithAction(element.name.isNotEmpty()) {
            binding?.pdName?.text = element.name
        }
        binding?.pdTotalPrice?.shouldShowWithAction(element.totalPriceStr.isNotEmpty()) {
            binding?.pdTotalPrice?.text = element.totalPriceStr
        }
        binding?.pdSlashedPrice?.shouldShowWithAction(element.detail.slashedPrice.isNotEmpty()) {
            binding?.pdSlashedPrice?.apply {
                text = element.detail.slashedPrice
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            binding?.pdTotalPrice?.setTextColor(ContextCompat.getColor(view.context, unifyprinciplesR.color.Unify_GN500))
        }
        binding?.pdTooltipIcon?.shouldShowWithAction(element.showTooltip) {
            view.setOnClickListener {
                ToolTipInfoBottomSheet.openTooltipInfoBottomSheet(
                    view.context as FragmentActivity, element.detail.tooltipTitle, element.detail.tooltipSubtitle
                )
            }
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_purchase_detail_normal_text
    }
}
