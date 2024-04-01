package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel

data class MerchantVoucherCarouselModel(
    val buttonText: String,
    val appLink: String,
    val url: String,
    val automateCouponModel: AutomateCouponModel.List
)
