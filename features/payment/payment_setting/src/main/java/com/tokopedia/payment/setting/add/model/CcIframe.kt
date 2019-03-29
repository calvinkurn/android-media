package com.tokopedia.payment.setting.add.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CcIframe {

    @SerializedName("merchant_code")
    @Expose
    var merchantCode: String? = null
    @SerializedName("profile_code")
    @Expose
    var profileCode: String? = null
    @SerializedName("ip_address")
    @Expose
    var ipAddress: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: String? = null
    @SerializedName("customer_name")
    @Expose
    var customerName: String? = null
    @SerializedName("customer_email")
    @Expose
    var customerEmail: String? = null
    @SerializedName("callback_url")
    @Expose
    var callbackUrl: String? = null
    @SerializedName("cc_token")
    @Expose
    var ccToken: String? = null
    @SerializedName("signature")
    @Expose
    var signature: String? = null

}
