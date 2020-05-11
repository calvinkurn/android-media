package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/9/17.
 */

open class AttributesCheckout (
    @SerializedName("redirect_url")
    @Expose
    open val redirectUrl: String? = null,
    @SerializedName("callback_url_success")
    @Expose
    open val callbackUrlSuccess: String? = null,
    @SerializedName("callback_url_failed")
    @Expose
    open val callbackUrlFailed: String? = null,
    @SerializedName("query_string")
    @Expose
    open val queryString: String? = null,
    @SerializedName("parameter")
    @Expose
    open val parameter: Parameter? = null,
    @SerializedName("thanks_url")
    @Expose
    open val thanksUrl: String? = null
)
