package com.tokopedia.oneclickcheckout.order.view.card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.databinding.CardOrderPromoBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.promocheckout.common.view.uimodel.PromoEntryPointSummaryItem
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.promocheckout.common.R as promocheckoutcommonR
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR

class OrderPromoCard(
    private val binding: CardOrderPromoBinding,
    private val listener: OrderPromoCardListener,
    private val analytics: OrderSummaryAnalytics
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val ICON_URL_ENTRY_POINT_APPLIED = "https://images.tokopedia.net/img/promo/icon/Applied.png"
        private const val PROMO_TYPE_BEBAS_ONGKIR = "bebas_ongkir"
        const val VIEW_TYPE = 6

        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f
    }

    fun setupButtonPromo(orderPromo: OrderPromo) {
        if (orderPromo.isPromoRevamp) {
            renderNewButtonPromo(orderPromo)
        } else {
            renderOldButtonPromo(orderPromo)
        }
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
                    promocheckoutcommonR.drawable.ic_promo_checkout_chevron_right
            }

            OccButtonState.DISABLE -> {
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.INACTIVE
                binding.btnPromoCheckout.title =
                    binding.root.context.getString(purchase_platformcommonR.string.promo_checkout_inactive_label)
                binding.btnPromoCheckout.desc =
                    binding.root.context.getString(purchase_platformcommonR.string.promo_checkout_inactive_desc)
                binding.btnPromoCheckout.chevronIcon = promocheckoutcommonR.drawable.ic_promo_checkout_refresh
                binding.btnPromoCheckout.setOnClickListener {
                    if (!orderPromo.isDisabled) {
                        listener.onClickRetryValidatePromo()
                    }
                }
            }

            else -> {
                val lastApply = orderPromo.lastApply
                var title = binding.root.context.getString(purchase_platformcommonR.string.promo_funnel_label)
                if (lastApply.additionalInfo.messageInfo.message.isNotEmpty()) {
                    title = lastApply.additionalInfo.messageInfo.message
                } else if (lastApply.defaultEmptyPromoMessage.isNotBlank()) {
                    title = lastApply.defaultEmptyPromoMessage
                }
                binding.btnPromoCheckout.state = ButtonPromoCheckoutView.State.ACTIVE
                binding.btnPromoCheckout.title = title
                binding.btnPromoCheckout.desc = lastApply.additionalInfo.messageInfo.detail
                binding.btnPromoCheckout.chevronIcon =
                    promocheckoutcommonR.drawable.ic_promo_checkout_chevron_right

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

            else -> {
                val isUsingGlobalPromo = orderPromo.lastApply.codes.isNotEmpty() &&
                        orderPromo.lastApply.message.state != "red"
                val isUsingPromo = orderPromo.lastApply.voucherOrders
                    .any { it.code.isNotBlank() && it.message.state != "red" }
                val hasSummaries = orderPromo.lastApply.additionalInfo.usageSummaries.isNotEmpty()

                val entryPointInfo = orderPromo.entryPointInfo

                if (!isUsingGlobalPromo && !isUsingPromo && !hasSummaries) {
                    if (entryPointInfo != null) {
                        if (!entryPointInfo.isSuccess) {
                            if (entryPointInfo.statusCode == ResultStatus.STATUS_USER_BLACKLISTED
                                || entryPointInfo.statusCode == ResultStatus.STATUS_PHONE_NOT_VERIFIED
                                || entryPointInfo.statusCode == ResultStatus.STATUS_COUPON_LIST_EMPTY) {
                                val message = entryPointInfo.messages.firstOrNull().ifNull { "" }
                                if (entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN) {
                                    binding.btnPromoEntryPoint.showActiveNew(
                                        leftImageUrl = entryPointInfo.iconUrl,
                                        wording = message,
                                        rightIcon = IconUnify.CHEVRON_RIGHT,
                                        onClickListener = {
                                            if (entryPointInfo.isClickable) {
                                                listener.onClickPromo()
                                            }
                                        }
                                    )
                                } else {
                                    binding.btnPromoEntryPoint.showInactiveNew(
                                        leftImageUrl = entryPointInfo.iconUrl,
                                        wording = message,
                                        onClickListener = {
                                            if (entryPointInfo.isClickable) {
                                                listener.onClickPromo()
                                            }
                                        }
                                    )
                                }
                            } else {
                                binding.btnPromoEntryPoint.showError {
                                    listener.onClickRetryValidatePromo()
                                }
                            }
                        } else {
                            val message = entryPointInfo.messages.firstOrNull().ifNull { "" }
                            if (entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN) {
                                binding.btnPromoEntryPoint.showActiveNew(
                                    leftImageUrl = entryPointInfo.iconUrl,
                                    wording = message,
                                    rightIcon = IconUnify.CHEVRON_RIGHT,
                                    onClickListener = {
                                        if (entryPointInfo.isClickable) {
                                            listener.onClickPromo()
                                        }
                                    }
                                )
                            } else {
                                binding.btnPromoEntryPoint.showInactiveNew(
                                    leftImageUrl = entryPointInfo.iconUrl,
                                    wording = message,
                                    onClickListener = {
                                        if (entryPointInfo.isClickable) {
                                            listener.onClickPromo()
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        binding.btnPromoEntryPoint.showError {
                            listener.onClickRetryValidatePromo()
                        }
                    }
                } else {
                    val lastApply = orderPromo.lastApply
                    val message = when {
                        lastApply.additionalInfo.messageInfo.message.isNotBlank() -> {
                            lastApply.additionalInfo.messageInfo.message
                        }

                        lastApply.defaultEmptyPromoMessage.isNotBlank() -> {
                            lastApply.defaultEmptyPromoMessage
                        }

                        else -> {
                            ""
                        }
                    }
                    val boPromoSummaries = lastApply.additionalInfo.usageSummaries
                        .filter { it.type == PROMO_TYPE_BEBAS_ONGKIR }
                        .map {
                            PromoEntryPointSummaryItem(
                                title = it.description,
                                value = it.amountStr
                            )
                        }
                    val otherPromoSummaries = lastApply.additionalInfo.usageSummaries
                        .filter { it.type != PROMO_TYPE_BEBAS_ONGKIR }
                        .map {
                            PromoEntryPointSummaryItem(
                                title = it.description,
                                value = it.amountStr
                            )
                        }
                    val secondaryText = if (otherPromoSummaries.isEmpty()) {
                        entryPointInfo?.messages?.firstOrNull().ifNull { "" }
                    } else {
                        ""
                    }
                    val isSecondaryTextEnabled = otherPromoSummaries.isEmpty()
                            && secondaryText.isNotEmpty()
                            && entryPointInfo?.color == PromoEntryPointInfo.COLOR_GREEN
                    val isExpanded = boPromoSummaries.isNotEmpty() && isSecondaryTextEnabled
                    binding.btnPromoEntryPoint.showActiveNewExpandable(
                        leftImageUrl = ICON_URL_ENTRY_POINT_APPLIED,
                        wording = message,
                        firstLevelSummary = boPromoSummaries,
                        groupedSummary = otherPromoSummaries,
                        secondaryText = secondaryText,
                        isSecondaryTextEnabled = isSecondaryTextEnabled,
                        isExpanded = isExpanded,
                        animateWording = orderPromo.isAnimateWording,
                        onClickListener = {
                            listener.onClickPromo()
                        }
                    )
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
