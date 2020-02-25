package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.view.View
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import kotlinx.android.synthetic.main.card_order_preference.view.*

class OrderPreferenceCard(private val view: View, private val fragment: OrderSummaryPageFragment, private val listener: OrderPreferenceCardListener) {

    fun initView() {
        view.tv_choose_preference.setOnClickListener {
            listener.onChangePreferenceClicked()
        }
    }

    interface OrderPreferenceCardListener {

        fun onChangePreferenceClicked()
    }
}