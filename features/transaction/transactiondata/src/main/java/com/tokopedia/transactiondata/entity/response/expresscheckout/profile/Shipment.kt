package com.tokopedia.transactiondata.entity.response.expresscheckout.profile

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class Shipment(
        @SerializedName("service_id")
        val serviceId: Int,

        @SerializedName("service_duration")
        val serviceDuration: String
)