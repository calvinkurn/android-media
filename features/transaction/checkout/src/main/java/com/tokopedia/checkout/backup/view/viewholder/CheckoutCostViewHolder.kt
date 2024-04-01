package com.tokopedia.checkout.backup.view.viewholder

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
import com.tokopedia.checkout.backup.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.backup.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
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

                override fun onDismiss() {}
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
        val insuranceCourierList = if (cost.hasInsurance) listOf(cost.shippingInsuranceFee) else emptyList()
        val giftingList = if (cost.hasAddOn) listOf(cost.totalAddOnPrice) else emptyList()
        val egoldList = if (cost.emasPrice > 0.0) listOf(cost.emasPrice) else emptyList()
        val donationList = if (cost.donation > 0.0) listOf(cost.donation) else emptyList()
        if ((insuranceCourierList.size + cost.listAddOnSummary.size + giftingList.size + cost.listCrossSell.size + egoldList.size + donationList.size) > 2) {
            // render in collapsable group
            binding.tvCheckoutCostOthersValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(cost.totalOtherFee, false).removeDecimalSuffix()
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
                itemBinding.tvCheckoutCostItemValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    it,
                    false
                ).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
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
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }
            cost.listCrossSell.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthersExpanded,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text = it.crossSellModel.orderSummary.title
                itemBinding.tvCheckoutCostItemValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.crossSellModel.price, false).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }
            giftingList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthersExpanded,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.setText(R.string.checkout_label_total_gifting)
                itemBinding.tvCheckoutCostItemValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    it,
                    false
                ).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }
            egoldList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthersExpanded,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text = binding.root.resources.getString(R.string.label_emas)
                itemBinding.tvCheckoutCostItemValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it, false).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }
            donationList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthersExpanded,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text = binding.root.resources.getString(R.string.label_donation)
                itemBinding.tvCheckoutCostItemValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it, false).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthersExpanded.addView(itemBinding.root)
            }

            binding.llCheckoutCostOthers.isVisible = false
            binding.tvCheckoutCostOthersTitle.isVisible = true
            binding.icCheckoutCostOthersToggle.isVisible = true
            if (cost.isExpandOtherFee) {
                binding.tvCheckoutCostOthersValue.isVisible = false
                binding.llCheckoutCostOthersExpanded.isVisible = true
                binding.vCheckoutCostOthersExpandedSeparator.isVisible = true
                binding.icCheckoutCostOthersToggle.setImage(IconUnify.CHEVRON_DOWN)
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
                itemBinding.tvCheckoutCostItemValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    it,
                    false
                ).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
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
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            cost.listCrossSell.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthers,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text = it.crossSellModel.orderSummary.title
                itemBinding.tvCheckoutCostItemValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.crossSellModel.price, false).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            giftingList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthers,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.setText(R.string.checkout_label_total_gifting)
                itemBinding.tvCheckoutCostItemValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    it,
                    false
                ).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            egoldList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthers,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text = binding.root.resources.getString(R.string.label_emas)
                itemBinding.tvCheckoutCostItemValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it, false).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            donationList.forEach {
                val itemBinding = ItemCheckoutCostDynamicBinding.inflate(
                    layoutInflater,
                    binding.llCheckoutCostOthers,
                    false
                )
                itemBinding.tvCheckoutCostItemTitle.text = binding.root.resources.getString(R.string.label_donation)
                itemBinding.tvCheckoutCostItemValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it, false).removeDecimalSuffix()
                (itemBinding.root.layoutParams as? MarginLayoutParams)?.topMargin = 8.dpToPx(binding.root.context.resources.displayMetrics)
                binding.llCheckoutCostOthers.addView(itemBinding.root)
            }
            binding.llCheckoutCostOthers.isVisible = true
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_cost
    }
}
