package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ProductTrackerData(
        @SerializedName("attribution")
        val attribution: String = "",
        @SerializedName("tracker_list_name")
        val trackerListName: String = ""
)