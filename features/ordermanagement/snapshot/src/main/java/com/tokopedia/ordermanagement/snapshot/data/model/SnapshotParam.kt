package com.tokopedia.ordermanagement.snapshot.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 1/26/21.
 */
data class SnapshotParam (
        @SerializedName("order_id")
        var orderId: String = "",

        @SerializedName("order_detail_id")
        var orderDetailId: String = "")