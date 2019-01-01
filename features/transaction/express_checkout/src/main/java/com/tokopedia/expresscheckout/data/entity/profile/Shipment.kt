package com.tokopedia.expresscheckout.data.entity.profile

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class Shipment(
        @SerializedName("service_id")
        val serviceId: Int
)