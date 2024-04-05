package com.tokopedia.sessioncommon.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 5/26/17.
 * Used by WSService.java (InstrumentationAuthHelper)
 */
class MakeLoginPojo {
    @SerializedName("shop_is_gold")
    @Expose
    var shopIsGold = 0

    @SerializedName("msisdn_is_verified")
    @Expose
    var msisdnIsVerified = ""

    @SerializedName("shop_id")
    @Expose
    var shopId: String? = null

    @SerializedName("shop_name")
    @Expose
    var shopName = ""

    @SerializedName("full_name")
    @Expose
    var fullName = ""

    /**
     * @return "true" or "false"
     */
    @SerializedName("is_login")
    @Expose
    var isLogin = ""

    @SerializedName("shop_has_terms")
    @Expose
    var shopHasTerms = 0

    @SerializedName("shop_is_official")
    @Expose
    var shopIsOfficial = 0

    @SerializedName("is_register_device")
    @Expose
    var isRegisterDevice = 0

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("msisdn_show_dialog")
    @Expose
    var msisdnShowDialog = 0

    @SerializedName("shop_avatar")
    @Expose
    var shopAvatar = ""

    @SerializedName("user_image")
    @Expose
    var userImage = ""

    @SerializedName("security")
    @Expose
    val securityPojo = SecurityPojo()
}
