package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 30/04/18.
 */

class CancellationEntity(
        @SerializedName("cancel_id")
        @Expose
        val refundId: String = "",
        @SerializedName("details")
        @Expose
        val details: List<CancellationDetailsAttribute>,
        @SerializedName("create_time")
        @Expose
        val createTime: String = "",
        @SerializedName("estimated_refund")
        @Expose
        val estimatedRefund: String = "",
        @SerializedName("real_refund")
        @Expose
        val realRefund: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("status_str")
        @Expose
        val statusStr: String = "",
        @SerializedName("status_type")
        @Expose
        val statusType: String = "",
        @SerializedName("refund_info")
        @Expose
        val refundInfo: String = "",
        @SerializedName("refund_detail")
        @Expose
        val refundDetail: RefundDetailEntity = RefundDetailEntity())
