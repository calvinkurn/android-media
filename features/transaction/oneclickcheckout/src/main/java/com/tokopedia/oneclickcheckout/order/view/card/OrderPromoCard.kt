package com.tokopedia.oneclickcheckout.order.view.card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.oneclickcheckout.databinding.CardOrderPromoBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView

class OrderPromoCard(private val binding: CardOrderPromoBinding,
                     private val listener: OrderPromoCardListener,
                     private val analytics: OrderSummaryAnalytics) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = 6
    }

    fun setupButtonPromo(orderPromo: OrderPromo) {
        binding.btnPromoCheckout.margin = ButtonPromoCheckoutView.Margin.NO_BOTTOM
        when (orderPromo.state) {
            OccButtonState.LOADING -> {
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.LOADING
            }
            OccButtonState.DISABLE -> {
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.INACTIVE
                binding.btnPromoCheckout.title = binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_checkout_inactive_label)
                binding.btnPromoCheckout.desc = binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_checkout_inactive_desc)
                binding.btnPromoCheckout.setOnClickListener {
                    if (!orderPromo.isDisabled) {
                        listener.onClickRetryValidatePromo()
                    }
                }
            }
            else -> {
                val lastApply = orderPromo.lastApply
                var title = binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_funnel_label)
                if (lastApply?.additionalInfo?.messageInfo?.message?.isNotEmpty() == true) {
                    title = lastApply.additionalInfo.messageInfo.message
                } else if (lastApply?.defaultEmptyPromoMessage?.isNotBlank() == true) {
                    title = lastApply.defaultEmptyPromoMessage
                }
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.ACTIVE
                binding.btnPromoCheckout.title = title
                binding.btnPromoCheckout.desc = lastApply?.additionalInfo?.messageInfo?.detail ?: ""

                if (lastApply?.additionalInfo?.usageSummaries?.isNotEmpty() == true) {
                    analytics.eventViewPromoAlreadyApplied()
                }

                binding.btnPromoCheckout.setOnClickListener {
                    if (!orderPromo.isDisabled) {
                        listener.onClickPromo()
                    }
                }
            }
        }
        binding.root.alpha = if (orderPromo.isDisabled) 0.5f else 1.0f
    }

    interface OrderPromoCardListener {

        fun onClickRetryValidatePromo()

        fun onClickPromo()
    }
}