package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
interface ShippingCourierAdapterListener {
    fun onCourierChoosen(shippingCourierUiModel: ShippingCourierUiModel, cartPosition: Int, isNeedPinpoint: Boolean)
}
