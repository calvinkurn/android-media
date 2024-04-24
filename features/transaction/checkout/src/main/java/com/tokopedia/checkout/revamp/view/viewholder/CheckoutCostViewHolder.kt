package com.tokopedia.checkout.revamp.view.viewholder

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutCostBinding
import com.tokopedia.checkout.databinding.ItemCheckoutCostDynamicBinding
import com.tokopedia.checkout.databinding.ItemCheckoutCostPaymentDynamicBinding
import com.tokopedia.checkout.revamp.view.CheckoutViewModel
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.checkoutpayment.view.OrderPaymentFee
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CheckoutCostViewHolder(
    private val binding: ItemCheckoutCostBinding,
    private val layoutInflater: LayoutInflater,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cost: CheckoutCostModel) {
        binding.tvCheckoutCostItemPriceTitle.text =
            getTotalItemLabel(binding.tvCheckoutCostItemPriceTitle.context, cost.totalItem)
        renderItemPrice(cost)
        renderShippingPrice(cost)
        if (cost.totalItem > 0) {
            renderPlatformFee(cost.dynamicPlatformFee)
        } else {
            hidePlatformFee()
        }

        renderOtherFee(cost)
        renderPaymentFee(cost)
        renderPaymentWordings(cost)

        binding.tvCheckoutCostTotalValue.setTextAndContentDescription(
            cost.totalPriceString,
            R.string.content_desc_tv_total_payment
        )
    }

    private fun renderShippingPrice(cost: CheckoutCostModel) {
        if (cost.finalShippingFee < cost.originalShippingFee) {
            binding.tvCheckoutCostShippingTitle.isVisible = true
            binding.tvCheckoutCostShippingValue.isVisible = true
            binding.tvCheckoutCostShippingSlashedValue.setTextAndContentDescription(
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    cost.originalShippingFee,
                    false
                ).removeDecimalSuffix(),
                R.string.content_desc_tv_shipping_fee_summary
            )
            binding.tvCheckoutCostShippingSlashedValue.paintFlags =
                binding.tvCheckoutCostShippingSlashedValue.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvCheckoutCostShippingSlashedValue.isVisible = true
            binding.tvCheckoutCostShippingValue.setTextColorCompat(unifyprinciplesR.color.Unify_TN500)
            binding.tvCheckoutCostShippingValue.setTextAndContentDescription(
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    cost.finalShippingFee,
                    false
                ).removeDecimalSuffix(),
                R.string.content_desc_tv_shipping_fee_summary
            )
        } else if (cost.originalShippingFee == 0.0 && !cost.hasSelectAllShipping) {
            binding.tvCheckoutCostShippingTitle.isVisible = false
            binding.tvCheckoutCostShippingSlashedValue.isVisible = false
            binding.tvCheckoutCostShippingValue.isVisible = false
        } else {
            binding.tvCheckoutCostShippingTitle.isVisible = true
            binding.tvCheckoutCostShippingValue.isVisible = true
            binding.tvCheckoutCostShippingSlashedValue.isVisible = false
            binding.tvCheckoutCostShippingValue.setTextColorCompat(unifyprinciplesR.color.Unify_NN950)
            binding.tvCheckoutCostShippingValue.setTextAndContentDescription(
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    cost.originalShippingFee,
                    false
                ).removeDecimalSuffix(),
                R.string.content_desc_tv_shipping_fee_summary
            )
        }
    }

    private fun renderItemPrice(cost: CheckoutCostModel) {
        if (cost.finalItemPrice < cost.originalItemPrice) {
            binding.tvCheckoutCostItemPriceSlashedValue.setTextAndContentDescription(
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    cost.originalItemPrice,
                    false
                ).removeDecimalSuffix(),
                R.string.content_desc_tv_total_item_price_summary
            )
            binding.tvCheckoutCostItemPriceSlashedValue.paintFlags =
                binding.tvCheckoutCostItemPriceSlashedValue.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvCheckoutCostItemPriceSlashedValue.isVisible = true
            binding.tvCheckoutCostItemPriceValue.setTextColorCompat(unifyprinciplesR.color.Unify_TN500)
            binding.tvCheckoutCostItemPriceValue.setTextAndContentDescription(
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    cost.finalItemPrice,
                    false
                ).removeDecimalSuffix(),
                R.string.content_desc_tv_total_item_price_summary
            )
        } else {
            binding.tvCheckoutCostItemPriceSlashedValue.isVisible = false
            binding.tvCheckoutCostItemPriceValue.setTextColorCompat(unifyprinciplesR.color.Unify_NN950)
            binding.tvCheckoutCostItemPriceValue.setTextAndContentDescription(
                if (cost.originalItemPrice == 0.0) {
                    "-"
                } else {
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        cost.originalItemPrice,
                        false
                    ).removeDecimalSuffix()
                },
                R.string.content_desc_tv_total_item_price_summary
            )
        }
    }

    private fun getTotalItemLabel(context: Context, totalItem: Int): String {
        return String.format(
            context.getString(R.string.label_item_count_summary_with_format),
            totalItem
        )
    }

    private fun hidePlatformFee() {
        binding.tickerPlatformFeeInfo.gone()
        binding.tvCheckoutCostPlatformFeeTitle.gone()
        binding.tvCheckoutCostPlatformFeeValue.gone()
        binding.tvCheckoutCostPlatformFeeSlashedValue.gone()
        binding.icCheckoutCostPlatformFee.gone()
        binding.loaderPlatformFeeLabel.gone()
        binding.loaderPlatformFeeValue.gone()
    }

    private fun renderPlatformFee(platformFeeModel: ShipmentPaymentFeeModel) {
        if (platformFeeModel.isLoading) {
            binding.tickerPlatformFeeInfo.gone()
            binding.tvCheckoutCostPlatformFeeTitle.gone()
            binding.tvCheckoutCostPlatformFeeValue.gone()
            binding.tvCheckoutCostPlatformFeeSlashedValue.gone()
            binding.icCheckoutCostPlatformFee.gone()
            binding.loaderPlatformFeeLabel.visible()
            binding.loaderPlatformFeeValue.visible()
        } else if (platformFeeModel.isShowTicker) {
            binding.loaderPlatformFeeLabel.gone()
            binding.tvCheckoutCostPlatformFeeValue.gone()
            binding.tvCheckoutCostPlatformFeeSlashedValue.gone()
            binding.tvCheckoutCostPlatformFeeTitle.gone()
            binding.loaderPlatformFeeLabel.gone()
            binding.loaderPlatformFeeValue.gone()
            binding.icCheckoutCostPlatformFee.gone()
            binding.tickerPlatformFeeInfo.visible()
            binding.tickerPlatformFeeInfo.setHtmlDescription(platformFeeModel.ticker)
            binding.tickerPlatformFeeInfo.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    listener.checkPlatformFee()
                }

                override fun onDismiss() {
                    /* no-op */
                }
            })
        } else {
            binding.tickerPlatformFeeInfo.gone()

            if (platformFeeModel.title.isEmpty()) {
                binding.loaderPlatformFeeLabel.gone()
                binding.loaderPlatformFeeValue.gone()
                binding.tvCheckoutCostPlatformFeeValue.gone()
                binding.tvCheckoutCostPlatformFeeSlashedValue.gone()
                binding.tvCheckoutCostPlatformFeeTitle.gone()
                binding.icCheckoutCostPlatformFee.gone()
            } else {
                binding.loaderPlatformFeeLabel.gone()
                binding.loaderPlatformFeeValue.gone()
                binding.tvCheckoutCostPlatformFeeTitle.visible()
                binding.tvCheckoutCostPlatformFeeTitle.text = platformFeeModel.title
                binding.tvCheckoutCostPlatformFeeValue.visible()

                if (platformFeeModel.isShowSlashed) {
                    binding.tvCheckoutCostPlatformFeeSlashedValue.visible()
                    binding.tvCheckoutCostPlatformFeeSlashedValue.paintFlags =
                        binding.tvCheckoutCostPlatformFeeSlashedValue.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.tvCheckoutCostPlatformFeeSlashedValue.text =
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            platformFeeModel.slashedFee.toLong(),
                            false
                        ).removeDecimalSuffix()

                    binding.tvCheckoutCostPlatformFeeValue.text =
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            platformFeeModel.fee.toLong(),
                            false
                        ).removeDecimalSuffix()
                    binding.tvCheckoutCostPlatformFeeValue.setTextColor(
                        ContextCompat.getColor(
                            binding.tvCheckoutCostPlatformFeeValue.context,
                            unifyprinciplesR.color.Unify_TN500
                        )
                    )
                } else {
                    binding.tvCheckoutCostPlatformFeeSlashedValue.gone()
                    binding.tvCheckoutCostPlatformFeeValue.text =
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            platformFeeModel.fee.toLong(),
                            false
                        ).removeDecimalSuffix()
                    binding.tvCheckoutCostPlatformFeeValue.setTextColor(
                        ContextCompat.getColor(
                            binding.tvCheckoutCostPlatformFeeValue.context,
                            unifyprinciplesR.color.Unify_NN950
                        )
                    )
                }

                if (platformFeeModel.isShowTooltip) {
                    binding.icCheckoutCostPlatformFee.visible()
                    binding.icCheckoutCostPlatformFee.setOnClickListener {
                        listener.showPlatformFeeTooltipInfoBottomSheet(platformFeeModel)
                    }
                } else {
                    binding.icCheckoutCostPlatformFee.gone()
                }
            }
        }
    }

    private fun renderOtherFee(cost: CheckoutCostModel) {
        val insuranceCourierList =
            if (cost.hasInsurance) listOf(cost.shippingInsuranceFee) else emptyList()
        val giftingList = if (cost.hasAddOn) listOf(cost.totalAddOnPrice) else emptyList()
        val egoldList = if (cost.emasPrice > 0.0) listOf(cost.emasPrice) else emptyList()
        val donationList = if (cost.donation > 0.0) listOf(cost.donation) else emptyList()
        if ((insuranceCourierList.size + cost.listAddOnSummary.size + giftingList.size + cost.listCrossSell.size + egoldList.size + donationList.size) > 2) {
            // render in collapsable group
            binding.tvCheckoutCostOthersValue.text =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(cost.totalOtherFee, false)
                    .removeDecimalSuffix()
            binding.icCheckoutCostOthersToggle.setOnClickListener {
                if (binding.llCheckoutCostOthersExpanded.isVisible) {
                    cost.isExpandOtherFee = false
                    binding.llCheckoutCostOthersExpanded.isVisible = false
                    binding.vCheckoutCostOthersExpandedSeparator.isVisible = false
                    binding.tvCheckoutCostOthersValue.isVisible = true
                    binding.icCheckoutCostOthersToggle.setImage(IconUnify.CHEVRON_DOWN)
                } else {
                    cost.isExpandOtherFee = true
                    binding.llCheckoutCostOthersExpanded.isVisible = true
                    binding.vCheckoutCostOthersExpandedSeparator.isVisible = true
                    binding.tvCheckoutCostOthersValue.isVisible = false
                    binding.icCheckoutCostOthersToggle.setImage(IconUnify.CHEVRON_UP)
                }
            }
            binding.llCheckoutCostOthersExpanded.removeAllViews()
            insuranceCourierList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthersExpanded,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.setText(R.string.checkout_label_total_shipping_insurance)
                itemBinding.tvCheckoutCostItemValue.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        it,
                        false
                    ).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }
            cost.listAddOnSummary.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthersExpanded,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text = it.wording
                itemBinding.tvCheckoutCostItemValue.text = it.priceLabel
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }
            cost.listCrossSell.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthersExpanded,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text = it.crossSellModel.orderSummary.title
                itemBinding.tvCheckoutCostItemValue.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(it.crossSellModel.price, false)
                        .removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }
            giftingList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthersExpanded,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.setText(R.string.checkout_label_total_gifting)
                itemBinding.tvCheckoutCostItemValue.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        it,
                        false
                    ).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }
            egoldList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthersExpanded,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text =
                    binding.root.resources.getString(R.string.label_emas)
                itemBinding.tvCheckoutCostItemValue.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(it, false).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }
            donationList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthersExpanded,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text =
                    binding.root.resources.getString(R.string.label_donation)
                itemBinding.tvCheckoutCostItemValue.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(it, false).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }

            binding.llCheckoutCostOthers.isVisible = false
            binding.tvCheckoutCostOthersTitle.isVisible = true
            binding.icCheckoutCostOthersToggle.isVisible = true
            if (cost.isExpandOtherFee) {
                binding.tvCheckoutCostOthersValue.isVisible = false
                binding.llCheckoutCostOthersExpanded.isVisible = true
                binding.vCheckoutCostOthersExpandedSeparator.isVisible = true
                binding.icCheckoutCostOthersToggle.setImage(IconUnify.CHEVRON_UP)
            } else {
                binding.tvCheckoutCostOthersValue.isVisible = true
                binding.llCheckoutCostOthersExpanded.isVisible = false
                binding.vCheckoutCostOthersExpandedSeparator.isVisible = false
                binding.icCheckoutCostOthersToggle.setImage(IconUnify.CHEVRON_DOWN)
            }
        } else {
            // render outside
            binding.tvCheckoutCostOthersTitle.isVisible = false
            binding.icCheckoutCostOthersToggle.isVisible = false
            binding.tvCheckoutCostOthersValue.isVisible = false
            binding.llCheckoutCostOthersExpanded.isVisible = false
            binding.vCheckoutCostOthersExpandedSeparator.isVisible = false

            binding.llCheckoutCostOthers.removeAllViews()
            insuranceCourierList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthers,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.setText(R.string.checkout_label_total_shipping_insurance)
                itemBinding.tvCheckoutCostItemValue.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        it,
                        false
                    ).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            cost.listAddOnSummary.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthers,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text = it.wording
                itemBinding.tvCheckoutCostItemValue.text = it.priceLabel
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            cost.listCrossSell.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthers,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text = it.crossSellModel.orderSummary.title
                itemBinding.tvCheckoutCostItemValue.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(it.crossSellModel.price, false)
                        .removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            giftingList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthers,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.setText(R.string.checkout_label_total_gifting)
                itemBinding.tvCheckoutCostItemValue.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        it,
                        false
                    ).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            egoldList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthers,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text =
                    binding.root.resources.getString(R.string.label_emas)
                itemBinding.tvCheckoutCostItemValue.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(it, false).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            donationList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthers,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text =
                    binding.root.resources.getString(R.string.label_donation)
                itemBinding.tvCheckoutCostItemValue.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(it, false).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                    8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            binding.llCheckoutCostOthers.isVisible = true
        }
    }

    private fun renderPaymentFee(cost: CheckoutCostModel) {
        if (cost.usePaymentFees) {
            val paymentFees = cost.dynamicPaymentFees?.filter {
                !it.code.equals(CheckoutViewModel.PLATFORM_FEE_CODE,
                    ignoreCase = true)
            } ?: emptyList()

            val originalPaymentFees = cost.originalPaymentFees

            val installmentServiceFee = if (cost.isInstallment) {
                listOf(cost.installmentFee)
            } else {
                emptyList()
            }

            val installmentFee = if (cost.isInstallment && cost.installmentDetail != null) {
                listOf(cost.installmentDetail.interestAmount)
            } else {
                emptyList()
            }

            if ((paymentFees.size + originalPaymentFees.size + installmentServiceFee.size + installmentFee.size) > 1) {
                // render in collapsible
                binding.llCheckoutCostPaymentsExpanded.removeAllViews()
                binding.tvCheckoutCostPaymentFeeTitle.isVisible = false
                binding.icCheckoutCostPaymentFee.isVisible = false
                binding.tvCheckoutCostPaymentFeeSlashedValue.isVisible = false
                binding.tvCheckoutCostPaymentFeeValue.isVisible = false

                var totalPaymentFee = 0.0

                val titleString = binding.root.context.getString(R.string.checkout_service_fee_title_info)
                val descString = binding.root.context.getString(R.string.checkout_service_fee_tooltip_info)
                for (installmentService in installmentServiceFee) {
                    totalPaymentFee += installmentService
                    val itemBinding = ItemCheckoutCostPaymentDynamicBinding.inflate(
                        layoutInflater,
                        binding.llCheckoutCostPaymentsExpanded,
                        false
                    )
                    itemBinding.tvCheckoutCostPaymentFeeTitle.text = titleString
                    itemBinding.icCheckoutCostPaymentFee.isVisible = true
                    itemBinding.icCheckoutCostPaymentFee.setOnClickListener {
                        listener.showPaymentFeeTooltipInfoBottomSheet(
                            OrderPaymentFee(
                                title = titleString,
                                tooltipInfo = descString
                            )
                        )
                    }
                    itemBinding.tvCheckoutCostPaymentFeeSlashedValue.isVisible = false
                    itemBinding.tvCheckoutCostPaymentFeeValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(installmentService, false).removeDecimalSuffix()
                    (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                        8.dpToPx(binding.root.context.resources.displayMetrics)
                    binding.llCheckoutCostPaymentsExpanded.addView(itemBinding.root)
                }

                for (originalPaymentFee in originalPaymentFees) {
                    totalPaymentFee += originalPaymentFee.amount
                    val itemBinding = ItemCheckoutCostPaymentDynamicBinding.inflate(
                        layoutInflater,
                        binding.llCheckoutCostPaymentsExpanded,
                        false
                    )
                    itemBinding.tvCheckoutCostPaymentFeeTitle.text = originalPaymentFee.title
                    itemBinding.icCheckoutCostPaymentFee.isVisible = originalPaymentFee.showTooltip
                    itemBinding.icCheckoutCostPaymentFee.setOnClickListener {
                        if (originalPaymentFee.showTooltip) {
                            listener.showPaymentFeeTooltipInfoBottomSheet(
                                OrderPaymentFee(
                                    title = originalPaymentFee.title,
                                    tooltipInfo = originalPaymentFee.tooltipInfo
                                )
                            )
                        }
                    }
                    itemBinding.tvCheckoutCostPaymentFeeSlashedValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPaymentFee.slashedFee, false).removeDecimalSuffix()
                    itemBinding.tvCheckoutCostPaymentFeeSlashedValue.isVisible = originalPaymentFee.showSlashed
                    itemBinding.tvCheckoutCostPaymentFeeSlashedValue.paintFlags =
                        itemBinding.tvCheckoutCostPaymentFeeSlashedValue.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    itemBinding.tvCheckoutCostPaymentFeeValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPaymentFee.amount, false).removeDecimalSuffix()
                    (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                        8.dpToPx(binding.root.context.resources.displayMetrics)
                    binding.llCheckoutCostPaymentsExpanded.addView(itemBinding.root)
                }

                for (paymentFee in paymentFees) {
                    totalPaymentFee += paymentFee.fee
                    val itemBinding = ItemCheckoutCostPaymentDynamicBinding.inflate(
                        layoutInflater,
                        binding.llCheckoutCostPaymentsExpanded,
                        false
                    )
                    itemBinding.tvCheckoutCostPaymentFeeTitle.text = paymentFee.title
                    itemBinding.icCheckoutCostPaymentFee.isVisible = paymentFee.showTooltip
                    itemBinding.icCheckoutCostPaymentFee.setOnClickListener {
                        if (paymentFee.showTooltip) {
                            listener.showPaymentFeeTooltipInfoBottomSheet(
                                OrderPaymentFee(
                                    title = paymentFee.title,
                                    tooltipInfo = paymentFee.tooltipInfo
                                )
                            )
                        }
                    }
                    itemBinding.tvCheckoutCostPaymentFeeSlashedValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(paymentFee.slashedFee, false).removeDecimalSuffix()
                    itemBinding.tvCheckoutCostPaymentFeeSlashedValue.isVisible = paymentFee.showSlashed
                    itemBinding.tvCheckoutCostPaymentFeeSlashedValue.paintFlags =
                        itemBinding.tvCheckoutCostPaymentFeeSlashedValue.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    itemBinding.tvCheckoutCostPaymentFeeValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(paymentFee.fee, false).removeDecimalSuffix()
                    (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                        8.dpToPx(binding.root.context.resources.displayMetrics)
                    binding.llCheckoutCostPaymentsExpanded.addView(itemBinding.root)
                }

                val installmentTitleString = binding.root.context.getString(R.string.checkout_lbl_installment_fee)
                for (installment in installmentFee) {
                    totalPaymentFee += installment
                    val itemBinding = ItemCheckoutCostPaymentDynamicBinding.inflate(
                        layoutInflater,
                        binding.llCheckoutCostPaymentsExpanded,
                        false
                    )
                    itemBinding.tvCheckoutCostPaymentFeeTitle.text = installmentTitleString
                    itemBinding.icCheckoutCostPaymentFee.isVisible = false
                    itemBinding.tvCheckoutCostPaymentFeeSlashedValue.isVisible = false
                    itemBinding.tvCheckoutCostPaymentFeeValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(installment, false).removeDecimalSuffix()
                    (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin =
                        8.dpToPx(binding.root.context.resources.displayMetrics)
                    binding.llCheckoutCostPaymentsExpanded.addView(itemBinding.root)
                }

                binding.tvCheckoutCostPaymentsTitle.isVisible = true
                binding.tvCheckoutCostPaymentsValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPaymentFee, false).removeDecimalSuffix()
                binding.icCheckoutCostPaymentsToggle.isVisible = true
                if (cost.isExpandPaymentFee) {
                    binding.tvCheckoutCostPaymentsValue.isVisible = false
                    binding.llCheckoutCostPaymentsExpanded.isVisible = true
                    binding.vCheckoutCostPaymentsExpandedSeparator.isVisible = true
                    binding.icCheckoutCostPaymentsToggle.setImage(IconUnify.CHEVRON_UP)
                } else {
                    binding.tvCheckoutCostPaymentsValue.isVisible = true
                    binding.llCheckoutCostPaymentsExpanded.isVisible = false
                    binding.vCheckoutCostPaymentsExpandedSeparator.isVisible = false
                    binding.icCheckoutCostPaymentsToggle.setImage(IconUnify.CHEVRON_DOWN)
                }

                binding.icCheckoutCostPaymentsToggle.setOnClickListener {
                    if (binding.llCheckoutCostPaymentsExpanded.isVisible) {
                        cost.isExpandPaymentFee = false
                        binding.llCheckoutCostPaymentsExpanded.isVisible = false
                        binding.vCheckoutCostPaymentsExpandedSeparator.isVisible = false
                        binding.tvCheckoutCostPaymentsValue.isVisible = true
                        binding.icCheckoutCostPaymentsToggle.setImage(IconUnify.CHEVRON_DOWN)
                    } else {
                        cost.isExpandPaymentFee = true
                        binding.llCheckoutCostPaymentsExpanded.isVisible = true
                        binding.vCheckoutCostPaymentsExpandedSeparator.isVisible = true
                        binding.tvCheckoutCostPaymentsValue.isVisible = false
                        binding.icCheckoutCostPaymentsToggle.setImage(IconUnify.CHEVRON_UP)
                    }
                }
            } else if (paymentFees.isNotEmpty()) {
                // render outside
                val paymentFee = paymentFees.first()
                binding.apply {
                    tvCheckoutCostPaymentFeeTitle.text = paymentFee.title
                    tvCheckoutCostPaymentFeeTitle.isVisible = true
                    icCheckoutCostPaymentFee.isVisible = paymentFee.showTooltip
                    icCheckoutCostPaymentFee.setOnClickListener {
                        if (paymentFee.showTooltip) {
                            listener.showPaymentFeeTooltipInfoBottomSheet(paymentFee)
                        }
                    }
                    tvCheckoutCostPaymentFeeSlashedValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(paymentFee.slashedFee, false).removeDecimalSuffix()
                    tvCheckoutCostPaymentFeeSlashedValue.isVisible = paymentFee.showSlashed
                    tvCheckoutCostPaymentFeeSlashedValue.paintFlags =
                        tvCheckoutCostPaymentFeeSlashedValue.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvCheckoutCostPaymentFeeValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(paymentFee.fee, false).removeDecimalSuffix()
                    tvCheckoutCostPaymentFeeValue.isVisible = true
                    tvCheckoutCostPaymentsTitle.isVisible = false
                    tvCheckoutCostPaymentsValue.isVisible = false
                    icCheckoutCostPaymentsToggle.isVisible = false
                    vCheckoutCostPaymentsExpandedSeparator.isVisible = false
                    llCheckoutCostPaymentsExpanded.isVisible = false
                }
            } else if (originalPaymentFees.isNotEmpty()) {
                // render outside
                val paymentFee = originalPaymentFees.first()
                binding.apply {
                    tvCheckoutCostPaymentFeeTitle.text = paymentFee.title
                    tvCheckoutCostPaymentFeeTitle.isVisible = true
                    icCheckoutCostPaymentFee.isVisible = paymentFee.showTooltip
                    icCheckoutCostPaymentFee.setOnClickListener {
                        if (paymentFee.showTooltip) {
                            listener.showPaymentFeeTooltipInfoBottomSheet(
                                OrderPaymentFee(
                                    title = paymentFee.title,
                                    tooltipInfo = paymentFee.tooltipInfo
                                )
                            )
                        }
                    }
                    tvCheckoutCostPaymentFeeSlashedValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(paymentFee.slashedFee, false).removeDecimalSuffix()
                    tvCheckoutCostPaymentFeeSlashedValue.isVisible = paymentFee.showSlashed
                    tvCheckoutCostPaymentFeeSlashedValue.paintFlags =
                        tvCheckoutCostPaymentFeeSlashedValue.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvCheckoutCostPaymentFeeValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(paymentFee.amount, false).removeDecimalSuffix()
                    tvCheckoutCostPaymentFeeValue.isVisible = true
                    tvCheckoutCostPaymentsTitle.isVisible = false
                    tvCheckoutCostPaymentsValue.isVisible = false
                    icCheckoutCostPaymentsToggle.isVisible = false
                    vCheckoutCostPaymentsExpandedSeparator.isVisible = false
                    llCheckoutCostPaymentsExpanded.isVisible = false
                }
            } else if (installmentServiceFee.isNotEmpty()) {
                // render outside
                val paymentFee = installmentServiceFee.first()
                binding.apply {
                    val titleString = binding.root.context.getString(R.string.checkout_service_fee_title_info)
                    val descString = binding.root.context.getString(R.string.checkout_service_fee_tooltip_info)
                    tvCheckoutCostPaymentFeeTitle.text = titleString
                    tvCheckoutCostPaymentFeeTitle.isVisible = true
                    icCheckoutCostPaymentFee.isVisible = true
                    icCheckoutCostPaymentFee.setOnClickListener {
                        listener.showPaymentFeeTooltipInfoBottomSheet(
                            OrderPaymentFee(
                                title = titleString,
                                tooltipInfo = descString
                            )
                        )
                    }
                    tvCheckoutCostPaymentFeeSlashedValue.isVisible = false
                    tvCheckoutCostPaymentFeeValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(paymentFee, false).removeDecimalSuffix()
                    tvCheckoutCostPaymentFeeValue.isVisible = true
                    tvCheckoutCostPaymentsTitle.isVisible = false
                    tvCheckoutCostPaymentsValue.isVisible = false
                    icCheckoutCostPaymentsToggle.isVisible = false
                    vCheckoutCostPaymentsExpandedSeparator.isVisible = false
                    llCheckoutCostPaymentsExpanded.isVisible = false
                }
            } else if (installmentFee.isNotEmpty()) {
                // render outside
                val paymentFee = installmentFee.first()
                binding.apply {
                    tvCheckoutCostPaymentFeeTitle.text = binding.root.context.getString(R.string.checkout_lbl_installment_fee)
                    tvCheckoutCostPaymentFeeTitle.isVisible = true
                    icCheckoutCostPaymentFee.isVisible = false
                    tvCheckoutCostPaymentFeeSlashedValue.isVisible = false
                    tvCheckoutCostPaymentFeeValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(paymentFee, false).removeDecimalSuffix()
                    tvCheckoutCostPaymentFeeValue.isVisible = true
                    tvCheckoutCostPaymentsTitle.isVisible = false
                    tvCheckoutCostPaymentsValue.isVisible = false
                    icCheckoutCostPaymentsToggle.isVisible = false
                    vCheckoutCostPaymentsExpandedSeparator.isVisible = false
                    llCheckoutCostPaymentsExpanded.isVisible = false
                }
            } else {
                binding.apply {
                    tvCheckoutCostPaymentFeeTitle.isVisible = false
                    icCheckoutCostPaymentFee.isVisible = false
                    tvCheckoutCostPaymentFeeSlashedValue.isVisible = false
                    tvCheckoutCostPaymentFeeValue.isVisible = false
                    tvCheckoutCostPaymentsTitle.isVisible = false
                    tvCheckoutCostPaymentsValue.isVisible = false
                    icCheckoutCostPaymentsToggle.isVisible = false
                    vCheckoutCostPaymentsExpandedSeparator.isVisible = false
                    llCheckoutCostPaymentsExpanded.isVisible = false
                }
            }
        } else {
            binding.apply {
                tvCheckoutCostPaymentFeeTitle.isVisible = false
                icCheckoutCostPaymentFee.isVisible = false
                tvCheckoutCostPaymentFeeSlashedValue.isVisible = false
                tvCheckoutCostPaymentFeeValue.isVisible = false
                tvCheckoutCostPaymentsTitle.isVisible = false
                tvCheckoutCostPaymentsValue.isVisible = false
                icCheckoutCostPaymentsToggle.isVisible = false
                vCheckoutCostPaymentsExpandedSeparator.isVisible = false
                llCheckoutCostPaymentsExpanded.isVisible = false
            }
        }
    }

    private fun renderPaymentWordings(cost: CheckoutCostModel) {
        if (cost.useNewWording) {
            binding.tvCheckoutCostTotalTitle.text = binding.root.context.getString(R.string.checkout_cost_total_with_payment_title)
            binding.tvCheckoutCostHeader.text = binding.root.context.getString(R.string.checkout_cost_with_payment_header_title)
        } else {
            binding.tvCheckoutCostTotalTitle.text = binding.root.context.getString(R.string.checkout_cost_total_title)
            binding.tvCheckoutCostHeader.text = binding.root.context.getString(R.string.checkout_cost_header_title)
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_cost
    }
}
