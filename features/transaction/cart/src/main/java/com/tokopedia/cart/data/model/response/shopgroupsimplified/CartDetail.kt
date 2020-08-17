package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

data class CartDetail(
    @SerializedName("cart_id")
    val cartId: Int = 0,
    @SerializedName("errors")
    val errors: List<String> = ArrayList(),
    @SerializedName("messages")
    val messages: List<String> = ArrayList(),
    @SerializedName("product")
    val product: Product = Product(),
    @SerializedName("checkbox_state")
    val isCheckboxState: Boolean = false,
    @SerializedName("similar_product")
    val similarProduct: SimilarProduct? = null,
    @SerializedName("nicotine_lite_message")
    val nicotineLiteMessage: NicotineLiteMessage? = null
)