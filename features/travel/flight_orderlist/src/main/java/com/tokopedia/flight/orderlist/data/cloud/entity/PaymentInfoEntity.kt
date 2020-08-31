package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by alvarisi on 1/23/18.
 */

class PaymentInfoEntity(
        @SerializedName("gateway_name")
        @Expose
        var gatewayName: String = "",
        @SerializedName("gateway_icon")
        @Expose
        var gatewayIcon: String = "",
        @SerializedName("expire_on")
        @Expose
        var expireOn: String = "",
        @SerializedName("transaction_code")
        @Expose
        var transactionCode: String = "",
        @SerializedName("need_to_pay_amt")
        @Expose
        var needToPayAmount: Int = 0,
        @SerializedName("manual_transfer")
        @Expose
        var manualTransfer: ManualTransferEntity = ManualTransferEntity())
