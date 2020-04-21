package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 04/06/18.
 */
data class ProductTrackerData(
    @SerializedName("attribution")
    @Expose
    var attribution: String = "",
    @SerializedName("tracker_list_name")
    @Expose
    var trackerListName: String = ""
)
