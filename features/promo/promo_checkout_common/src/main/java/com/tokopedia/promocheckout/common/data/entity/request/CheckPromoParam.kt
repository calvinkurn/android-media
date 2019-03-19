package com.tokopedia.promocheckout.common.data.entity.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 19/03/19.
 */

data class CheckPromoParam(
        @SerializedName("promo")
        var promo: CheckPromoFirstStepParam? = null
)