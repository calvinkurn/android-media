package com.tokopedia.purchase_platform.common.feature.promo_checkout.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2020-03-03.
 */
data class PromoMessageInfo (
        @SerializedName("message")
        var message: String = "",

        @SerializedName("detail")
        var detail: String = "")