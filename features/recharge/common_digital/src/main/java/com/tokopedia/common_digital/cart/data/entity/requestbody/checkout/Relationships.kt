package com.tokopedia.common_digital.cart.data.entity.requestbody.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/9/17.
 */

class Relationships(@field:SerializedName("cart")
                    @field:Expose
                    var cart: Cart?) {
}
