package com.tokopedia.recharge_credit_card.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeCCSignatureReponse(
        @SerializedName("rechargePCIDSSSignature")
        @Expose
        val rechargeSignature: RechargeCCSignature = RechargeCCSignature())

class RechargeCCSignature(
        @SerializedName("signature")
        @Expose
        val signature: String = "",
        @SerializedName("message_error")
        @Expose
        val messageError: String = "")