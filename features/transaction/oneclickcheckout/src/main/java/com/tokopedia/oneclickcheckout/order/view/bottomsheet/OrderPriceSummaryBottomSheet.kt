package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.BottomSheetOrderPriceSummaryBinding
import com.tokopedia.oneclickcheckout.databinding.ItemCashbackDetailBinding
import com.tokopedia.oneclickcheckout.databinding.ItemPaymentFeeBinding
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderPriceSummaryBottomSheet {

    fun show(view: OrderSummaryPageFragment, orderCost: OrderCost) {
        view.parentFragmentManager.let {
            BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showKnob = true
                showHeader = false
                showCloseIcon = false
                val binding = BottomSheetOrderPriceSummaryBinding.inflate(LayoutInflater.from(view.context))
                view.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setupView(binding, orderCost, view)
                setChild(binding.root)
                show(it, null)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView(binding: BottomSheetOrderPriceSummaryBinding, orderCost: OrderCost, view: OrderSummaryPageFragment) {
        binding.tvTotalProductPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.totalItemPrice, false).removeDecimalSuffix()

        if (orderCost.hasAddOn) {
            binding.tvTotalProductAddonsPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.addOnPrice, false).removeDecimalSuffix()
            binding.tvTotalProductAddonsPriceLabel.visible()
            binding.tvTotalProductAddonsPriceValue.visible()
        } else {
            binding.tvTotalProductAddonsPriceLabel.gone()
            binding.tvTotalProductAddonsPriceValue.gone()
        }

        if (orderCost.purchaseProtectionPrice > 0) {
            binding.tvPurchaseProtectionPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.purchaseProtectionPrice, false).removeDecimalSuffix()
            binding.tvPurchaseProtectionPriceLabel.visible()
            binding.tvPurchaseProtectionPriceValue.visible()
        } else {
            binding.tvPurchaseProtectionPriceLabel.gone()
            binding.tvPurchaseProtectionPriceValue.gone()
        }

        if (orderCost.productDiscountAmount > 0) {
            binding.tvTotalProductDiscountValue.text = "-${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.productDiscountAmount, false).removeDecimalSuffix()}"
            binding.tvTotalProductDiscountValue.visible()
            binding.tvTotalProductDiscountLabel.visible()
        } else {
            binding.tvTotalProductDiscountValue.gone()
            binding.tvTotalProductDiscountLabel.gone()
        }

        if (orderCost.shippingDiscountAmount > 0 && orderCost.shippingDiscountAmount >= orderCost.shippingFee) {
            binding.tvTotalShippingPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(0.0, false).removeDecimalSuffix()
            binding.tvTotalShippingDiscountValue.gone()
            binding.tvTotalShippingDiscountLabel.gone()
        } else {
            binding.tvTotalShippingPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.shippingFee, false).removeDecimalSuffix()
            if (orderCost.shippingDiscountAmount > 0) {
                binding.tvTotalShippingDiscountValue.text = "-${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.shippingDiscountAmount, false).removeDecimalSuffix()}"
                binding.tvTotalShippingDiscountValue.visible()
                binding.tvTotalShippingDiscountLabel.visible()
            } else {
                binding.tvTotalShippingDiscountValue.gone()
                binding.tvTotalShippingDiscountLabel.gone()
            }
        }

        if (orderCost.insuranceFee > 0.0 || orderCost.isUseInsurance) {
            binding.tvTotalInsurancePriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.insuranceFee, false).removeDecimalSuffix()
            binding.tvTotalInsurancePriceLabel.visible()
            binding.tvTotalInsurancePriceValue.visible()
        } else {
            binding.tvTotalInsurancePriceLabel.gone()
            binding.tvTotalInsurancePriceValue.gone()
        }

        binding.tvTotalPaymentPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.totalPrice, false).removeDecimalSuffix()

        renderCashbacks(orderCost, binding)

        renderPaymentFee(view, binding, orderCost)

        renderInstallment(binding, orderCost)
    }

    private fun renderCashbacks(orderCost: OrderCost, binding: BottomSheetOrderPriceSummaryBinding) {
        if (orderCost.cashbacks.isNotEmpty()) {
            binding.llCashback.removeAllViews()
            for (cashback in orderCost.cashbacks) {
                val cashbackDetailBinding = ItemCashbackDetailBinding.inflate(LayoutInflater.from(binding.root.context))
                cashbackDetailBinding.tvTotalCashbackLabel.text = cashback.description
                cashbackDetailBinding.tvTotalCashbackValue.text = cashback.amountStr
                cashbackDetailBinding.tvTotalCashbackCurrencyInfo.text = cashback.currencyDetailStr
                binding.llCashback.addView(cashbackDetailBinding.root)
                binding.llCashback.visible()
                binding.dividerCashback.visible()
            }
        } else {
            binding.llCashback.gone()
            binding.dividerCashback.gone()
        }
    }

    private fun renderInstallment(binding: BottomSheetOrderPriceSummaryBinding, orderCost: OrderCost) {
        if (orderCost.installmentData != null) {
            binding.tvTotalInstallmentFeePriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.installmentData.installmentFee, false).removeDecimalSuffix()
            binding.tvTotalInstallmentFeePriceValue.visible()
            binding.tvTotalInstallmentFeePriceLabel.visible()

            binding.tvTotalInstallmentTermValue.text = binding.root.context.getString(R.string.occ_lbl_gocicil_installment_period, orderCost.installmentData.installmentTerm)
            binding.tvTotalInstallmentTermValue.visible()
            binding.tvTotalInstallmentTermLabel.visible()

            if (orderCost.installmentData.installmentAmountPerPeriod > 0) {
                binding.tvTotalInstallmentPerPeriodValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.installmentData.installmentAmountPerPeriod, false).removeDecimalSuffix()
                binding.tvTotalInstallmentPerPeriodValue.visible()
                binding.tvTotalInstallmentPerPeriodLabel.visible()
            } else {
                binding.tvTotalInstallmentPerPeriodValue.gone()
                binding.tvTotalInstallmentPerPeriodLabel.gone()
            }

            if (orderCost.installmentData.installmentFirstDate.isNotBlank()) {
                binding.tvTotalInstallmentFirstDateValue.text = orderCost.installmentData.installmentFirstDate
                binding.tvTotalInstallmentFirstDateValue.visible()
                binding.tvTotalInstallmentFirstDateLabel.visible()
            } else {
                binding.tvTotalInstallmentFirstDateValue.gone()
                binding.tvTotalInstallmentFirstDateLabel.gone()
            }

            if (orderCost.installmentData.installmentLastDate.isNotBlank()) {
                binding.tvTotalInstallmentLastDateValue.text = orderCost.installmentData.installmentLastDate
                binding.tvTotalInstallmentLastDateValue.visible()
                binding.tvTotalInstallmentLastDateLabel.visible()
            } else {
                binding.tvTotalInstallmentLastDateValue.gone()
                binding.tvTotalInstallmentLastDateLabel.gone()
            }

            binding.dividerInstallment.visible()
        } else {
            binding.tvTotalInstallmentFeePriceValue.gone()
            binding.tvTotalInstallmentFeePriceLabel.gone()
            binding.tvTotalInstallmentTermValue.gone()
            binding.tvTotalInstallmentTermLabel.gone()
            binding.tvTotalInstallmentPerPeriodValue.gone()
            binding.tvTotalInstallmentPerPeriodLabel.gone()
            binding.tvTotalInstallmentFirstDateValue.gone()
            binding.tvTotalInstallmentFirstDateLabel.gone()
            binding.tvTotalInstallmentLastDateValue.gone()
            binding.tvTotalInstallmentLastDateLabel.gone()
            binding.dividerInstallment.gone()
        }
    }

    private fun renderPaymentFee(
        view: OrderSummaryPageFragment,
        binding: BottomSheetOrderPriceSummaryBinding,
        orderCost: OrderCost
    ) {
        binding.llPaymentFee.removeAllViews()
        if (orderCost.isInstallment) {
            renderPaymentFeeView(
                view, binding, OrderPaymentFee(
                    title = view.getString(com.tokopedia.oneclickcheckout.R.string.occ_service_fee_title_info),
                    fee = orderCost.paymentFee,
                    showTooltip = true,
                    tooltipInfo = view.getString(com.tokopedia.oneclickcheckout.R.string.occ_service_fee_tooltip_info)
                )
            )
            showPaymentFeeSection(binding, true)
        }
        if (orderCost.orderPaymentFees.isNotEmpty()) {
            for (orderPaymentFee in orderCost.orderPaymentFees) {
                renderPaymentFeeView(view, binding, orderPaymentFee)
            }
            showPaymentFeeSection(binding, true)
        }
        else if (orderCost.orderPaymentFees.isEmpty() && !orderCost.isInstallment) {
            showPaymentFeeSection(binding, false)
        }
    }

    private fun renderPaymentFeeView(
        view: OrderSummaryPageFragment,
        binding: BottomSheetOrderPriceSummaryBinding,
        orderPaymentFee: OrderPaymentFee
    ) {
        val itemPaymentFeeBinding =
            ItemPaymentFeeBinding.inflate(LayoutInflater.from(binding.root.context))
        itemPaymentFeeBinding.apply {
            tvPaymentFeePriceLabel.text = orderPaymentFee.title
            if (orderPaymentFee.showSlashed) {
                tvPaymentFeeSlashPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderPaymentFee.slashedFee, false).removeDecimalSuffix()
                tvPaymentFeeSlashPriceValue.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                tvPaymentFeeSlashPriceValue.visible()
            } else {
                tvPaymentFeeSlashPriceValue.invisible()
            }
            tvPaymentFeePriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderPaymentFee.fee, false).removeDecimalSuffix()

            if (orderPaymentFee.showTooltip) {
                imgPaymentFeeInfo.visible()
                imgPaymentFeeInfo.setOnClickListener {
                    TransactionFeeInfoBottomSheet().show(view, orderPaymentFee)
                }
            } else {
                imgPaymentFeeInfo.gone()
            }
        }
        binding.llPaymentFee.addView(itemPaymentFeeBinding.root)
    }

    private fun showPaymentFeeSection(binding: BottomSheetOrderPriceSummaryBinding, isShown: Boolean) {
        if (isShown) {
            binding.dividerTransactionFee.visible()
            binding.llPaymentFee.visible()
            binding.tvTransactionFee.visible()
        }
        else {
            binding.dividerTransactionFee.gone()
            binding.llPaymentFee.gone()
            binding.tvTransactionFee.gone()
        }
    }
}