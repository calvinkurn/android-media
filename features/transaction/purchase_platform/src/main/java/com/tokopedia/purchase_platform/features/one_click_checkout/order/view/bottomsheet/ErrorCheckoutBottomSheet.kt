package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet

import android.view.View
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class ErrorCheckoutBottomSheet{

    fun show(view: OrderSummaryPageFragment){
        view.fragmentManager?.let {
            BottomSheetUnify().apply {
                showCloseIcon = true
                showHeader = true

                val child = View.inflate(view.context, R.layout.bottom_sheet_error_checkout, null)
                view.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                show(it, null)
            }
        }
    }
}