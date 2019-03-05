package com.tokopedia.common_digital.cart.data.entity.requestbody.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyAppsFlyer
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier

/**
 * @author anggaprasetiyo on 3/9/17.
 */

class Attributes {

    @SerializedName("voucher_code")
    @Expose
    var voucherCode: String? = null
    @SerializedName("transaction_amount")
    @Expose
    var transactionAmount: Long? = null
    @SerializedName("ip_address")
    @Expose
    var ipAddress: String? = null
    @SerializedName("user_agent")
    @Expose
    var userAgent: String? = null
    @SerializedName("identifier")
    @Expose
    var identifier: RequestBodyIdentifier? = null
    @SerializedName("appsflyer")
    @Expose
    var appsFlyer: RequestBodyAppsFlyer? = null
    @SerializedName("client_id")
    @Expose
    var clientId: String? = null
    @SerializedName("subscribe")
    @Expose
    var subscribe: Boolean? = false
    @SerializedName("deals_ids")
    @Expose
    var dealsIds: List<Int>? = null
}
