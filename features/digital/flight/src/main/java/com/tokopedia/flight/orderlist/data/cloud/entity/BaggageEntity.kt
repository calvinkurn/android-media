package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by alvarisi on 12/6/17.
 */

class BaggageEntity(
        @SerializedName("is_up_to")
        @Expose
        var isUpTo: Boolean = false,
        @SerializedName("unit")
        @Expose
        var unit: String,
        @SerializedName("value")
        @Expose
        var value: String)
