package com.tokopedia.cart.data.model.response.promo

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2020-03-03.
 */
data class CartPromoData (
        @SerializedName("error_default")
        var errorDefault: ErrorDefault = ErrorDefault(),

        @SerializedName("last_apply")
        var lastApplyPromo: LastApplyPromo = LastApplyPromo())