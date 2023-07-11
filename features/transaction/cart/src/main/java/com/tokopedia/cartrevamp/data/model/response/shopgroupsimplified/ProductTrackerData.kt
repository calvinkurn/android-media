package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ProductTrackerData(
    @SerializedName("attribution")
    var attribution: String = "",
    @SerializedName("tracker_list_name")
    var trackerListName: String = ""
)
