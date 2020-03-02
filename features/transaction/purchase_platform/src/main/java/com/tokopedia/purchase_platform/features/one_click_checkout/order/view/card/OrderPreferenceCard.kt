package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.view.View
import com.tokopedia.logisticcart.shipping.features.shippingcourierocc.ShippingCourierOccBottomSheet
import com.tokopedia.logisticcart.shipping.features.shippingcourierocc.ShippingCourierOccBottomSheetListener
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import kotlinx.android.synthetic.main.card_order_preference.view.*

class OrderPreferenceCard(private val view: View, private val fragment: OrderSummaryPageFragment, private val listener: OrderPreferenceCardListener) {

    private lateinit var preference: Preference

    fun setPreference(preference: Preference) {
        this.preference = preference
    }

    fun initView() {
        view.tv_shipping_price.setOnClickListener {
            ShippingCourierOccBottomSheet().showBottomSheet(fragment, listOf(), object : ShippingCourierOccBottomSheetListener {
                override fun onCourierChosen(shippingCourierViewModel: ShippingCourierViewModel) {

                }

                override fun onLogisticPromoClicked(data: LogisticPromoViewModel) {

                }
            })
        }
        view.tv_choose_preference.setOnClickListener {
            listener.onChangePreferenceClicked()
        }
    }

    interface OrderPreferenceCardListener {

        fun onChangePreferenceClicked()

        fun onCourierChange()
    }
}