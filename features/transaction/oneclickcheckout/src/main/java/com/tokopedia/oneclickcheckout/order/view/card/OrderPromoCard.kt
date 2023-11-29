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
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.promousage.domain.entity.PromoEntryPointSummaryItem
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.promocheckout.common.R as promocheckoutcommonR
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR

class OrderPromoCard(
    private val binding: CardOrderPromoBinding,
    private val listener: OrderPromoCardListener,
    private val analytics: OrderSummaryAnalytics
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val PROMO_TYPE_BEBAS_ONGKIR = "bebas_ongkir"
        const val VIEW_TYPE = 6

        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f
    }

    fun setupButtonPromo(orderPromo: OrderPromo) {
        if (orderPromo.isCartCheckoutRevamp) {
            if (orderPromo.isPromoRevamp) {
                renderNewButtonPromoWithNewBehavior(orderPromo)
            } else {
                renderNewButtonPromoWithOldBehavior(orderPromo)
            }
        } else {
            renderOldButtonPromo(orderPromo)
        }
    }

    private fun initPromoButton(enableNewInterface: Boolean) {
        if (binding.btnPromoEntryPoint.enableNewInterface != enableNewInterface) {
            binding.btnPromoEntryPoint.enableNewInterface = enableNewInterface
            binding.btnPromoEntryPoint.init()
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

    private fun renderNewButtonPromoWithOldBehavior(orderPromo: OrderPromo) {
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
                var title = binding.root.context.getString(purchase_platformcommonR.string.promo_funnel_label)
                if (lastApply.additionalInfo.messageInfo.message.isNotEmpty()) {
                    title = lastApply.additionalInfo.messageInfo.message
                } else if (lastApply.defaultEmptyPromoMessage.isNotBlank()) {
                    title = lastApply.defaultEmptyPromoMessage
                }
                val summaries = lastApply.additionalInfo.usageSummaries.map {
                    PromoEntryPointSummaryItem(
                        title = it.description,
                        value = it.amountStr,
                        subValue = it.currencyDetailsStr
                    )
                }
                if (summaries.isNotEmpty()) {
                    analytics.eventViewPromoAlreadyApplied()
                    binding.btnPromoEntryPoint.showApplied(
                        title = title,
                        desc = lastApply.additionalInfo.messageInfo.detail,
                        rightIcon = IconUnify.CHEVRON_RIGHT,
                        summaries = summaries,
                        onClickListener = {
                            if (!orderPromo.isDisabled) {
                                listener.onClickPromo()
                            }
                        }
                    )
                } else {
                    binding.btnPromoEntryPoint.showActive(
                        wording = title,
                        rightIcon = IconUnify.CHEVRON_RIGHT,
                        onClickListener = {
                            if (!orderPromo.isDisabled) {
                                listener.onClickPromo()
                            }
                        }
                    )
                }
            }
        }
        binding.root.alpha = if (orderPromo.isDisabled) DISABLE_ALPHA else ENABLE_ALPHA
    }

    private fun renderNewButtonPromoWithNewBehavior(orderPromo: OrderPromo) {
        initPromoButton(orderPromo.enableNewInterface)
        binding.btnPromoCheckout.gone()
        binding.btnPromoEntryPoint.visible()
        binding.dividerPromoEntryPointBottom.visible()
        when (orderPromo.state) {
            OccButtonState.LOADING -> {
                binding.btnPromoEntryPoint.showLoading()
            }

            else -> {
                val isUsingPromo = orderPromo.lastApply.additionalInfo.usageSummaries.isNotEmpty()
                val entryPointInfo = orderPromo.entryPointInfo

                val isUserBlacklisted = entryPointInfo.statusCode == ResultStatus.STATUS_USER_BLACKLISTED
                val isUserPhoneNotVerified = entryPointInfo.statusCode == ResultStatus.STATUS_PHONE_NOT_VERIFIED
                val isUserPromoEmpty = entryPointInfo.statusCode == ResultStatus.STATUS_COUPON_LIST_EMPTY

                if (!isUsingPromo) {
                    if (!entryPointInfo.isSuccess) {
                        if (isUserBlacklisted || isUserPhoneNotVerified || isUserPromoEmpty) {
                            renderPromoError(orderPromo, entryPointInfo)
                        } else {
                            renderPromoErrorDefault(orderPromo)
                        }
                    } else {
                        renderPromoNoneApplied(orderPromo, entryPointInfo)
                    }
                } else {
                    if (!entryPointInfo.isSuccess) {
                        if (isUserBlacklisted || isUserPhoneNotVerified) {
                            renderPromoError(orderPromo, entryPointInfo)
                        } else if (isUserPromoEmpty) {
                            renderPromoApplied(orderPromo, entryPointInfo)
                        } else {
                            renderPromoErrorDefault(orderPromo)
                        }
                    } else {
                        renderPromoApplied(orderPromo, entryPointInfo)
                    }
                }
            }
        }
        binding.root.alpha = if (orderPromo.isDisabled) DISABLE_ALPHA else ENABLE_ALPHA
    }

    private fun renderPromoError(orderPromo: OrderPromo, entryPointInfo: PromoEntryPointInfo) {
        val message = entryPointInfo.messages.firstOrNull().ifNull { "" }
        if (entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN) {
            binding.btnPromoEntryPoint.showActiveNew(
                leftImageUrl = entryPointInfo.iconUrl,
                wording = message,
                rightIcon = IconUnify.CHEVRON_RIGHT,
                onClickListener = {
                    if (entryPointInfo.isClickable && !orderPromo.isDisabled) {
                        listener.onClickPromo()
                        listener.sendClickUserSavingAndPromoEntryPointEvent(
                            entryPointMessages = listOf(message),
                            entryPointInfo = entryPointInfo,
                            lastApply = orderPromo.lastApply
                        )
                    }
                }
            )
        } else {
            binding.btnPromoEntryPoint.showInactiveNew(
                leftImageUrl = entryPointInfo.iconUrl,
                wording = message,
                onClickListener = {
                    if (entryPointInfo.isClickable && !orderPromo.isDisabled) {
                        listener.onClickPromo()
                        listener.sendClickUserSavingAndPromoEntryPointEvent(
                            entryPointMessages = listOf(message),
                            entryPointInfo = entryPointInfo,
                            lastApply = orderPromo.lastApply
                        )
                    }
                }
            )
        }
        listener.sendImpressionPromoEntryPointErrorEvent(
            errorMessage = message,
            lastApply = orderPromo.lastApply
        )
    }

    private fun renderPromoErrorDefault(orderPromo: OrderPromo) {
        binding.btnPromoEntryPoint.showError {
            listener.onClickRetryValidatePromo()
            val errorMessage = binding.btnPromoEntryPoint.context
                .getString(promocheckoutcommonR.string.promo_checkout_failed_label_new)
            listener.sendImpressionPromoEntryPointErrorEvent(
                errorMessage = errorMessage,
                lastApply = orderPromo.lastApply
            )
        }
    }

    private fun renderPromoNoneApplied(orderPromo: OrderPromo, entryPointInfo: PromoEntryPointInfo) {
        val message = entryPointInfo.messages.firstOrNull()
            .ifNull { binding.root.context.getString(purchase_platformcommonR.string.promo_funnel_label) }
        if (entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN) {
            binding.btnPromoEntryPoint.showActiveNew(
                leftImageUrl = entryPointInfo.iconUrl,
                wording = message,
                rightIcon = IconUnify.CHEVRON_RIGHT,
                onClickListener = {
                    if (entryPointInfo.isClickable && !orderPromo.isDisabled) {
                        listener.onClickPromo()
                    }
                }
            )
        } else {
            binding.btnPromoEntryPoint.showInactiveNew(
                leftImageUrl = entryPointInfo.iconUrl,
                wording = message,
                onClickListener = {
                    if (entryPointInfo.isClickable && !orderPromo.isDisabled) {
                        listener.onClickPromo()
                    }
                }
            )
        }
    }

    private fun renderPromoApplied(orderPromo: OrderPromo, entryPointInfo: PromoEntryPointInfo) {
        val lastApply = orderPromo.lastApply
        val message = when {
            lastApply.additionalInfo.messageInfo.message.isNotBlank() -> {
                lastApply.additionalInfo.messageInfo.message
            }

            lastApply.defaultEmptyPromoMessage.isNotBlank() -> {
                lastApply.defaultEmptyPromoMessage
            }

            else -> {
                binding.root.context.getString(purchase_platformcommonR.string.promo_funnel_label)
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
            entryPointInfo.messages.firstOrNull().ifNull { "" }
        } else {
            ""
        }
        val isSecondaryTextEnabled = otherPromoSummaries.isEmpty() &&
            secondaryText.isNotEmpty() &&
            entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN
        val isDetailExpanded = boPromoSummaries.isNotEmpty() && isSecondaryTextEnabled
        binding.btnPromoEntryPoint.showActiveNewExpandable(
            leftImageUrl = PromoEntryPointInfo.ICON_URL_ENTRY_POINT_APPLIED,
            wording = message,
            firstLevelSummary = boPromoSummaries,
            groupedSummary = otherPromoSummaries,
            secondaryText = secondaryText,
            isSecondaryTextEnabled = isSecondaryTextEnabled,
            isExpanded = isDetailExpanded,
            animateWording = orderPromo.isAnimateWording,
            onClickListener = {
                if (entryPointInfo.isClickable && !orderPromo.isDisabled) {
                    listener.onClickPromo()
                    listener.sendClickUserSavingDetailTotalSubsidyEvent(
                        entryPointMessages = listOf(message),
                        entryPointInfo = entryPointInfo,
                        lastApply = lastApply
                    )
                }
            },
            onChevronExpandClickListener = { isExpanded ->
                if (isExpanded) {
                    listener.sendClickUserSavingAndPromoEntryPointEvent(
                        entryPointMessages = listOf(message),
                        entryPointInfo = entryPointInfo,
                        lastApply = lastApply
                    )
                }
            },
            onSummaryExpandedListener = {
                listener.sendImpressionUserSavingDetailTotalSubsidyEvent(
                    entryPointMessages = listOf(message),
                    entryPointInfo = entryPointInfo,
                    lastApply = lastApply
                )
            }
        )
        listener.sendImpressionUserSavingTotalSubsidyEvent(
            entryPointMessages = listOf(message),
            entryPointInfo = entryPointInfo,
            lastApply = lastApply
        )
    }

    interface OrderPromoCardListener {

        fun onClickRetryValidatePromo()

        fun onClickPromo()

        fun sendImpressionUserSavingTotalSubsidyEvent(
            entryPointMessages: List<String>,
            entryPointInfo: PromoEntryPointInfo?,
            lastApply: LastApplyUiModel
        )

        fun sendClickUserSavingAndPromoEntryPointEvent(
            entryPointMessages: List<String>,
            entryPointInfo: PromoEntryPointInfo?,
            lastApply: LastApplyUiModel
        )

        fun sendImpressionUserSavingDetailTotalSubsidyEvent(
            entryPointMessages: List<String>,
            entryPointInfo: PromoEntryPointInfo?,
            lastApply: LastApplyUiModel
        )

        fun sendClickUserSavingDetailTotalSubsidyEvent(
            entryPointMessages: List<String>,
            entryPointInfo: PromoEntryPointInfo?,
            lastApply: LastApplyUiModel
        )

        fun sendImpressionPromoEntryPointErrorEvent(
            errorMessage: String,
            lastApply: LastApplyUiModel
        )
    }
}
