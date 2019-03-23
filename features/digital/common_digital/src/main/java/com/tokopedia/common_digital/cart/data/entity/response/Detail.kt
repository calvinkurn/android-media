package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 2/27/17.
 */

class Detail {

    @SerializedName("label")
    @Expose
    var label: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null
}
