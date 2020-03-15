package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProfileResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.Shipment

data class OrderPreference(val preference: ProfileResponse = ProfileResponse(), val shipping: Shipment? = null)