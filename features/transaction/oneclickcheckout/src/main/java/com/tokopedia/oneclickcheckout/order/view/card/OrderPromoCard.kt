package com.tokopedia.oneclickcheckout.order.view.card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.databinding.CardOrderPromoBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.promocheckout.common.view.uimodel.PromoEntryPointSummaryItem
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.promocheckout.common.R as promoR

class OrderPromoCard(
    private val binding: CardOrderPromoBinding,
    private val listener: OrderPromoCardListener,
    private val analytics: OrderSummaryAnalytics
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = 6

        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f
    }

    fun setupButtonPromo(orderPromo: OrderPromo) {
//        renderOldButtonPromo(orderPromo)
        renderNewButtonPromo(orderPromo)
    }

    private fun renderOldButtonPromo(orderPromo: OrderPromo) {
        binding.btnPromoEntryPoint.gone()
        binding.dividerPromoEntryPointBottom.gone()
        binding.btnPromoCheckout.visible()
        binding.btnPromoCheckout.margin = ButtonPromoCheckoutView.Margin.NO_BOTTOM
        when (orderPromo.state) {
            OccButtonState.LOADING -> {
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.LOADING
                binding.btnPromoCheckout.chevronIcon =
                    promoR.drawable.ic_promo_checkout_chevron_right
            }

            OccButtonState.DISABLE -> {
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.INACTIVE
                binding.btnPromoCheckout.title =
                    binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_checkout_inactive_label)
                binding.btnPromoCheckout.desc =
                    binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_checkout_inactive_desc)
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
                binding.btnPromoCheckout.chevronIcon =
                    promoR.drawable.ic_promo_checkout_chevron_right

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

    private fun renderNewButtonPromo(orderPromo: OrderPromo) {
        binding.btnPromoCheckout.gone()
        binding.btnPromoEntryPoint.visible()
        binding.dividerPromoEntryPointBottom.visible()
        when (orderPromo.state) {
            OccButtonState.LOADING -> {
                binding.btnPromoEntryPoint.showLoading()
            }

            OccButtonState.DISABLE -> {
                binding.btnPromoEntryPoint.showError {
                    if (!orderPromo.isDisabled) {
                        listener.onClickRetryValidatePromo()
                    }
                }
            }

            else -> {
                val lastApply = orderPromo.lastApply
                var title = binding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_funnel_label)
                var isApplied = false
                if (lastApply.additionalInfo.messageInfo.message.isNotEmpty()) {
                    title = lastApply.additionalInfo.messageInfo.message
                    isApplied = lastApply.additionalInfo.usageSummaries.isNotEmpty()
                } else if (lastApply.defaultEmptyPromoMessage.isNotBlank()) {
                    title = lastApply.defaultEmptyPromoMessage
                }
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.ACTIVE
                binding.btnPromoCheckout.title = title
                binding.btnPromoCheckout.desc = lastApply.additionalInfo.messageInfo.detail
                binding.btnPromoCheckout.chevronIcon =
                    promoR.drawable.ic_promo_checkout_chevron_right

                if (lastApply.additionalInfo.usageSummaries.isNotEmpty()) {
                    analytics.eventViewPromoAlreadyApplied()
                }

                binding.btnPromoCheckout.setOnClickListener {
                    if (!orderPromo.isDisabled) {
                        listener.onClickPromo()
                    }
                }

                if (isApplied) {
                    binding.btnPromoEntryPoint.showApplied(
                        title,
                        lastApply.additionalInfo.messageInfo.detail,
                        IconUnify.CHEVRON_RIGHT,
                        lastApply.additionalInfo.usageSummaries.map { PromoEntryPointSummaryItem(it.description, it.amountStr) },
                        showConfetti = true
                    ) {
                        if (!orderPromo.isDisabled) {
                            listener.onClickPromo()
                        }
                    }
                } else {
                    binding.btnPromoEntryPoint.showActive(
                        title,
                        IconUnify.CHEVRON_RIGHT
                    ) {
                        if (!orderPromo.isDisabled) {
                            listener.onClickPromo()
                        }
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
