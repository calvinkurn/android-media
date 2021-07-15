package com.tokopedia.cart_common.data.response.updatecart

import com.google.gson.annotations.SerializedName
import com.tokopedia.cart_common.data.response.common.OutOfService

data class Data(
        @SerializedName("error")
        val error: String = "",
        @SerializedName("status")
        val status: Boolean = false,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("out_of_service")
        val outOfService: OutOfService = OutOfService(),
        @SerializedName("toaster_action")
        val toasterAction: ToasterAction = ToasterAction()
)