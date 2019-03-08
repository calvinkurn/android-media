package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/7/17.
 */

class RelationshipsVoucher {

    @SerializedName("category")
    @Expose
    var category: Category? = null
    @SerializedName("cart")
    @Expose
    var cart: Cart? = null
}
