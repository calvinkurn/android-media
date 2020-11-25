
package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class FreeShipping : Serializable {

    @SerializedName("imageUrl")
    var imageUrl: String? = null

    @SerializedName("isEligible")
    var isEligible: Boolean = false

    override fun toString(): String {
        return "FreeShipping{" +
                "imageUrl = '" + imageUrl + '\''.toString() +
                ",isEligible = '" + isEligible + '\''.toString() +
                "}"
    }
} 