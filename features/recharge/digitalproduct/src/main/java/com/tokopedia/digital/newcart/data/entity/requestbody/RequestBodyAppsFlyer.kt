package com.tokopedia.digital.newcart.data.entity.requestbody

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Varys Prasetyo on 14.09.17.
 */

class RequestBodyAppsFlyer {

    @SerializedName("appsflyer_id")
    @Expose
    var appsflyerId: String? = null
    @SerializedName("device_id")
    @Expose
    var deviceId: String? = null
}
