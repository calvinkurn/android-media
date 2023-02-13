package com.tokopedia.oneclickcheckout.order.view.card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.oneclickcheckout.databinding.CardOrderPromoBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.promocheckout.common.R as promoR

class OrderPromoCard(private val binding: CardOrderPromoBinding,
                     private val listener: OrderPromoCardListener,
                     private val analytics: OrderSummaryAnalytics) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = 6

        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f
    }

    fun setupButtonPromo(orderPromo: OrderPromo) {
        binding.btnPromoCheckout.margin = ButtonPromoCheckoutView.Margin.NO_BOTTOM
        when (orderPromo.state) {
            OccButtonState.LOADING -> {
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.LOADING
                binding.btnPromoCheckout.chevronIcon = promoR.drawable.ic_promo_checkout_chevron_right
            }
            OccButtonState.DISABLE -> {
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.INACTIVE
                binding.btnPromoCheckout.title = binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_checkout_inactive_label)
                binding.btnPromoCheckout.desc = binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_checkout_inactive_desc)
                binding.btnPromoCheckout.chevronIcon = promoR.drawable.ic_promo_checkout_refresh
                binding.btnPromoCheckout.setOnClickListener {
                    if (!orderPromo.isDisabled) {
                        listener.onClickRetryValidatePromo()
                    }
                }
            }
            else -> {
                val lastApply = orderPromo.lastApply
                var title = binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_funnel_label)
                if (lastApply.additionalInfo.messageInfo.message.isNotEmpty()) {
                    title = lastApply.additionalInfo.messageInfo.message
                } else if (lastApply.defaultEmptyPromoMessage.isNotBlank()) {
                    title = lastApply.defaultEmptyPromoMessage
                }
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.ACTIVE
                binding.btnPromoCheckout.title = title
                binding.btnPromoCheckout.desc = lastApply.additionalInfo.messageInfo.detail
                binding.btnPromoCheckout.chevronIcon = promoR.drawable.ic_promo_checkout_chevron_right

                if (lastApply.additionalInfo.usageSummaries.isNotEmpty()) {
                    analytics.eventViewPromoAlreadyApplied()
                }

                binding.btnPromoCheckout.setOnClickListener {
                    if (!orderPromo.isDisabled) {
                        listener.onClickPromo()
                    }
                }
            }
        }
        binding.root.alpha = if (orderPromo.isDisabled) DISABLE_ALPHA else ENABLE_ALPHA
    }

    interface OrderPromoCardListener {

        fun onClickRetryValidatePromo()

        fun onClickPromo()
    }
}