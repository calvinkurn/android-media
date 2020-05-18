package com.tokopedia.recharge_credit_card.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeCCBankListReponse(
        @SerializedName("rechargePCIDSSSignature")
        @Expose
        val rechargeCCBankList: RechargeCCBankList = RechargeCCBankList())

class RechargeCCBankList(
        @SerializedName("bank_list")
        @Expose
        val bankList: List<RechargeCCBank> = listOf(),
        @SerializedName("message_error")
        @Expose
        val messageError: String = "")

class RechargeCCBank(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "")