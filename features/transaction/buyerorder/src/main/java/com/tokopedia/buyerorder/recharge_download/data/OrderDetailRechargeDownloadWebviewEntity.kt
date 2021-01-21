package com.tokopedia.buyerorder.recharge_download.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 21/01/2021
 */
class OrderDetailRechargeDownloadWebviewEntity(
        @SerializedName("data")
        @Expose
        val data: String = ""
) {
    data class Response(
            @SerializedName("rechargeEncodeInvoice")
            @Expose
            val rechargeEncodeInvoice: OrderDetailRechargeDownloadWebviewEntity = OrderDetailRechargeDownloadWebviewEntity()
    )
}