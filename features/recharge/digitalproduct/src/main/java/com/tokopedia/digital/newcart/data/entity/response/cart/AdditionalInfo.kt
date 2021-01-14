package com.tokopedia.digital.newcart.data.entity.response.cart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 2/27/17.
 */

class AdditionalInfo {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("detail")
    @Expose
    var detail: List<Detail>? = null
}