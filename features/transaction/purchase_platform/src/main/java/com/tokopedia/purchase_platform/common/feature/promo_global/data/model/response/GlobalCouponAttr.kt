package com.tokopedia.purchase_platform.common.feature.promo_global.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 14/04/19.
 */
data class GlobalCouponAttr(
    @SerializedName("description")
    var description: String = "",

    @SerializedName("quantity_label")
    var quantityLabel: String = ""
)
