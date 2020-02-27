package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.view.View
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheetListener
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import kotlinx.android.synthetic.main.card_order_preference.view.*

class OrderPreferenceCard(private val view: View, private val fragment: OrderSummaryPageFragment, private val listener: OrderPreferenceCardListener) {

    fun initView() {
        view.tv_shipping_price.setOnClickListener {
            val bottomsheet = ShippingCourierBottomsheet.newInstance(listOf(), RecipientAddressModel(), 0, true)
            bottomsheet.setShippingCourierBottomsheetListener(object : ShippingCourierBottomsheetListener {
                override fun onCourierChoosen(shippingCourierViewModel: ShippingCourierViewModel, courierItemData: CourierItemData, recipientAddressModel: RecipientAddressModel, cartPosition: Int, isCod: Boolean, isPromoCourier: Boolean, isNeedPinpoint: Boolean) {

                }

                override fun onCourierShipmentRecpmmendationCloseClicked() {

                }

                override fun onRetryReloadCourier(shipmentCartItemModel: ShipmentCartItemModel?, cartPosition: Int, shopShipmentList: MutableList<ShopShipment>?) {

                }
            })
            bottomsheet.show(fragment.fragmentManager!!, null)
        }
        view.tv_choose_preference.setOnClickListener {
            listener.onChangePreferenceClicked()
        }
    }

    interface OrderPreferenceCardListener {

        fun onChangePreferenceClicked()
    }
}