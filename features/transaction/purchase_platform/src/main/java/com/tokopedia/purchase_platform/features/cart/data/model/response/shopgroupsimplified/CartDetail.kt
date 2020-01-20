package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

data class CartDetail(
    @SerializedName("cart_id")
    @Expose
    val cartId: Int = 0,
    @SerializedName("errors")
    @Expose
    val errors: List<String> = ArrayList(),
    @SerializedName("messages")
    @Expose
    val messages: List<String> = ArrayList(),
    @SerializedName("product")
    @Expose
    val product: Product = Product(),
    @SerializedName("checkbox_state")
    @Expose
    val isCheckboxState: Boolean = false,
    @SerializedName("similar_product")
    @Expose
    val similarProduct: SimilarProduct? = null,
    @SerializedName("nicotine_lite_message")
    @Expose
    val nicotineLiteMessage: NicotineLiteMessage? = null
)