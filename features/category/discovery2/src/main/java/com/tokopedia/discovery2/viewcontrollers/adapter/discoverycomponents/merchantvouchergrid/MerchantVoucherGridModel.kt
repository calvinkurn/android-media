package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel

data class MerchantVoucherGridModel(
    val buttonText: String,
    val appLink: String,
    val url: String,
    val automateCouponModel: AutomateCouponModel.Grid
)
