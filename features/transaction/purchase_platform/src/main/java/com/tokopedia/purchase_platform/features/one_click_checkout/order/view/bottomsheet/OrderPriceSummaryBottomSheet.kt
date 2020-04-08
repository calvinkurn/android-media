package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderCost
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.bottom_sheet_order_price_summary.view.*

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
        child.tv_total_product_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.totalItemPrice, false)

        val tvTotalProductDiscountValue = child.tv_total_product_discount_value
        val tvTotalProductDiscountLabel = child.tv_total_product_discount_label
        if (orderCost.productDiscountAmount > 0) {
            tvTotalProductDiscountValue.text = "-${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.productDiscountAmount, false)}"
            tvTotalProductDiscountValue.visible()
            tvTotalProductDiscountLabel.visible()
        } else {
            tvTotalProductDiscountValue.gone()
            tvTotalProductDiscountLabel.gone()
        }

        val tvTotalShippingPriceValue = child.tv_total_shipping_price_value
        val tvTotalShippingDiscountValue = child.tv_total_shipping_discount_value
        val tvTotalShippingDiscountLabel = child.tv_total_shipping_discount_label
        if (orderCost.shippingDiscountAmount > 0 && orderCost.shippingDiscountAmount >= orderCost.shippingFee) {
            tvTotalShippingPriceValue.setText(R.string.label_free_shipping)
            tvTotalShippingDiscountValue.gone()
            tvTotalShippingDiscountLabel.gone()
        } else {
            tvTotalShippingPriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.shippingFee, false)
            if (orderCost.shippingDiscountAmount > 0) {
                tvTotalShippingDiscountValue.text = "-${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.shippingDiscountAmount, false)}"
                tvTotalShippingDiscountValue.visible()
                tvTotalShippingDiscountLabel.visible()
            } else {
                tvTotalShippingDiscountValue.gone()
                tvTotalShippingDiscountLabel.gone()
            }
        }

        val tvTotalInsurancePriceValue = child.tv_total_insurance_price_value
        val tvTotalInsurancePriceLabel = child.tv_total_insurance_price_label
        if (orderCost.insuranceFee > 0.0) {
            tvTotalInsurancePriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.insuranceFee, false)
            tvTotalInsurancePriceLabel.visible()
            tvTotalInsurancePriceValue.visible()
        } else {
            tvTotalInsurancePriceLabel.gone()
            tvTotalInsurancePriceValue.gone()
        }

        val tvTotalPaymentFeePriceValue = child.tv_total_payment_fee_price_value
        val tvTotalPaymentFeePriceLabel = child.tv_total_payment_fee_price_label
        if (orderCost.paymentFee > 0.0) {
            tvTotalPaymentFeePriceValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.paymentFee, false)
            tvTotalPaymentFeePriceLabel.visible()
            tvTotalPaymentFeePriceValue.visible()
        } else {
            tvTotalPaymentFeePriceLabel.gone()
            tvTotalPaymentFeePriceValue.gone()
        }

        child.tv_total_payment_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.totalPrice, false)

        val llCashback = child.ll_cashback
        val divider2 = child.divider2
        if (orderCost.cashbacks.isNotEmpty()) {
            llCashback.removeAllViews()
            for (cashback in orderCost.cashbacks) {
                val view = View.inflate(child.context, R.layout.item_cashback_detail, null)
                view.findViewById<Typography>(R.id.tv_total_cashback_label).text = cashback.first
                view.findViewById<Typography>(R.id.tv_total_cashback_value).text = cashback.second
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