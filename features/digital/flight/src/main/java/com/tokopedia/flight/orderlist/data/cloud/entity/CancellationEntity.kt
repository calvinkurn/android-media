package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 30/04/18.
 */

class CancellationEntity(
        @SerializedName("cancel_id")
        @Expose
        val refundId: Int,
        @SerializedName("details")
        @Expose
        val details: List<CancellationDetailsAttribute>,
        @SerializedName("create_time")
        @Expose
        val createTime: String,
        @SerializedName("estimated_refund")
        @Expose
        val estimatedRefund: String,
        @SerializedName("real_refund")
        @Expose
        val realRefund: String,
        @SerializedName("status")
        @Expose
        val status: Int)
