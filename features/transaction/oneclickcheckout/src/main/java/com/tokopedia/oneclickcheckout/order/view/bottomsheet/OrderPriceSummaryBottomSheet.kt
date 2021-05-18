package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderPriceSummaryBottomSheet {

    fun show(view: OrderSummaryPageFragment, orderCost: OrderCost) {
        view.fragmentManager?.let {
            BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showKnob = true
                showHeader = false
                showCloseIcon = false
                val child = View.inflate(view.context, R.layout.bottom_sheet_order_price_summary, null)
                view.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setupView(child, orderCost)
                setChild(child)
                show(it, null)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView(child: View, orderCost: OrderCost) {
        child.findViewById<Typography>(R.id.tv_total_product_price_value).text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.totalItemPrice, false).removeDecimalSuffix()

        val tvPurchaseProtectionPriceLabel = child.findViewById<Typography>(R.id.tv_purchase_protection_price_label)
        val tvPurchaseProtectionPriceValue = child.findViewById<Typography>(R.id.tv_purchase_protection_price_value)
        if (orderCost.purchaseProtectionPrice > 0) {
            tvPurchaseProtectionPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.purchaseProtectionPrice, false).removeDecimalSuffix()
            tvPurchaseProtectionPriceLabel.visible()
            tvPurchaseProtectionPriceValue.visible()
        } else {
            tvPurchaseProtectionPriceLabel.gone()
            tvPurchaseProtectionPriceValue.gone()
        }

        val tvTotalProductDiscountValue = child.findViewById<Typography>(R.id.tv_total_product_discount_value)
        val tvTotalProductDiscountLabel = child.findViewById<Typography>(R.id.tv_total_product_discount_label)
        if (orderCost.productDiscountAmount > 0) {
            tvTotalProductDiscountValue.text = "-${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.productDiscountAmount, false).removeDecimalSuffix()}"
            tvTotalProductDiscountValue.visible()
            tvTotalProductDiscountLabel.visible()
        } else {
            tvTotalProductDiscountValue.gone()
            tvTotalProductDiscountLabel.gone()
        }

        val tvTotalShippingPriceValue = child.findViewById<Typography>(R.id.tv_total_shipping_price_value)
        val tvTotalShippingDiscountValue = child.findViewById<Typography>(R.id.tv_total_shipping_discount_value)
        val tvTotalShippingDiscountLabel = child.findViewById<Typography>(R.id.tv_total_shipping_discount_label)
        if (orderCost.shippingDiscountAmount > 0 && orderCost.shippingDiscountAmount >= orderCost.shippingFee) {
            tvTotalShippingPriceValue.setText(com.tokopedia.purchase_platform.common.R.string.label_free_shipping)
            tvTotalShippingDiscountValue.gone()
            tvTotalShippingDiscountLabel.gone()
        } else {
            tvTotalShippingPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.shippingFee, false).removeDecimalSuffix()
            if (orderCost.shippingDiscountAmount > 0) {
                tvTotalShippingDiscountValue.text = "-${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.shippingDiscountAmount, false).removeDecimalSuffix()}"
                tvTotalShippingDiscountValue.visible()
                tvTotalShippingDiscountLabel.visible()
            } else {
                tvTotalShippingDiscountValue.gone()
                tvTotalShippingDiscountLabel.gone()
            }
        }

        val tvTotalInsurancePriceValue = child.findViewById<Typography>(R.id.tv_total_insurance_price_value)
        val tvTotalInsurancePriceLabel = child.findViewById<Typography>(R.id.tv_total_insurance_price_label)
        if (orderCost.insuranceFee > 0.0) {
            tvTotalInsurancePriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.insuranceFee, false).removeDecimalSuffix()
            tvTotalInsurancePriceLabel.visible()
            tvTotalInsurancePriceValue.visible()
        } else {
            tvTotalInsurancePriceLabel.gone()
            tvTotalInsurancePriceValue.gone()
        }

        val tvTotalPaymentFeePriceValue = child.findViewById<Typography>(R.id.tv_total_payment_fee_price_value)
        val tvTotalPaymentFeePriceLabel = child.findViewById<Typography>(R.id.tv_total_payment_fee_price_label)
        if (orderCost.paymentFee > 0.0) {
            tvTotalPaymentFeePriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.paymentFee, false).removeDecimalSuffix()
            tvTotalPaymentFeePriceLabel.visible()
            tvTotalPaymentFeePriceValue.visible()
        } else {
            tvTotalPaymentFeePriceLabel.gone()
            tvTotalPaymentFeePriceValue.gone()
        }

        child.findViewById<Typography>(R.id.tv_total_payment_price_value).text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.totalPrice, false).removeDecimalSuffix()

        val llCashback = child.findViewById<LinearLayout>(R.id.ll_cashback)
        val divider2 = child.findViewById<View>(R.id.divider2)
        if (orderCost.cashbacks.isNotEmpty()) {
            llCashback.removeAllViews()
            for (cashback in orderCost.cashbacks) {
                val view = View.inflate(child.context, R.layout.item_cashback_detail, null)
                view.findViewById<Typography>(R.id.tv_total_cashback_label).text = cashback.description
                view.findViewById<Typography>(R.id.tv_total_cashback_value).text = cashback.amountStr
                view.findViewById<Typography>(R.id.tv_total_cashback_currency_info).text = cashback.currencyDetailStr
                llCashback.addView(view)
                llCashback.visible()
                divider2.visible()
            }
        } else {
            llCashback.gone()
            divider2.gone()
        }
    }
}