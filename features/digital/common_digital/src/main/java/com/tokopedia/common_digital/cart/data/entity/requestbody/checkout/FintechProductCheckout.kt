package com.tokopedia.common_digital.cart.data.entity.requestbody.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyAppsFlyer
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier

/**
 * @author anggaprasetiyo on 3/9/17.
 */

class FintechProductCheckout (
    @SerializedName("transaction_type")
    @Expose
    var transactionType: String? = null,
    @SerializedName("tier_id")
    @Expose
    var tierId: Int = 0,
    @SerializedName("user_id")
    @Expose
    var userId: String? = null,
    @SerializedName("fintech_amount")
    @Expose
    var fintechAmount: Long = 0,
    @SerializedName("fintech_partner_amount")
    @Expose
    var fintechPartnerAmount: Long = 0
)
