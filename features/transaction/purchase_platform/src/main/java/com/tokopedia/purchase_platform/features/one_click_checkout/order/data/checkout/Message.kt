package com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-11-12.
 */

data class Message(
        @SerializedName("title")
        val title: String = "",

        @SerializedName("desc")
        val desc: String = "",

        @SerializedName("action")
        val action: String
)