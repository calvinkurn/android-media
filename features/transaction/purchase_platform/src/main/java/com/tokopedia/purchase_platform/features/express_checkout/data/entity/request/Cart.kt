package com.tokopedia.purchase_platform.features.express_checkout.data.entity.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.data.model.request.checkout.CheckoutRequest

/**
 * Created by Irfan Khoirul on 11/01/19.
 */

data class Cart(
        @SerializedName("set_default_profile")
        var setDefaultProfile: Boolean? = false
) : CheckoutRequest()