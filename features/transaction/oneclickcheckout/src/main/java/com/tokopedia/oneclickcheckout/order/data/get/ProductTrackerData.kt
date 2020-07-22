package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

data class ProductTrackerData(
    @SerializedName("attribution")
    var attribution: String = "",
    @SerializedName("tracker_list_name")
    var trackerListName: String = ""
)
