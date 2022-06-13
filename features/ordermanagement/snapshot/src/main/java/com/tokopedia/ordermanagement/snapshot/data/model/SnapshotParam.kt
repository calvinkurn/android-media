package com.tokopedia.ordermanagement.snapshot.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 1/26/21.
 */
data class SnapshotParam(
    @Expose
    @SerializedName("order_id")
    var orderId: String = "",

    @Expose
    @SerializedName("order_detail_id")
    var orderDetailId: String = ""
)