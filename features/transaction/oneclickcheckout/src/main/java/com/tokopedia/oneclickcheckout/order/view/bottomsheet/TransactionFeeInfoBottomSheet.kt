package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.view.LayoutInflater
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.BottomSheetTransactionInfoBinding
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper

class TransactionFeeInfoBottomSheet {

    fun show(view: OrderSummaryPageFragment, orderPaymentFee: OrderPaymentFee) {
        view.parentFragmentManager.let {
            BottomSheetUnify().apply {
                setTitle(view.getString(com.tokopedia.oneclickcheckout.R.string.occ_payment_fee_title_info, orderPaymentFee.title.lowercase()))
                isHideable = true
                clearContentPadding = true
                val binding = BottomSheetTransactionInfoBinding.inflate(LayoutInflater.from(view.context))
                setupView(binding, orderPaymentFee)
                setChild(binding.root)
                show(it, null)
            }
        }
    }

    private fun setupView(binding: BottomSheetTransactionInfoBinding, orderPaymentFee: OrderPaymentFee) {
        binding.llContainer.setBackgroundResource(R.drawable.background_transaction_fee_info_bottom_sheet)
        binding.tvInfo.text = HtmlLinkHelper(binding.root.context, orderPaymentFee.tooltipInfo).spannedString
    }
}
