package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProfileResponse

data class OrderPreference(val preference: ProfileResponse = ProfileResponse(), val shipping: OrderShipping? = null)