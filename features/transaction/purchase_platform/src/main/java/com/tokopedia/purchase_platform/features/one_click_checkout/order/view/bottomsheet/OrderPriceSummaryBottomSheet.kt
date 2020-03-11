package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet

import android.view.View
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderCost
import com.tokopedia.unifycomponents.BottomSheetUnify
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

    private fun setupView(child: View, orderCost: OrderCost) {
        child.tv_total_product_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.totalItemPrice, false)
        child.tv_total_shipping_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.shippingFee, false)
        if (orderCost.insuranceFee > 0.0) {
            child.tv_total_insurance_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.insuranceFee, false)
            child.tv_total_insurance_price_label.visible()
            child.tv_total_insurance_price_value.visible()
        } else {
            child.tv_total_insurance_price_label.gone()
            child.tv_total_insurance_price_value.gone()
        }
        child.tv_total_payment_price_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderCost.totalPrice, false)
    }
}