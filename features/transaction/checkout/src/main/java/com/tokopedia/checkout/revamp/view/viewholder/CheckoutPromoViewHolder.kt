package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutPromoBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promocheckout.common.view.uimodel.PromoEntryPointSummaryItem
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR

class CheckoutPromoViewHolder(private val binding: ItemCheckoutPromoBinding, private val listener: CheckoutAdapterListener) :
    RecyclerView.ViewHolder(binding.root) {

    private var isApplied = false

    fun bind(promoModel: CheckoutPromoModel) {
        if (!promoModel.isEnable) {
            binding.root.gone()
            return
        } else {
            binding.root.visible()
        }

        val promo = promoModel.promo
        if (promoModel.isPromoRevamp) {
            processNewEntryPointInfo(promoModel)
        } else {
            processOldEntryPointInfo(promo)
        }
    }

    private fun processNewEntryPointInfo(model: CheckoutPromoModel) {
        val lastApply = model.promo
        val entryPointInfo = model.entryPointInfo
        if (!model.isLoading) {
            val defaultMessage = itemView.context.getString(purchase_platformcommonR.string.promo_funnel_label)
            if (!entryPointInfo.isSuccess) {
                if (entryPointInfo.statusCode == ResultStatus.STATUS_USER_BLACKLISTED ||
                    entryPointInfo.statusCode == ResultStatus.STATUS_PHONE_NOT_VERIFIED ||
                    entryPointInfo.statusCode == ResultStatus.STATUS_COUPON_LIST_EMPTY
                ) {
                    val message = entryPointInfo.messages.firstOrNull()
                        .ifNull { defaultMessage }
                    if (entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN) {
                        binding.btnCheckoutPromo.showActiveNew(
                            leftImageUrl = entryPointInfo.iconUrl,
                            wording = message,
                            rightIcon = IconUnify.CHEVRON_RIGHT,
                            onClickListener = {
                                if (entryPointInfo.isClickable) {
                                    listener.onClickPromoCheckout(lastApply)
                                }
                            }
                        )
                    } else {
                        binding.btnCheckoutPromo.showInactiveNew(
                            leftImageUrl = entryPointInfo.iconUrl,
                            wording = message,
                            onClickListener = {
                                if (entryPointInfo.isClickable) {
                                    listener.onClickPromoCheckout(lastApply)
                                }
                            }
                        )
                    }
                } else {
                    binding.btnCheckoutPromo.showError {
                        listener.onClickReloadPromoWidget()
                    }
                }
            } else {
                val isUsingPromo = lastApply.additionalInfo.usageSummaries.isNotEmpty()
                if (!isUsingPromo) {
                    val message = entryPointInfo.messages.firstOrNull()
                        .ifNull { defaultMessage }
                    if (entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN) {
                        binding.btnCheckoutPromo.showActiveNew(
                            leftImageUrl = entryPointInfo.iconUrl,
                            wording = message,
                            rightIcon = IconUnify.CHEVRON_RIGHT,
                            onClickListener = {
                                if (entryPointInfo.isClickable) {
                                    listener.onClickPromoCheckout(lastApply)
                                }
                            }
                        )
                    } else {
                        binding.btnCheckoutPromo.showInactiveNew(
                            leftImageUrl = entryPointInfo.iconUrl,
                            wording = message,
                            onClickListener = {
                                if (entryPointInfo.isClickable) {
                                    listener.onClickPromoCheckout(lastApply)
                                }
                            }
                        )
                    }
                } else {
                    val message = when {
                        lastApply.additionalInfo.messageInfo.message.isNotBlank() -> {
                            lastApply.additionalInfo.messageInfo.message
                        }

                        lastApply.defaultEmptyPromoMessage.isNotBlank() -> {
                            lastApply.defaultEmptyPromoMessage
                        }

                        else -> {
                            defaultMessage
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
                    val isExpanded = boPromoSummaries.isNotEmpty() && isSecondaryTextEnabled
                    binding.btnCheckoutPromo.showActiveNewExpandable(
                        leftImageUrl = PromoEntryPointInfo.ICON_URL_ENTRY_POINT_APPLIED,
                        wording = message,
                        firstLevelSummary = boPromoSummaries,
                        groupedSummary = otherPromoSummaries,
                        secondaryText = secondaryText,
                        isSecondaryTextEnabled = isSecondaryTextEnabled,
                        isExpanded = isExpanded,
                        animateWording = model.isAnimateWording,
                        onClickListener = {
                            if (entryPointInfo.isClickable) {
                                listener.onClickPromoCheckout(lastApply)
                            }
                        }
                    )
                }
            }
        } else {
            binding.btnCheckoutPromo.showLoading()
        }
    }

    private fun processOldEntryPointInfo(promo: LastApplyUiModel) {
        val titleValue: String
        when {
            promo.additionalInfo.messageInfo.message.isNotEmpty() -> {
                titleValue = promo.additionalInfo.messageInfo.message
                if (promo.additionalInfo.usageSummaries.isNotEmpty()) {
                    isApplied = true
                    listener.onSendAnalyticsViewPromoCheckoutApplied()
                } else {
                    isApplied = false
                }
            }
            promo.defaultEmptyPromoMessage.isNotBlank() -> {
                titleValue = promo.defaultEmptyPromoMessage
                isApplied = false
            }
            else -> {
                titleValue = itemView.context.getString(purchase_platformcommonR.string.promo_funnel_label)
                isApplied = false
            }
        }

        if (isApplied) {
            binding.btnCheckoutPromo.showApplied(
                titleValue,
                promo.additionalInfo.messageInfo.detail,
                IconUnify.CHEVRON_RIGHT,
                promo.additionalInfo.usageSummaries.map { PromoEntryPointSummaryItem(it.description, it.amountStr, it.currencyDetailsStr) },
                showConfetti = true
            ) {
                listener.onClickPromoCheckout(promo)
                listener.onSendAnalyticsClickPromoCheckout(isApplied, getAllPromosApplied(promo))
            }
        } else {
            binding.btnCheckoutPromo.showActive(
                titleValue,
                IconUnify.CHEVRON_RIGHT
            ) {
                listener.onClickPromoCheckout(promo)
                listener.onSendAnalyticsClickPromoCheckout(isApplied, getAllPromosApplied(promo))
            }
        }
    }

    private fun getAllPromosApplied(lastApplyData: LastApplyUiModel): List<String> {
        val listPromos = arrayListOf<String>()
        lastApplyData.codes.forEach {
            listPromos.add(it)
        }
        lastApplyData.voucherOrders.forEach {
            listPromos.add(it.code)
        }
        return listPromos
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_promo

        private const val PROMO_TYPE_BEBAS_ONGKIR = "bebas_ongkir"
    }
}
