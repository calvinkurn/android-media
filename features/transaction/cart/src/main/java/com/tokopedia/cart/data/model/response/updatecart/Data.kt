package com.tokopedia.cart.data.model.response.updatecart

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.outofservice.OutOfServiceData
import com.tokopedia.purchase_platform.common.feature.toasteraction.ToasterActionData

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

data class Data(
        @SerializedName("error")
        val error: String = "",
        @SerializedName("status")
        val status: Boolean = false,
        @SerializedName("out_of_service")
        val outOfService: OutOfServiceData = OutOfServiceData(),
        @SerializedName("toaster_action")
        val toasterAction: ToasterActionData = ToasterActionData()
)