package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

data class CartDetail(
    @SerializedName("cart_id")
    val cartId: Long = 0,
    @SerializedName("errors")
    val errors: List<String> = ArrayList(),
    @SerializedName("messages")
    val messages: List<String> = ArrayList(),
    @SerializedName("product")
    val product: Product = Product(),
    @SerializedName("checkbox_state")
    val isCheckboxState: Boolean = false,
    @SerializedName("selected_unavailable_action_link")
    val selectedUnavailableActionLink: String = ""
)