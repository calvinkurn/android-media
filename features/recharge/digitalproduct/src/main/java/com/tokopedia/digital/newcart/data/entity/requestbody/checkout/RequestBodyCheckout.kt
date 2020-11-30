package com.tokopedia.digital.newcart.data.entity.requestbody.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/9/17.
 */

class RequestBodyCheckout {

    @SerializedName("type")
    @Expose
     var type: String? = null
    @SerializedName("attributes")
    @Expose
     var attributes: Attributes? = null
    @SerializedName("relationships")
    @Expose
     var relationships: Relationships? = null
}
