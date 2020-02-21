package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import android.view.View
import com.tokopedia.purchase_platform.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class OrderPriceSummaryBottomSheet {

    fun show(view: OrderSummaryPageFragment) {
        view.fragmentManager?.let {
            BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showKnob = true
                showHeader = false
                showCloseIcon = false
                val child = View.inflate(view.context, R.layout.bottomsheet_order_price_summary, null)
                view.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                show(it, null)
            }
        }
    }
}