package com.tokopedia.checkoutpayment.view.bottomsheet

import androidx.fragment.app.Fragment
import com.tokopedia.checkoutpayment.view.OrderPaymentFee
import com.tokopedia.unifycomponents.BottomSheetUnify

class PaymentFeeInfoBottomSheet {

    fun show(view: Fragment, orderPaymentFee: OrderPaymentFee) {
        view.parentFragmentManager.let {
            BottomSheetUnify().apply {
//                setTitle(view.getString(R.string.occ_payment_fee_title_info, orderPaymentFee.title.lowercase()))
                isHideable = true
                clearContentPadding = true
//                val binding = BottomSheetTransactionInfoBinding.inflate(LayoutInflater.from(view.context))
//                setupView(binding, orderPaymentFee)
//                setChild(binding.root)
                show(it, null)
            }
        }
    }

//    private fun setupView(binding: BottomSheetTransactionInfoBinding, orderPaymentFee: OrderPaymentFee) {
//        binding.llContainer.setBackgroundResource(R.drawable.background_transaction_fee_info_bottom_sheet)
//        binding.tvInfo.text = HtmlLinkHelper(binding.root.context, orderPaymentFee.tooltipInfo).spannedString
//    }
}
