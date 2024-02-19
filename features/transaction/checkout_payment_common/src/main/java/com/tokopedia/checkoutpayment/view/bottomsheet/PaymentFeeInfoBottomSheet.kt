package com.tokopedia.checkoutpayment.view.bottomsheet

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.tokopedia.checkoutpayment.R
import com.tokopedia.checkoutpayment.databinding.BottomSheetPaymentFeeInfoBinding
import com.tokopedia.checkoutpayment.view.OrderPaymentFee
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper

class PaymentFeeInfoBottomSheet {

    fun show(view: Fragment, orderPaymentFee: OrderPaymentFee) {
        view.parentFragmentManager.let {
            BottomSheetUnify().apply {
                setTitle(view.getString(R.string.checkout_payment_fee_title_info, orderPaymentFee.title.lowercase()))
                isHideable = true
                clearContentPadding = true
                val binding = BottomSheetPaymentFeeInfoBinding.inflate(LayoutInflater.from(view.context))
                setupView(binding, orderPaymentFee)
                setChild(binding.root)
                show(it, null)
            }
        }
    }

    private fun setupView(binding: BottomSheetPaymentFeeInfoBinding, orderPaymentFee: OrderPaymentFee) {
        binding.llContainer.setBackgroundResource(R.drawable.background_payment_fee_info_bottom_sheet)
        binding.tvInfo.text = HtmlLinkHelper(binding.root.context, orderPaymentFee.tooltipInfo).spannedString
    }
}
