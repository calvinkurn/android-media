package com.tokopedia.common_digital.atc.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import java.util.*

data class Attributes(

        @SuppressLint("Invalid Data Type")
        @SerializedName("user_id")
        @Expose
        var userId: Long = 0L,

        @SuppressLint("Invalid Data Type")
        @SerializedName("product_id")
        @Expose
        var productId: Long = 0L,

        @SuppressLint("Invalid Data Type")
        @SerializedName("order_id")
        @Expose
        var orderId: Long = 0L,

        @SuppressLint("Invalid Data Type")
        @SerializedName("device_id")
        @Expose
        var deviceId: Int = 0,

        @SerializedName("instant_checkout")
        @Expose
        var instantCheckout: Boolean = false,

        @SerializedName("ip_address")
        @Expose
        var ipAddress: String = "",

        @SerializedName("user_agent")
        @Expose
        var userAgent: String = "",

        @SerializedName("fields")
        @Expose
        var fields: List<Field> = ArrayList(),

        @SerializedName("identifier")
        @Expose
        var identifier: RequestBodyIdentifier = RequestBodyIdentifier(),

        @SerializedName("is_reseller")
        @Expose
        var isReseller: Boolean = false,

        @SerializedName("show_subscribe_flag")
        @Expose
        var showSubscribeFlag: Boolean = false,

        @SerializedName("show_subscribe_pop_up")
        @Expose
        var showSubscribePopUp: Boolean = false,

        @SerializedName("auto_subscribe")
        @Expose
        var autoSubscribe: Boolean = false,

        @SerializedName("is_thankyou_native")
        @Expose
        var isThankyouNative: Boolean = false,

        @SerializedName("is_thankyou_native_new")
        @Expose
        var isThankyouNativeNew: Boolean = false,

        @SerializedName("atc_source")
        @Expose
        var atcSource: String = ""
) {
    data class Field(
            @SerializedName("name")
            @Expose
            var name: String = "",

            @SerializedName("value")
            @Expose
            var value: String = ""
    )
}