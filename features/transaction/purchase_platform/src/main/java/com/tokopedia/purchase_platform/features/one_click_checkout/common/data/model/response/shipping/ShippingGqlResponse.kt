package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping

import com.google.gson.annotations.SerializedName

class ShippingGqlResponse (
    @SerializedName("get_shipping_rates")
    val data: Response
)