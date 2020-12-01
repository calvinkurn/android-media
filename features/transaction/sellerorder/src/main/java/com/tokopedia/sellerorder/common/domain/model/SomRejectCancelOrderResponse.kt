package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SomRejectCancelOrderResponse(
        @SerializedName("data")
        @Expose
        val data: Data = Data()
) {
    data class Data(
            @SerializedName("reject_cancel_request")
            @Expose
            val rejectCancelRequest: RejectCancelRequest = RejectCancelRequest()
    ) {
        data class RejectCancelRequest(
                @SerializedName("success")
                @Expose
                val success: Int = 0,

                @SerializedName("message")
                @Expose
                val message: String = ""
        )
    }
}