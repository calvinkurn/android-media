package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by alvarisi on 1/23/18.
 */

class PaymentInfoEntity(
        @SerializedName("gateway_name")
        @Expose
        val gatewayName: String = "",
        @SerializedName("gateway_icon")
        @Expose
        val gatewayIcon: String = "",
        @SerializedName("expire_on")
        @Expose
        val expireOn: String = "",
        @SerializedName("transaction_code")
        @Expose
        val transactionCode: String = "",
        @SerializedName("need_to_pay_amt")
        @Expose
        val needToPayAmount: Int = 0,
        @SerializedName("manual_transfer")
        @Expose
        val manualTransfer: ManualTransferEntity)
