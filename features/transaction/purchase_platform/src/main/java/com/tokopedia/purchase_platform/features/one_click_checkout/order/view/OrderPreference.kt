package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference

data class OrderPreference(val preference: Preference = Preference(), val shipping: OrderShipping? = null)