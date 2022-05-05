package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetPaymentCardBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetPaymentData
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetPaymentCardListener


class CMHomeWidgetPaymentCardViewHolder(
    private val binding: LayoutCmHomeWidgetPaymentCardBinding,
    private val listener: CMHomeWidgetPaymentCardListener
) : AbstractViewHolder<CMHomeWidgetPaymentData>(binding.root) {

    override fun bind(paymentData: CMHomeWidgetPaymentData) {
        setBankLogo(paymentData.imgUrl)
        setBankGatewayName(paymentData.gatewayName)
        setAccountNo(paymentData.accountNumber)
        setTotalPayment(paymentData.totalPayment)
        setActionButtonText(paymentData.actionButton?.get(0)?.actionButtonText)
        binding.root.post {
            listener.setPaymentCardHeight(binding.root.measuredHeight)
        }
    }

    private fun setBankLogo(paymentImageUrl: String?) {
        paymentImageUrl?.let {
            binding.ivCmHomeWidgetPayment.setImageUrl(it)
        }
    }

    private fun setBankGatewayName(gatewayName: String?) {
        binding.tvCmHomeWidgetPaymentGatewayName.text = gatewayName
    }

    private fun setAccountNo(accountNo: String?){
        binding.tvCmHomeWidgetPaymentAccountNo.text = accountNo
    }

    private fun setTotalPayment(amount: String?) {
        binding.tvCmHomeWidgetPaymentAmt.text = amount
    }

    private fun setActionButtonText(text: String?) {
        binding.btnCmHomeWidgetProduct.text = text
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_product_card
        const val RATIO_WIDTH = 0.689
    }

}