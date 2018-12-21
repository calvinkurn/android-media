package com.tokopedia.common_digital.cart.data.entity.requestbody.atc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier

import java.util.ArrayList

/**
 * Created by Rizky on 27/08/18.
 */
class Attributes {

    @SerializedName("user_id")
    @Expose
    var userId: Int = 0
    @SerializedName("product_id")
    @Expose
    var productId: Int = 0
    @SerializedName("device_id")
    @Expose
    var deviceId: Int = 0
    @SerializedName("instant_checkout")
    @Expose
    var instantCheckout: Boolean = false
    @SerializedName("ip_address")
    @Expose
    var ipAddress: String? = null
    @SerializedName("user_agent")
    @Expose
    var userAgent: String? = null
    @SerializedName("fields")
    @Expose
    var fields: List<Field> = ArrayList()
    @SerializedName("identifier")
    @Expose
    var identifier: RequestBodyIdentifier? = null
    @SerializedName("is_reseller")
    @Expose
    var isReseller: Boolean = false
    @SerializedName("show_subscribe_flag")
    @Expose
    var showSubscribeFlag: Boolean = false
    @SerializedName("is_thankyou_native")
    @Expose
    var isThankyouNative: Boolean = false
    @SerializedName("is_thankyou_native_new")
    @Expose
    var isThankyouNativeNew: Boolean = false
}