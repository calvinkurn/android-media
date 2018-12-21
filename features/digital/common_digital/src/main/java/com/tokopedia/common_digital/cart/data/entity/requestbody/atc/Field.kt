package com.tokopedia.common_digital.cart.data.entity.requestbody.atc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 27/08/18.
 */
class Field {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null

}
