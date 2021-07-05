package com.tokopedia.digital.newcart.data.entity.response.cart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 2/27/17.
 */

class RelationData {

    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
}
