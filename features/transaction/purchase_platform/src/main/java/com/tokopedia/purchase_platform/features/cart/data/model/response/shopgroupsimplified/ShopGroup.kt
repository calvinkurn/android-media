package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

data class ShopGroup(
    @SerializedName("user_address_id")
    @Expose
    val userAddressId: Int = 0,
    @SerializedName("errors")
    @Expose
    val errors: List<String> = ArrayList(),
    @SerializedName("sort_key")
    @Expose
    val sortKey: Int = 0,
    @SerializedName("shop")
    @Expose
    val shop: Shop = Shop(),
    @SerializedName("is_fulfillment_service")
    @Expose
    val isFulFillment: Boolean = false,
    @SerializedName("warehouse")
    @Expose
    val warehouse: Warehouse? = null,
    @SerializedName("cart_string")
    @Expose
    val cartString: String = "",
    @SerializedName("cart_details")
    @Expose
    val cartDetails: List<CartDetail> = ArrayList(),
    @SerializedName("has_promo_list")
    @Expose
    val hasPromoList: Boolean = false,
    @SerializedName("checkbox_state")
    @Expose
    val isCheckboxState: Boolean = false
)
