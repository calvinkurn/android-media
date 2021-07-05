package com.tokopedia.digital_checkout.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 21/01/21
 */

data class CheckoutRelationships(
        @SerializedName("cart")
        @Expose
        var cart: Cart = Cart()
) {
    data class Cart(
            @SerializedName("data")
            @Expose
            var data: Data = Data()) {

        data class Data(
                @SerializedName("type")
                @Expose
                var type: String = "",

                @SerializedName("id")
                @Expose
                var id: String = ""
        )
    }
}