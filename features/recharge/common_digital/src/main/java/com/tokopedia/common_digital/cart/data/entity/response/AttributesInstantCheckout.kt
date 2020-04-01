package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/23/17.
 */

class AttributesInstantCheckout {

    @SerializedName("thanks_url")
    @Expose
    var thanksUrl: String? = null
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
}
