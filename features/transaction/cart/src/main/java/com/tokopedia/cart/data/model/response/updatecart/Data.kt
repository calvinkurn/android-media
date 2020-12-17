package com.tokopedia.cart.data.model.response.updatecart

import com.google.gson.annotations.SerializedName
import com.tokopedia.cart.data.model.response.shopgroupsimplified.OutOfService

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

data class Data(
        @SerializedName("error")
        val error: String = "",
        @SerializedName("status")
        val status: Boolean = false,
        @SerializedName("out_of_service")
        val outOfService: OutOfService = OutOfService(),
        @SerializedName("toaster_action")
        val toasterAction: ToasterAction = ToasterAction()
)