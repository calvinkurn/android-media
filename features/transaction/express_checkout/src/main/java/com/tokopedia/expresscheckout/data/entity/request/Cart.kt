package com.tokopedia.expresscheckout.data.entity.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.transactiondata.entity.request.CheckoutRequest

/**
 * Created by Irfan Khoirul on 11/01/19.
 */

data class Cart(
        @SerializedName("set_default_profile")
        var setDefaultProfile: Boolean? = false
) : CheckoutRequest()