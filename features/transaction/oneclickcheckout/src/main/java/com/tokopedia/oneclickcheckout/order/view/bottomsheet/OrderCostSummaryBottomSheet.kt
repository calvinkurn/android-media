package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.BottomSheetOrderCostSummaryBinding
import com.tokopedia.oneclickcheckout.databinding.ItemCashbackDetailBinding
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderCostSummaryBottomSheet {

    fun show(view: OrderSummaryPageFragment, orderCost: OrderCost) {
        view.parentFragmentManager.let {
            BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showKnob = false
                showHeader = true
                showCloseIcon = true
                val binding = BottomSheetOrderCostSummaryBinding.inflate(LayoutInflater.from(view.context))
                view.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setTitle(binding.root.context.getString(R.string.occ_lbl_order_summary))
                setupView(binding, orderCost)
                setChild(binding.root)
                show(it, null)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView(binding: BottomSheetOrderCostSummaryBinding, orderCost: OrderCost) {
        binding.tvTotalProductPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.totalItemPrice, false).removeDecimalSuffix()

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
            binding.tvTotalShippingPriceValue.setText(com.tokopedia.purchase_platform.common.R.string.label_free_shipping)
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

        if (orderCost.paymentFee > 0.0) {
            binding.tvTotalPaymentFeePriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.paymentFee, false).removeDecimalSuffix()
            binding.tvTotalPaymentFeePriceLabel.visible()
            binding.tvTotalPaymentFeePriceValue.visible()
        } else {
            binding.tvTotalPaymentFeePriceLabel.gone()
            binding.tvTotalPaymentFeePriceValue.gone()
        }

        binding.tvTotalPaymentPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.totalPrice, false).removeDecimalSuffix()

        renderCashbacks(binding, orderCost)

        renderInstallment(binding, orderCost)
    }

    private fun renderCashbacks(binding: BottomSheetOrderCostSummaryBinding, orderCost: OrderCost) {
        if (orderCost.cashbacks.isNotEmpty()) {
            binding.llCashback.removeAllViews()
            val layoutInflater = LayoutInflater.from(binding.root.context)
            for (cashback in orderCost.cashbacks) {
                val cashbackDetailBinding = ItemCashbackDetailBinding.inflate(layoutInflater)
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

    private fun renderInstallment(binding: BottomSheetOrderCostSummaryBinding, orderCost: OrderCost) {
        if (orderCost.installmentData == null) {
//            binding.tvTotalInstallmentFeePriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.installmentData.installmentFee, false).removeDecimalSuffix()
            binding.tvTotalInstallmentFeePriceValue.text = "blablabla"
            binding.tvTotalInstallmentFeePriceValue.visible()
            binding.tvTotalInstallmentFeePriceLabel.visible()

            binding.tvTotalInstallmentTermValue.text = "orderCost.installmentData.installmentTerm"
            binding.tvTotalInstallmentTermValue.visible()
            binding.tvTotalInstallmentTermLabel.visible()

            binding.tvTotalInstallmentPerPeriodValue.text = "orderCost.installmentData.installmentAmountPerPeriod"
            binding.tvTotalInstallmentPerPeriodValue.visible()
            binding.tvTotalInstallmentPerPeriodLabel.visible()

            binding.tvTotalInstallmentFirstDateValue.text = "orderCost.installmentData.installmentFirstDate"
            binding.tvTotalInstallmentFirstDateValue.visible()
            binding.tvTotalInstallmentFirstDateLabel.visible()

            binding.tvTotalInstallmentLastDateValue.text = "orderCost.installmentData.installmentLastDate"
            binding.tvTotalInstallmentLastDateValue.visible()
            binding.tvTotalInstallmentLastDateLabel.visible()

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
}