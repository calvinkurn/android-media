package com.tokopedia.cartrevamp.data.model.response.promo

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2020-03-03.
 */
data class CartPromoData(
    @SerializedName("last_apply")
    var lastApplyPromo: LastApplyPromo = LastApplyPromo(),

    @SerializedName("ticker")
    var ticker: CartPromoTicker = CartPromoTicker(),

    @SerializedName("show_choose_promo_widget")
    var showChoosePromoWidget: Boolean = false
)
