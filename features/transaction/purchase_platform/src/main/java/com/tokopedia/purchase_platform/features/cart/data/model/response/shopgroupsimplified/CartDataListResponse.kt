package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.data.model.response.Messages
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.AutoApplyStack
import com.tokopedia.purchase_platform.common.feature.promo_global.data.model.response.GlobalCouponAttr
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
    @SerializedName("global_coupon_attr")
    @Expose
    val globalCouponAttr: GlobalCouponAttr = GlobalCouponAttr(),
    @SerializedName("autoapply_stack")
    @Expose
    val autoApplyStack: AutoApplyStack = AutoApplyStack(),
    @SerializedName("global_checkbox_state")
    @Expose
    val isGlobalCheckboxState: Boolean = false
)