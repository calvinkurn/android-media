package com.tokopedia.purchase_platform.features.cart.data.model.response.promo

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2020-03-03.
 */
class PromoErrorDetail (
        @SerializedName("message")
        var message: String = "")