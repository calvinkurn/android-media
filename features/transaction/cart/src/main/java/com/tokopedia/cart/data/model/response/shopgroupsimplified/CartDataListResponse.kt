package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Messages
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker
import java.util.*

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class CartDataListResponse(
    @SerializedName("errors")
    @Expose
    val errors: List<String> = ArrayList(),
    @SerializedName("tickers")
    @Expose
    val tickers: List<Ticker> = ArrayList(),
    @SerializedName("is_coupon_active")
    @Expose
    val isCouponActive: Int = 0,
    @SerializedName("max_quantity")
    @Expose
    val maxQuantity: Int = 0,
    @SerializedName("max_char_note")
    @Expose
    val maxCharNote: Int = 0,
    @SerializedName("messages")
    @Expose
    val messages: Messages = Messages(),
    @SerializedName("shop_group_available")
    @Expose
    val shopGroupAvailables: List<ShopGroupAvailable> = ArrayList(),
    @SerializedName("shop_group_with_errors")
    @Expose
    val shopGroupWithErrors: List<ShopGroupWithError> = ArrayList(),
    @SerializedName("donation")
    @Expose
    val donation: Donation = Donation(),
    @SerializedName("global_checkbox_state")
    @Expose
    val isGlobalCheckboxState: Boolean = false,
    @SerializedName("promo")
    @Expose
    val promo: CartPromoData = CartPromoData()
)