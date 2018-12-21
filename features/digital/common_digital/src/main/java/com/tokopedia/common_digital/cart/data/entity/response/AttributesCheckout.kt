package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/9/17.
 */

class AttributesCheckout {
    @SerializedName("redirect_url")
    @Expose
    var redirectUrl: String? = null
    @SerializedName("callback_url_success")
    @Expose
    var callbackUrlSuccess: String? = null
    @SerializedName("callback_url_failed")
    @Expose
    var callbackUrlFailed: String? = null
    @SerializedName("query_string")
    @Expose
    var queryString: String? = null
    @SerializedName("parameter")
    @Expose
    var parameter: Parameter? = null
}
