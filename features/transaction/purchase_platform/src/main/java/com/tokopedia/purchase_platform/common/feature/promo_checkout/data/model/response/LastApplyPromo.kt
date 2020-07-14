package com.tokopedia.purchase_platform.common.feature.promo_checkout.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2020-03-03.
 */
data class LastApplyPromo (
        @SerializedName("data")
        var lastApplyPromoData: LastApplyPromoData = LastApplyPromoData(),

        @SerializedName("code")
        var code: String = "")