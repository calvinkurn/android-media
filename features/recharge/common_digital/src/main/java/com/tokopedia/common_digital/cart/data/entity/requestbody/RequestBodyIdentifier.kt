package com.tokopedia.common_digital.cart.data.entity.requestbody

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 27/08/18.
 */
class RequestBodyIdentifier {

    @SerializedName("user_id")
    @Expose
    var userId: String? = null
    @SerializedName("device_token")
    @Expose
    var deviceToken: String? = null
    @SerializedName("os_type")
    @Expose
    var osType: String? = null
}
